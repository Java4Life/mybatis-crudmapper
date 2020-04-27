package com.wuxudu.mybatis.crudmapper.provider;

import com.wuxudu.mybatis.crudmapper.mapping.JpaTable;
import com.wuxudu.mybatis.crudmapper.mapping.JpaTableManager;
import org.apache.ibatis.builder.annotation.ProviderContext;

public abstract class AbstractSqlProvider {

    protected JpaTable getJpaTable(ProviderContext context) {
        Class<?> mapperType = context.getMapperType();
        JpaTableManager mgr = JpaTableManager.getInstance();
        return mgr.getMappedJpaTables().get(mapperType.getName());
    }
}
