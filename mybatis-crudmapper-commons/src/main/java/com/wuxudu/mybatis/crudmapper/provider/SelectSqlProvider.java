package com.wuxudu.mybatis.crudmapper.provider;

import com.wuxudu.mybatis.crudmapper.domain.condition.Condition;
import com.wuxudu.mybatis.crudmapper.domain.param.SelectParam;
import com.wuxudu.mybatis.crudmapper.domain.sort.Sort;
import com.wuxudu.mybatis.crudmapper.mapping.JpaTable;
import com.wuxudu.mybatis.crudmapper.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

public class SelectSqlProvider extends ConditionalSqlProvider {

    public String sql(ProviderContext context, SelectParam<?> param) {

        Validator.argName("param").notNull(param);

        JpaTable jpaTable = this.getJpaTable(context);
        String tableName = jpaTable.getTableName();

        Condition condition = param.getCondition();
        String whereSql = this.getWhereSql(condition, jpaTable);
        String orderBySql = this.getOrderBySql(param, jpaTable);

        return new SQL() {
            {
                SELECT("*");

                FROM(tableName);

                if (StringUtils.isNotBlank(whereSql)) {
                    WHERE(whereSql);
                }

                if (StringUtils.isNotBlank(orderBySql)) {
                    ORDER_BY(orderBySql);
                }

                OFFSET(param.getOffset());
                LIMIT(param.getLimit());
            }
        }.toString();

    }

    private String getOrderBySql(SelectParam<?> param, JpaTable jpaTable) {
        List<String> cols = new ArrayList<>();
        for (Sort sort : param.getSorts()) {
            if (sort != null) {
                String fieldName = sort.getFieldName();
                String columnName = jpaTable.getColumn(fieldName).getColumnName();
                cols.add(columnName + " " + (sort.isAsc() ? "ASC" : "DESC"));
            }
        }
        return StringUtils.join(cols, ",");
    }

}
