package com.wuxudu.mybatis.crudmapper.autoconfigure;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ReflectionUtils;

import com.wuxudu.mybatis.crudmapper.domain.CrudMapper;
import com.wuxudu.mybatis.crudmapper.exception.CrudMapperException;
import com.wuxudu.mybatis.crudmapper.mapping.JpaColumn;
import com.wuxudu.mybatis.crudmapper.mapping.JpaTable;
import com.wuxudu.mybatis.crudmapper.mapping.JpaTableManager;
import com.wuxudu.mybatis.crudmapper.resultmap.InlineResultMap;

@Configuration
@Import(CrudMapperAutoConfiguration.AutoConfiguredTableScannerRegistrar.class)
public class CrudMapperAutoConfiguration implements InitializingBean {

	private static final String SELECT_RESULT_MAP_ID = "select-SelectParam";
	private static final String RESULTMAP_PROPERTY_RESULTMAPPINGS = "resultMappings";
	private static final String RESULTMAP_PROPERTY_IDRESULTMAPPINGS = "idResultMappings";
	private static final String RESULTMAP_PROPERTY_PROPERTYRESULTMAPPINGS = "propertyResultMappings";
	private static final String RESULTMAP_PROPERTY_MAPPEDCOLUMNS = "mappedColumns";
	private static final String RESULTMAP_PROPERTY_MAPPEDPROPERTIES = "mappedProperties";
	private static final String MAPPEDSTATEMENT_PROPERTY_KEYPROPERTIES = "keyProperties";
	private static final String MAPPEDSTATEMENT_PROPERTY_KEYCOLUMNS = "keyColumns";
	private static final String MAPPEDSTATEMENT_METHODNAME_INSERTONE = "insertOne";
	private static final String MAPPEDSTATEMENT_METHODNAME_INSERTALL = "insertAll";
	private static final String PROXY_PROPERTY_H = "h";
	private static final String PROXY_PROPERTY_MAPPERINTERFACE = "mapperInterface";

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Autowired
	private List<CrudMapper<?>> crudMappers;

	@Autowired
	private JpaTableManager jpaTableManager;

	@Override
	public void afterPropertiesSet() throws Exception {

		if (this.sqlSessionFactory == null) {
			throw new CrudMapperException("No SqlSessionFactory instance autowired");
		}

		if (CollectionUtils.isEmpty(this.crudMappers)) {
			throw new CrudMapperException("No CrudMapper instances autowired");
		}

		if (this.jpaTableManager == null) {
			throw new CrudMapperException("No JpaTableManager instance autowired");
		}

		JpaTableManager mgr = JpaTableManager.getInstance();

		for (CrudMapper<?> crudMapper : this.crudMappers) {

			// check proxy
			Class<?> mapperClass = crudMapper.getClass();
			if (!Proxy.isProxyClass(mapperClass)) {
				throw new CrudMapperException("CrudMapper class " + mapperClass + " is not a proxy class");
			}

			// check interface
			Class<?> mapperInterface = this.getMapperInterface(crudMapper);
			if (mapperInterface == null) {
				throw new CrudMapperException("CrudMapper interface not found for " + mapperClass);
			}

			// check parameterized type
			Type type = this.getParameterizedType(mapperInterface);
			if (type == null) {
				throw new CrudMapperException("CrudMapper parameterized type not defined for " + mapperInterface.getName());
			}

			// check jpaTable
			JpaTable jpaTable = mgr.getAnnotatedJpaTables().get(type.getTypeName());
			if (jpaTable == null) {
				throw new CrudMapperException("Annotation @Table not found in class " + type.getTypeName());
			}
			mgr.mapping(mapperInterface, jpaTable);

			// reflect select resultMap
			this.replaceSelectResultMapConfig(mapperInterface, jpaTable);

			// reflect insert keyProperty and keyColumn
			this.replaceInsertKeyConfig(mapperInterface, jpaTable);
		}
	}

	private void replaceSelectResultMapConfig(Class<?> mapperInterface, JpaTable jpaTable) {
		// get original select ResultMap
		org.apache.ibatis.session.Configuration mybatisConfig = this.sqlSessionFactory.getConfiguration();
		String resultMapId = mapperInterface.getName() + "." + SELECT_RESULT_MAP_ID;
		ResultMap resultMap = mybatisConfig.getResultMap(resultMapId);
		if (resultMap == null) {
			throw new CrudMapperException("ResultMap " + resultMapId + " not found in org.apache.ibatis.session.Configuration");
		}

		// reflect select ResultMap properties
		ResultMap newResultMap = new InlineResultMap(resultMapId, jpaTable, mybatisConfig).build();
		Map<String, Object> substitutes = new HashMap<>();
		substitutes.put(RESULTMAP_PROPERTY_RESULTMAPPINGS, newResultMap.getResultMappings());
		substitutes.put(RESULTMAP_PROPERTY_IDRESULTMAPPINGS, newResultMap.getIdResultMappings());
		substitutes.put(RESULTMAP_PROPERTY_PROPERTYRESULTMAPPINGS, newResultMap.getPropertyResultMappings());
		substitutes.put(RESULTMAP_PROPERTY_MAPPEDCOLUMNS, newResultMap.getMappedColumns());
		substitutes.put(RESULTMAP_PROPERTY_MAPPEDPROPERTIES, newResultMap.getMappedProperties());
		substitutes.forEach((k, v) -> {
			Field field = ReflectionUtils.findField(resultMap.getClass(), k);
			ReflectionUtils.makeAccessible(field);
			ReflectionUtils.setField(field, resultMap, v);
		});
	}

	private void replaceInsertKeyConfig(Class<?> mapperInterface, JpaTable jpaTable) {

		org.apache.ibatis.session.Configuration mybatisConfig = this.sqlSessionFactory.getConfiguration();

		JpaColumn idColumn = jpaTable.getIdColumn();

		if (idColumn != null && !idColumn.isInsertable()) {
			String[] methodNames = { MAPPEDSTATEMENT_METHODNAME_INSERTONE, MAPPEDSTATEMENT_METHODNAME_INSERTALL };
			Map<String, Object> substitutes = new HashMap<>();
			substitutes.put(MAPPEDSTATEMENT_PROPERTY_KEYPROPERTIES, new String[] { idColumn.getFieldName() });
			substitutes.put(MAPPEDSTATEMENT_PROPERTY_KEYCOLUMNS, new String[] { idColumn.getColumnName() });
			for (String methodName : methodNames) {
				String statementId = mapperInterface.getName() + "." + methodName;
				MappedStatement statement = mybatisConfig.getMappedStatement(statementId);
				substitutes.forEach((k, v) -> {
					Field field = ReflectionUtils.findField(statement.getClass(), k);
					ReflectionUtils.makeAccessible(field);
					ReflectionUtils.setField(field, statement, v);
				});
			}
		}
	}

	private Class<?> getMapperInterface(Object proxy) {
		Field h = ReflectionUtils.findField(proxy.getClass(), PROXY_PROPERTY_H);
		ReflectionUtils.makeAccessible(h);
		MapperProxy<?> mapperProxy = (MapperProxy<?>) ReflectionUtils.getField(h, proxy);
		Field mapperInterface = ReflectionUtils.findField(mapperProxy.getClass(), PROXY_PROPERTY_MAPPERINTERFACE);
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
