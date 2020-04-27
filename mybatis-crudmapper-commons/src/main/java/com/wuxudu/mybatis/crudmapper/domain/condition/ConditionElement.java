package com.wuxudu.mybatis.crudmapper.domain.condition;

import java.util.Map;

public final class ConditionElement extends Condition {

    private final String fieldName;
    private final Operator operator;
    private final Object parameter;

    private String parameterKey;

    ConditionElement(String fieldName, Operator operator, Object parameter) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.parameter = parameter;
    }

    @Override
    protected void setParameterMap(Map<String, Object> parameterMap) {
        int size = parameterMap.size();
        this.parameterKey = "value" + (size + 1);
        parameterMap.put(this.parameterKey, this.parameter);
    }

    public String getFieldName() {
        return fieldName;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getParameterKey() {
        return parameterKey;
    }

    public Object getParameter() {
        return parameter;
    }

    public enum Operator {

        Equal("="),
        NotEqual("<>"),
        GreaterThan(">"),
        GreaterThanEqual(">="),
        LessThan("<"),
        LessThanEqual("<="),
        Like("LIKE"),
        In("IN");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return this.symbol;
        }
    }

}
