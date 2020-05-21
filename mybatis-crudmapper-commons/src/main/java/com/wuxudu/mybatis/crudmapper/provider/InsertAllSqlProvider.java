package com.wuxudu.mybatis.crudmapper.provider;

import com.wuxudu.mybatis.crudmapper.mapping.JpaColumn;
import com.wuxudu.mybatis.crudmapper.mapping.JpaTable;
import com.wuxudu.mybatis.crudmapper.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InsertAllSqlProvider extends AbstractSqlProvider {

    public String sql(ProviderContext context, @Param("collection") Collection<?> entities) {

        Validator.argName("entities").notEmpty(entities);

        JpaTable jpaTable = this.getJpaTable(context);
        String tableName = jpaTable.getTableName();

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(tableName);

        List<String> columnNames = new ArrayList<>();
        List<String> fieldNames = new ArrayList<>();

        for (JpaColumn column : jpaTable.getColumns()) {
            if (column.isInsertable()) {
                columnNames.add(column.getColumnName());
                fieldNames.add(column.getFieldName());
            }
        }

        sb.append(" (");
        sb.append(StringUtils.join(columnNames, ","));
        sb.append(") ");
        sb.append("VALUES ");

        List<String> rows = new ArrayList<>();

        for (int i = 0; i < entities.size(); i++) {

            List<String> cols = new ArrayList<>();

            for (String fieldName : fieldNames) {
                cols.add("#{collection[" + i + "]." + fieldName + "}");
            }

            rows.add("(" + StringUtils.join(cols, ",") + ")");
        }

        sb.append(StringUtils.join(rows, ","));

        return sb.toString();

    }
}
