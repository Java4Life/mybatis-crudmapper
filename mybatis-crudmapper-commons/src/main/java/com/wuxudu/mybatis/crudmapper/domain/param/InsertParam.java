package com.wuxudu.mybatis.crudmapper.domain.param;

import com.wuxudu.mybatis.crudmapper.validator.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class InsertParam<T> {

    private final List<T> entities;

    public InsertParam() {
        this.entities = new ArrayList<>();
    }

    public Object[] values() {
        return this.entities.toArray(new Object[0]);
    }

    public void values(T... entities) {
        Validator.argName("entities").notEmpty(entities);
        this.entities.addAll(Arrays.asList(entities));
    }
}
