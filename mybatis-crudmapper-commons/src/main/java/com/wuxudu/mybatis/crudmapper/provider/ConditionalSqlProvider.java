package com.wuxudu.mybatis.crudmapper.provider;

import com.wuxudu.mybatis.crudmapper.domain.condition.Condition;
import com.wuxudu.mybatis.crudmapper.domain.condition.ConditionElement;
import com.wuxudu.mybatis.crudmapper.domain.condition.ConditionGroup;
import com.wuxudu.mybatis.crudmapper.mapping.JpaTable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class ConditionalSqlProvider extends AbstractSqlProvider {

    private static final String CONDITION_VALUES = "conditionValues";

    protected String getWhereSql(Condition condition, JpaTable jpaTable) {
        if (condition == null) {
            return null;
        } else if (condition instanceof ConditionElement) {
            return this.parseSql((ConditionElement) condition, jpaTable);
        } else if (condition instanceof ConditionGroup) {
            String sql = this.parseSql((ConditionGroup) condition, jpaTable);
            if (((ConditionGroup) condition).getConditions().size() > 1) {
                sql = StringUtils.removeStart(sql, "(");
                sql = StringUtils.removeEnd(sql, ")");
            }
            return sql;
        } else {
            return null;
        }
    }

    private String parseSql(ConditionElement element, JpaTable jpaTable) {

        String sqlFormat = "%s %s %s";

        String fieldName = element.getFieldName();
        ConditionElement.Operator operator = element.getOperator();
        Object parameter = element.getParameter();
        String parameterKey = element.getParameterKey();

        String columnName = jpaTable.getColumn(fieldName).getColumnName();
        String operatorSymbol = operator.getSymbol();
        String parameterHolder;

        if (operator == ConditionElement.Operator.In) {

            List<String> items = new ArrayList<>();
            for (int i = 0; i < ((List<?>) parameter).size(); i++) {
                items.add(String.format("#{%s.%s[%d]}", CONDITION_VALUES, parameterKey, i));
            }
            parameterHolder = "(" + StringUtils.join(items, ",") + ")";

        } else {
            parameterHolder = String.format("#{%s.%s}", CONDITION_VALUES, parameterKey);
        }

        return String.format(sqlFormat, columnName, operatorSymbol, parameterHolder);
    }

    private String parseSql(ConditionGroup group, JpaTable jpaTable) {

        List<String> items = new ArrayList<>();

        group.getConditions().forEach(condition -> {
            String sql = null;
            if (condition instanceof ConditionElement) {
                sql = this.parseSql((ConditionElement) condition, jpaTable);
            } else if (condition instanceof ConditionGroup) {
                sql = this.parseSql((ConditionGroup) condition, jpaTable);
            }
            if (StringUtils.isNotBlank(sql)) {
                items.add(sql);
            }
        });

        ConditionGroup.Operator operator = group.getOperator();
        String operatorSymbol = operator.getSymbol();

        String sql = StringUtils.join(items, " " + operatorSymbol + " ");

        if (items.size() > 1) {
            sql = "(" + sql + ")";
        }

        return sql;
    }

}
