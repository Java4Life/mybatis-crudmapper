package com.wuxudu.mybatis.crudmapper.sample;

import com.wuxudu.mybatis.crudmapper.domain.condition.Condition;
import com.wuxudu.mybatis.crudmapper.domain.param.DeleteParam;
import com.wuxudu.mybatis.crudmapper.sample.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DeleteTest {

    @Autowired
    EmployeeMapper mapper;

    @Test
    void paramIsNull() {
        try {
            this.mapper.delete(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void conditionIsNull() {
        DeleteParam param = new DeleteParam();
        int rows = this.mapper.delete(param);
        assertEquals(rows, 9);
    }

    @Test
    void equal() {
        Condition condition = Condition.by("id").equal(1);
        DeleteParam param = new DeleteParam();
        param.where(condition);
        int rows = this.mapper.delete(param);
        assertEquals(rows, 1);
    }

}
