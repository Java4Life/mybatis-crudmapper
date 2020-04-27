package com.wuxudu.mybatis.crudmapper.provider;

import com.wuxudu.mybatis.crudmapper.domain.condition.Condition;
import com.wuxudu.mybatis.crudmapper.domain.param.CountParam;
import com.wuxudu.mybatis.crudmapper.mapping.JpaTable;
import com.wuxudu.mybatis.crudmapper.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

public class CountSqlProvider extends ConditionalSqlProvider {

    public String sql(ProviderContext context, CountParam<?> param) {

        Validator.argName("param").notNull(param);

        JpaTable jpaTable = this.getJpaTable(context);
        String tableName = jpaTable.getTableName();

        Condition condition = param.getCondition();
        String whereSql = this.getWhereSql(condition, jpaTable);

        return new SQL() {
            {
                SELECT("count(*)");

                FROM(tableName);

                if (StringUtils.isNotBlank(whereSql)) {
                    WHERE(whereSql);
                }
            }
        }.toString();

    }
}
