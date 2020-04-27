package com.wuxudu.mybatis.crudmapper.domain.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ConditionGroup extends Condition {

    private final Operator operator;
    private final List<Condition> conditions;

    ConditionGroup(Operator operator) {
        this.operator = operator;
        this.conditions = new ArrayList<>();
    }

    protected void add(Condition... conditions) {
        this.conditions.addAll(Arrays.asList(conditions));
    }

    @Override
    protected void setParameterMap(Map<String, Object> parameterMap) {
        this.conditions.forEach(condition -> condition.setParameterMap(parameterMap));
    }

    public Operator getOperator() {
        return operator;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public enum Operator {

        AND("AND"), OR("OR");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return this.symbol;
        }
    }
}
