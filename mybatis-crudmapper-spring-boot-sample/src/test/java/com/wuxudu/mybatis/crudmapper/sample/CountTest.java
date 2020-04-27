package com.wuxudu.mybatis.crudmapper.sample;

import com.wuxudu.mybatis.crudmapper.domain.condition.Condition;
import com.wuxudu.mybatis.crudmapper.domain.param.CountParam;
import com.wuxudu.mybatis.crudmapper.sample.domain.Employee;
import com.wuxudu.mybatis.crudmapper.sample.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CountTest {

    @Autowired
    EmployeeMapper mapper;

    @Test
    void paramIsNull() {
        try {
            this.mapper.count(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void conditionIsNull() {
        CountParam<Employee> param = new CountParam<>();
        long count = this.mapper.count(param);
        assertEquals(count, 9);
    }

    @Test
    void greaterThan() {
        Condition condition = Condition.by("id").greaterThan(1);
        CountParam<Employee> param = new CountParam<>();
        param.where(condition);
        long count = this.mapper.count(param);
        assertEquals(count, 8);
    }

}
