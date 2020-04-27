package com.wuxudu.mybatis.crudmapper.domain.param;

import com.wuxudu.mybatis.crudmapper.domain.condition.Condition;
import com.wuxudu.mybatis.crudmapper.domain.condition.ConditionWrapper;
import com.wuxudu.mybatis.crudmapper.validator.Validator;

import java.util.HashMap;
import java.util.Map;

public abstract class ConditionalParam {

    private final Map<String, Object> conditionValues;
    private ConditionWrapper conditionWrapper;

    ConditionalParam() {
        this.conditionValues = new HashMap<>();
    }

    public void where(final Condition condition) {
        Validator.argName("condition").notNull(condition);
        this.conditionValues.clear();
        this.conditionWrapper = new ConditionWrapper(condition, conditionValues);
    }

    public Condition getCondition() {
        if (this.conditionWrapper == null) {
            return null;
        } else {
            return this.conditionWrapper.getCondition();
        }
    }
}
