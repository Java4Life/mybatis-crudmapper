package com.wuxudu.mybatis.crudmapper;

import com.wuxudu.mybatis.crudmapper.domain.CrudMapper;
import com.wuxudu.mybatis.crudmapper.domain.condition.Condition;
import com.wuxudu.mybatis.crudmapper.domain.param.SelectParam;
import org.junit.Test;

public class CrudMapperUsage {

    CrudMapper mapper = null;

    @Test
    public void usage() {
        SelectParam<User> param = new SelectParam<>();
        Condition byName = Condition.by("name").equal("诸葛亮");
        Condition byAddress = Condition.by("address").equal("南阳");
        Condition byNameAndAddress = Condition.and(byName, byAddress);
        param.where(byNameAndAddress);
        mapper.select(param);
    }

    class User {
        private String name;
        private String address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
