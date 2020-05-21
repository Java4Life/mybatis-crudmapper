package com.wuxudu.mybatis.crudmapper.domain;

import com.wuxudu.mybatis.crudmapper.domain.param.CountParam;
import com.wuxudu.mybatis.crudmapper.domain.param.DeleteParam;
import com.wuxudu.mybatis.crudmapper.domain.param.SelectParam;
import com.wuxudu.mybatis.crudmapper.domain.param.UpdateParam;
import com.wuxudu.mybatis.crudmapper.provider.*;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.List;

public interface CrudMapper<T> {

    @Options(useGeneratedKeys = true)
    @InsertProvider(type = InsertOneSqlProvider.class, method = "sql")
    int insertOne(T entity);

    @Options(useGeneratedKeys = true)
    @InsertProvider(type = InsertAllSqlProvider.class, method = "sql")
    int insertAll(Collection<T> entities);

    @DeleteProvider(type = DeleteSqlProvider.class, method = "sql")
    int delete(DeleteParam param);

    @UpdateProvider(type = UpdateSqlProvider.class, method = "sql")
    int update(UpdateParam<T> param);

    @SelectProvider(type = SelectSqlProvider.class, method = "sql")
    List<T> select(SelectParam param);

    @SelectProvider(type = CountSqlProvider.class, method = "sql")
    long count(CountParam param);

}
