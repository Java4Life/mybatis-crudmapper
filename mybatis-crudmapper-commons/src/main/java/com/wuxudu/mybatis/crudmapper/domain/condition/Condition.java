package com.wuxudu.mybatis.crudmapper.domain.condition;

import com.wuxudu.mybatis.crudmapper.validator.Validator;

import java.util.Arrays;
import java.util.Map;

public abstract class Condition {

    public static Builder by(String fieldName) {
        Validator.argName("fieldName").notNull(fieldName);
        return new Condition.Builder(fieldName);
    }

    public static Condition and(Condition... conditions) {
        Validator.argName("conditions").notEmpty(conditions);
        ConditionGroup group = new ConditionGroup(ConditionGroup.Operator.AND);
        group.add(conditions);
        return group;
    }

    public static Condition or(Condition... conditions) {
        Validator.argName("conditions").notEmpty(conditions);
        ConditionGroup group = new ConditionGroup(ConditionGroup.Operator.OR);
        group.add(conditions);
        return group;
    }

    protected abstract void setParameterMap(Map<String, Object> parameterMap);

    public static class Builder {

        private String fieldName;

        protected Builder(String fieldName) {
            this.fieldName = fieldName;
        }

        public Condition equal(Object value) {
            Validator.argName("value").notNull(value);
            return new ConditionElement(fieldName, ConditionElement.Operator.Equal, value);
        }

        public Condition notEqual(Object value) {
            Validator.argName("value").notNull(value);
            return new ConditionElement(fieldName, ConditionElement.Operator.NotEqual, value);
        }

        public Condition greaterThan(Object value) {
            Validator.argName("value").notNull(value);
            return new ConditionElement(fieldName, ConditionElement.Operator.GreaterThan, value);
        }

        public Condition greaterThanEqual(Object value) {
            Validator.argName("value").notNull(value);
            return new ConditionElement(fieldName, ConditionElement.Operator.GreaterThanEqual, value);
        }

        public Condition lessThan(Object value) {
            Validator.argName("value").notNull(value);
            return new ConditionElement(fieldName, ConditionElement.Operator.LessThan, value);
        }

        public Condition lessThanEqual(Object value) {
            Validator.argName("value").notNull(value);
            return new ConditionElement(fieldName, ConditionElement.Operator.LessThanEqual, value);
        }

        public Condition like(String value) {
            Validator.argName("value").notNull(value);
            return new ConditionElement(fieldName, ConditionElement.Operator.Like, value);
        }

        public Condition in(Object... values) {
            Validator.argName("value").notEmpty(values);
            return new ConditionElement(fieldName, ConditionElement.Operator.In, Arrays.asList(values));
        }
    }
}
