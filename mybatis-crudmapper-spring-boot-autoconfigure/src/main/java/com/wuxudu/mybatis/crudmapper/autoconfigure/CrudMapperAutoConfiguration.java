package com.wuxudu.mybatis.crudmapper.autoconfigure;

import com.wuxudu.mybatis.crudmapper.domain.CrudMapper;
import com.wuxudu.mybatis.crudmapper.exception.CrudMapperException;
import com.wuxudu.mybatis.crudmapper.mapping.JpaColumn;
import com.wuxudu.mybatis.crudmapper.mapping.JpaTable;
import com.wuxudu.mybatis.crudmapper.mapping.JpaTableManager;
import com.wuxudu.mybatis.crudmapper.resultmap.InlineResultMap;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ReflectionUtils;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

@Configuration
@Import(CrudMapperAutoConfiguration.AutoConfiguredTableScannerRegistrar.class)
public class CrudMapperAutoConfiguration implements InitializingBean {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private List<CrudMapper<?>> crudMappers;

    @Autowired
    private JpaTableManager jpaTableManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (sqlSessionFactory != null && crudMappers != null && jpaTableManager != null) {
            crudMappers.forEach(crudMapper -> {
                if (Proxy.isProxyClass(crudMapper.getClass())) {

                    Class<?> mapperInterface = this.getMapperClass(crudMapper);
                    Type type = this.getParameterizedType(mapperInterface);

                    if (mapperInterface != null && type != null) {

                        JpaTableManager mgr = JpaTableManager.getInstance();
                        JpaTable jpaTable = mgr.getAnnotatedJpaTables().get(type.getTypeName());
                        if (jpaTable == null) {
                            throw new CrudMapperException("Annotation @Table not found in class " + type.getTypeName());
                        }

                        org.apache.ibatis.session.Configuration mybatisConfig = sqlSessionFactory.getConfiguration();

                        // replace select resultmap
                        String resultMapId = mapperInterface.getName() + ".select-SelectParam";
                        ResultMap resultMap = mybatisConfig.getResultMap(resultMapId);

                        if (resultMap != null) {

                            mgr.mapping(mapperInterface, jpaTable);

                            InlineResultMap inlineResultMap = new InlineResultMap(resultMapId, jpaTable, mybatisConfig);
                            ResultMap newResultMap = inlineResultMap.build();

                            String[] properties = {
                                    "resultMappings",
                                    "idResultMappings",
                                    "propertyResultMappings",
                                    "mappedColumns",
                                    "mappedProperties"
                            };

                            Object[] values = {
                                    newResultMap.getResultMappings(),
                                    newResultMap.getIdResultMappings(),
                                    newResultMap.getPropertyResultMappings(),
                                    newResultMap.getMappedColumns(),
                                    newResultMap.getMappedProperties()
                            };

                            for (int i = 0; i < properties.length; i++) {
                                String propertyName = properties[i];
                                Object value = values[i];
                                Field field = ReflectionUtils.findField(resultMap.getClass(), propertyName);
                                ReflectionUtils.makeAccessible(field);
                                ReflectionUtils.setField(field, resultMap, value);
                            }
                        }

                        // replace keyProperty and keyColumn
                        JpaColumn idColumn = jpaTable.getIdColumn();
                        if (idColumn != null) {

                            String[] properties = {
                                    "keyProperties",
                                    "keyColumns"
                            };

                            Object[] values = {
                                    new String[]{idColumn.getFieldName()},
                                    new String[]{idColumn.getColumnName()}
                            };

                            String[] methodNames = {
                                    "insertOne",
                                    "insertAll"
                            };

                            for (String methodName : methodNames) {
                                String statementId = mapperInterface.getName() + "." + methodName;
                                MappedStatement statement = mybatisConfig.getMappedStatement(statementId);
                                for (int i = 0; i < properties.length; i++) {
                                    String propertyName = properties[i];
                                    Object value = values[i];
                                    Field field = ReflectionUtils.findField(statement.getClass(), propertyName);
                                    ReflectionUtils.makeAccessible(field);
                                    ReflectionUtils.setField(field, statement, value);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private Class<?> getMapperClass(Object proxy) {
        Field h = ReflectionUtils.findField(proxy.getClass(), "h");
        ReflectionUtils.makeAccessible(h);
        MapperProxy mapperProxy = (MapperProxy) ReflectionUtils.getField(h, proxy);
        Field mapperInterface = ReflectionUtils.findField(mapperProxy.getClass(), "mapperInterface");
        ReflectionUtils.makeAccessible(mapperInterface);
        return (Class<?>) ReflectionUtils.getField(mapperInterface, mapperProxy);
    }

    private Type getParameterizedType(Class<?> clazz) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (ParameterizedType.class.isAssignableFrom(genericInterface.getClass())) {
                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments) {
                    return actualTypeArgument;
                }
            }
        }
        return null;
    }

    public static class AutoConfiguredTableScannerRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar {

        Logger logger = Logger.getLogger(AutoConfiguredTableScannerRegistrar.class.getName());

        private BeanFactory beanFactory;

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Bean
        @ConditionalOnMissingBean
        public JpaTableManager jpaTableManager() {

            if (!AutoConfigurationPackages.has(this.beanFactory)) {
                logger.warning("Could not determine auto-configuration package, automatic @Table mappings scanning disabled");
                return null;
            }

            logger.info("Searching for classes annotated with @Table");

            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(Table.class));

            JpaTableManager mgr = JpaTableManager.getInstance();

            List<String> basePackages = AutoConfigurationPackages.get(this.beanFactory);
            for (final String basePackage : basePackages) {
                scanner.findCandidateComponents(basePackage).forEach(beanDefinition -> {
                    String className = beanDefinition.getBeanClassName();
                    mgr.register(className);
                });
            }

            int count = mgr.getAnnotatedJpaTables().size();
            logger.info(count + " @Table annotation(s) found");

            return mgr;
        }
    }

}
