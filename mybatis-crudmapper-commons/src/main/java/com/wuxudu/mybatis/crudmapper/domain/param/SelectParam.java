package com.wuxudu.mybatis.crudmapper.domain.param;

import com.wuxudu.mybatis.crudmapper.domain.sort.Sort;
import com.wuxudu.mybatis.crudmapper.validator.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SelectParam extends ConditionalParam {

    private final List<Sort> sorts = new ArrayList<>();
    private int offset = 0;
    private int limit = 1000;

    public void limit(int offset, int limit) {
        Validator.argName("offset").notNegative(offset);
        Validator.argName("limit").notNegativeOrZero(limit);
        this.offset = offset;
        this.limit = limit;
    }

    public void sort(Sort... sorts) {
        Validator.argName("sorts").notEmpty(sorts);
        this.sorts.addAll(Arrays.asList(sorts));
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public Sort[] getSorts() {
        return sorts.toArray(new Sort[0]);
    }
}
