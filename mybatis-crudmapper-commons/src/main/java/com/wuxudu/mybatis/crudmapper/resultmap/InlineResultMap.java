package com.wuxudu.mybatis.crudmapper.resultmap;

import com.wuxudu.mybatis.crudmapper.mapping.JpaColumn;
import com.wuxudu.mybatis.crudmapper.mapping.JpaTable;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

public final class InlineResultMap {

    private final String id;
    private final JpaTable jpaTable;
    private final Configuration configuration;

    public InlineResultMap(String id, JpaTable jpaTable, Configuration configuration) {
        this.id = id;
        this.jpaTable = jpaTable;
        this.configuration = configuration;
    }

    public ResultMap build() {

        Class<?> type = jpaTable.getTableClass();
        List<ResultMapping> resultMappings = new ArrayList<>();

        for (JpaColumn column : jpaTable.getColumns()) {
            String fieldName = column.getFieldName();
            String columnName = column.getColumnName();
            Class<?> fieldType = column.getFieldType();

            ResultMapping.Builder builder = new ResultMapping.Builder(configuration, fieldName, columnName, fieldType);

            if (column.isId()) {
                List<ResultFlag> flags = new ArrayList<>();
                flags.add(ResultFlag.ID);
                builder.flags(flags);
            }

            resultMappings.add(builder.build());
        }

        ResultMap resultMap = new ResultMap.Builder(configuration, id, type, resultMappings).build();

        return resultMap;
    }
}
