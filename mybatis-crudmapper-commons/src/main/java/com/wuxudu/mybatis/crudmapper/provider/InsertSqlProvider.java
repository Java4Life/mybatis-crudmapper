package com.wuxudu.mybatis.crudmapper.provider;

import com.wuxudu.mybatis.crudmapper.domain.param.InsertParam;
import com.wuxudu.mybatis.crudmapper.mapping.JpaColumn;
import com.wuxudu.mybatis.crudmapper.mapping.JpaTable;
import com.wuxudu.mybatis.crudmapper.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.ArrayList;
import java.util.List;

public class InsertSqlProvider extends AbstractSqlProvider {

    public String sql(ProviderContext context, InsertParam<?> param) {

        Validator.argName("param").notNull(param);
        Validator.argName("param.entities").notEmpty(param.values());

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

        for (int i = 0; i < param.values().length; i++) {

            List<String> cols = new ArrayList<>();

            for (String fieldName : fieldNames) {
                cols.add("#{entities[" + i + "]." + fieldName + "}");
            }

            rows.add("(" + StringUtils.join(cols, ",") + ")");
        }

        sb.append(StringUtils.join(rows, ","));

        return sb.toString();

    }
}
