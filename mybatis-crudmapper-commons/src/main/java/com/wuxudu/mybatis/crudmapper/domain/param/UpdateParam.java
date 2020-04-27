package com.wuxudu.mybatis.crudmapper.domain.param;

import com.wuxudu.mybatis.crudmapper.validator.Validator;

public final class UpdateParam<T> extends ConditionalParam {

    private T entity;

    public T value() {
        return entity;
    }

    public void value(T entity) {
        Validator.argName("entity").notNull(entity);
        this.entity = entity;
    }
}
