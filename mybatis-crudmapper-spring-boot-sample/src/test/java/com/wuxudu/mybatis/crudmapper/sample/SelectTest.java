package com.wuxudu.mybatis.crudmapper.sample;

import com.wuxudu.mybatis.crudmapper.domain.condition.Condition;
import com.wuxudu.mybatis.crudmapper.domain.param.SelectParam;
import com.wuxudu.mybatis.crudmapper.domain.sort.Sort;
import com.wuxudu.mybatis.crudmapper.sample.domain.Employee;
import com.wuxudu.mybatis.crudmapper.sample.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SelectTest {

    @Autowired
    EmployeeMapper mapper;

    @Test
    void paramIsNull() {
        try {
            this.mapper.select(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void conditionIsNull() {
        SelectParam<Employee> param = new SelectParam<>();
        List<Employee> employees = this.mapper.select(param);
        employees.forEach(employee -> System.out.println(employee.toString()));
        assertEquals(employees.size(), 9);
    }

    @Test
    void equal() {
        Condition condition = Condition.by("id").equal(1);
        SelectParam<Employee> param = new SelectParam<>();
        param.where(condition);
        List<Employee> employees = this.mapper.select(param);
        employees.forEach(employee -> System.out.println(employee.toString()));
        assertEquals(employees.size(), 1);
    }

    @Test
    void notEqual() {
        Condition condition = Condition.by("id").notEqual(1);
        SelectParam<Employee> param = new SelectParam<>();
        param.where(condition);
        List<Employee> employees = this.mapper.select(param);
        employees.forEach(employee -> System.out.println(employee.toString()));
        assertEquals(employees.size(), 8);
    }

    @Test
    void greaterThan() {
        Condition condition = Condition.by("id").greaterThan(1);
        SelectParam<Employee> param = new SelectParam<>();
        param.where(condition);
        List<Employee> employees = this.mapper.select(param);
        employees.forEach(employee -> System.out.println(employee.toString()));
        assertEquals(employees.size(), 8);
    }

    @Test
    void greaterThanEqual() {
        Condition condition = Condition.by("id").greaterThanEqual(1);
        SelectParam<Employee> param = new SelectParam<>();
        param.where(condition);
        List<Employee> employees = this.mapper.select(param);
        employees.forEach(employee -> System.out.println(employee.toString()));
        assertEquals(employees.size(), 9);
    }

    @Test
    void lessThan() {
        Condition condition = Condition.by("createTime").lessThan(LocalDateTime.now());
        SelectParam<Employee> param = new SelectParam<>();
        param.where(condition);
        List<Employee> employees = this.mapper.select(param);
        employees.forEach(employee -> System.out.println(employee.toString()));
        assertEquals(employees.size(), 9);
    }

    @Test
    void lessThanEqual() {
        Condition condition = Condition.by("id").lessThanEqual(3);
        SelectParam<Employee> param = new SelectParam<>();
        param.where(condition);
        List<Employee> employees = this.mapper.select(param);
        employees.forEach(employee -> System.out.println(employee.toString()));
        assertEquals(employees.size(), 3);
    }

    @Test
    void like() {
        Condition condition = Condition.by("employeeName").like("S%");
        SelectParam<Employee> param = new SelectParam<>();
        param.where(condition);
        List<Employee> employees = this.mapper.select(param);
        employees.forEach(employee -> System.out.println(employee.toString()));
        assertEquals(employees.size(), 2);
    }

    @Test
    void in() {
        Condition condition = Condition.by("employeeName").in("Sarah", "Sophia");
        SelectParam<Employee> param = new SelectParam<>();
        param.where(condition);
        List<Employee> employees = this.mapper.select(param);
        employees.forEach(employee -> System.out.println(employee.toString()));
        assertEquals(employees.size(), 2);
    }

    @Test
    void group() {
        Condition byName = Condition.by("employeeName").in("Sarah", "Sophia");
        Condition bySalaryBtm = Condition.by("employeeSalary").greaterThanEqual(new BigDecimal(10000));
        Condition bySalaryTop = Condition.by("employeeSalary").lessThanEqual(new BigDecimal(80000));
        Condition bySalary = Condition.and(bySalaryBtm, bySalaryTop);
        Condition condition = Condition.or(bySalary, byName);
        SelectParam<Employee> param = new SelectParam<>();
        param.where(condition);
        List<Employee> employees = this.mapper.select(param);
        employees.forEach(employee -> System.out.println(employee.toString()));
        assertEquals(employees.size(), 9);
    }

    @Test
    void limit() {
        SelectParam<Employee> param = new SelectParam<>();
        param.limit(1, 3);
        List<Employee> employees = this.mapper.select(param);
        employees.forEach(employee -> System.out.println(employee.toString()));
        assertEquals(employees.size(), 3);
    }

    @Test
    void sort() {
        SelectParam<Employee> param = new SelectParam<>();
        param.sort(Sort.by("id").asc(), Sort.by("employeeName").desc());
        List<Employee> employees = this.mapper.select(param);
        employees.forEach(employee -> System.out.println(employee.toString()));
        assertEquals(employees.size(), 9);
    }
}
