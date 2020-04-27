package com.wuxudu.mybatis.crudmapper.sample.mapper;

import com.wuxudu.mybatis.crudmapper.domain.CrudMapper;
import com.wuxudu.mybatis.crudmapper.sample.domain.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends CrudMapper<Employee> {

}
