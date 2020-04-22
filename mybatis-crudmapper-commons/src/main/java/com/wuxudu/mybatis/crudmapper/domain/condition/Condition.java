package com.wuxudu.mybatis.crudmapper.domain.condition;

public abstract class Condition {

    public static Builder by(String fieldName) {
        return null;
    }

    public static Condition and(Condition... conditions) {
        return null;
    }

    public static Condition or(Condition... conditions) {
        return null;
    }

    public static class Builder {

        public Condition equal(Object value) {
            return null;
        }

    }
}
