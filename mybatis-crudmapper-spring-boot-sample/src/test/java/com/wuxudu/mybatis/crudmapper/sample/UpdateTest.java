package com.wuxudu.mybatis.crudmapper.sample;

import com.wuxudu.mybatis.crudmapper.domain.condition.Condition;
import com.wuxudu.mybatis.crudmapper.domain.param.DeleteParam;
import com.wuxudu.mybatis.crudmapper.domain.param.SelectParam;
import com.wuxudu.mybatis.crudmapper.domain.param.UpdateParam;
import com.wuxudu.mybatis.crudmapper.sample.domain.Employee;
import com.wuxudu.mybatis.crudmapper.sample.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UpdateTest {

    @Autowired
    EmployeeMapper mapper;

    @Test
    void paramIsNull() {
        try {
            this.mapper.update(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void valueIsNull() {
        try {
            UpdateParam<Employee> param = new UpdateParam<>();
            this.mapper.update(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void conditionIsNull() {
        Employee toUpdate = new Employee();
        toUpdate.setEmployeeSalary(new BigDecimal(99999));
        toUpdate.setEmployedYears(1);
        UpdateParam<Employee> param = new UpdateParam<>();
        param.value(toUpdate);
        int rows = this.mapper.update(param);
        assertEquals(rows, 9);
        SelectParam<Employee> query = new SelectParam<>();
        this.mapper.select(query).forEach(employee -> System.out.println(employee.toString()));
    }

    @Test
    void equal() {
        Employee toUpdate = new Employee();
        toUpdate.setEmployeeSalary(new BigDecimal(99999));
        toUpdate.setEmployedYears(1);
        Condition condition = Condition.by("id").equal(1);
        UpdateParam<Employee> param = new UpdateParam<>();
        param.value(toUpdate);
        param.where(condition);
        int rows = this.mapper.update(param);
        assertEquals(rows, 1);
        SelectParam<Employee> query = new SelectParam<>();
        this.mapper.select(query).forEach(employee -> System.out.println(employee.toString()));
    }

}
