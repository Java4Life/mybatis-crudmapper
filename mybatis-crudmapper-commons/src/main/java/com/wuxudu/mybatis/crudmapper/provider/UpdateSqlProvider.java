package com.wuxudu.mybatis.crudmapper.provider;

import com.wuxudu.mybatis.crudmapper.domain.condition.Condition;
import com.wuxudu.mybatis.crudmapper.domain.param.UpdateParam;
import com.wuxudu.mybatis.crudmapper.mapping.JpaColumn;
import com.wuxudu.mybatis.crudmapper.mapping.JpaTable;
import com.wuxudu.mybatis.crudmapper.provider.util.FieldUtils;
import com.wuxudu.mybatis.crudmapper.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

public class UpdateSqlProvider extends ConditionalSqlProvider {

    public String sql(ProviderContext context, UpdateParam<?> param) {

        Validator.argName("param").notNull(param);
        Validator.argName("param.entity").notNull(param.value());

        JpaTable jpaTable = this.getJpaTable(context);
        String tableName = jpaTable.getTableName();

        Object entity = param.value();
        List<JpaColumn> toUpdate = new ArrayList<>();
        for (JpaColumn column : jpaTable.getColumns()) {
            if (column.isUpdatable() && FieldUtils.hasValue(entity, column.getFieldName())) {
                toUpdate.add(column);
            }
        }

        Condition condition = param.getCondition();
        String whereSql = this.getWhereSql(condition, jpaTable);

        return new SQL() {
            {
                UPDATE(tableName);

                toUpdate.forEach(item -> {
                    String columnName = item.getColumnName();
                    String fieldName = item.getFieldName();
                    SET(columnName + " = #{entity." + fieldName + "}");
                });

                if (StringUtils.isNotBlank(whereSql)) {
                    WHERE(whereSql);
                }
            }
        }.toString();
    }

}
