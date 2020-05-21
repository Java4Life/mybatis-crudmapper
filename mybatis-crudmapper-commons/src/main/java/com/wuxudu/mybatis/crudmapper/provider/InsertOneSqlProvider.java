package com.wuxudu.mybatis.crudmapper.provider;

import com.wuxudu.mybatis.crudmapper.mapping.JpaColumn;
import com.wuxudu.mybatis.crudmapper.mapping.JpaTable;
import com.wuxudu.mybatis.crudmapper.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.ArrayList;
import java.util.List;

public class InsertOneSqlProvider extends AbstractSqlProvider {

    public String sql(ProviderContext context, Object entity) {

        Validator.argName("entity").notNull(entity);

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

        List<String> cols = new ArrayList<>();

        for (String fieldName : fieldNames) {
            cols.add("#{" + fieldName + "}");
        }

        sb.append("(" + StringUtils.join(cols, ",") + ")");

        return sb.toString();

    }

}
