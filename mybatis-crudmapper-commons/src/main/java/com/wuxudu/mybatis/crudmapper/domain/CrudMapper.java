package com.wuxudu.mybatis.crudmapper.domain;

import com.wuxudu.mybatis.crudmapper.domain.param.*;
import com.wuxudu.mybatis.crudmapper.provider.*;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

public interface CrudMapper<T> {

    @InsertProvider(type = InsertSqlProvider.class, method = "sql")
    int insert(InsertParam<T> param);

    @DeleteProvider(type = DeleteSqlProvider.class, method = "sql")
    int delete(DeleteParam<T> param);

    @UpdateProvider(type = UpdateSqlProvider.class, method = "sql")
    int update(UpdateParam<T> param);

    @SelectProvider(type = SelectSqlProvider.class, method = "sql")
    List<T> select(SelectParam<T> param);

    @SelectProvider(type = CountSqlProvider.class, method = "sql")
    long count(CountParam<T> param);

}
