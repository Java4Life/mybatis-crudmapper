package com.wuxudu.mybatis.crudmapper.domain.condition;

import java.util.Map;

public final class ConditionWrapper {

    private final Condition condition;

    public ConditionWrapper(Condition condition, Map<String, Object> parameterMap) {
        this.condition = condition;
        if (this.condition != null) {
            this.condition.setParameterMap(parameterMap);
        }
    }

    public Condition getCondition() {
        return condition;
    }
}
