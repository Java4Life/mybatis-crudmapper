package com.wuxudu.mybatis.crudmapper.domain;

import com.wuxudu.mybatis.crudmapper.domain.param.*;

public interface CrudMapper {

    int insert(InsertParam param);

    int delete(DeleteParam param);

    int update(UpdateParam param);

    int select(SelectParam param);

    long count(CountParam param);

}
