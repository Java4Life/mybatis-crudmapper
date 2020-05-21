package com.wuxudu.mybatis.crudmapper.sample;

import com.wuxudu.mybatis.crudmapper.domain.condition.Condition;
import com.wuxudu.mybatis.crudmapper.domain.param.SelectParam;
import com.wuxudu.mybatis.crudmapper.sample.domain.Employee;
import com.wuxudu.mybatis.crudmapper.sample.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class InsertTest {

    @Autowired
    EmployeeMapper mapper;

    @Test
    void paramIsNull() {
        try {
            this.mapper.insertAll(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void insertOne() {
        LocalDateTime now = LocalDateTime.now();
        Employee toInsert = new Employee();
        toInsert.setEmployeeNo("no1");
        toInsert.setEmployeeName("name1");
        toInsert.setEmployedYears(1);
        toInsert.setEmployeeSalary(new BigDecimal("1"));
        int rows = this.mapper.insertOne(toInsert);
        assertEquals(rows, 1);
        Condition byCreateTime = Condition.by("createTime").greaterThanEqual(now);
        SelectParam query = new SelectParam();
        query.where(byCreateTime);
        this.mapper.select(query).forEach(employee -> System.out.println(employee.toString()));
    }

    @Test
    void insertTwo() {
        LocalDateTime now = LocalDateTime.now();
        Employee employee1 = new Employee();
        employee1.setEmployeeNo("no1");
        employee1.setEmployeeName("name1");
        employee1.setEmployedYears(1);
        employee1.setEmployeeSalary(new BigDecimal("1"));
        Employee employee2 = new Employee();
        employee2.setEmployeeNo("no2");
        employee2.setEmployeeName("name2");
        employee2.setEmployedYears(2);
        employee2.setEmployeeSalary(new BigDecimal("2"));
        int rows = this.mapper.insertAll(Arrays.asList(employee1,employee2));
        assertEquals(rows, 2);
        Condition byCreateTime = Condition.by("createTime").greaterThanEqual(now);
        SelectParam query = new SelectParam();
        query.where(byCreateTime);
        this.mapper.select(query).forEach(employee -> System.out.println(employee.toString()));
    }
}
