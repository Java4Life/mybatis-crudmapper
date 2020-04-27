package com.wuxudu.mybatis.crudmapper;

import com.wuxudu.mybatis.crudmapper.domain.CrudMapper;
import com.wuxudu.mybatis.crudmapper.domain.condition.Condition;
import com.wuxudu.mybatis.crudmapper.domain.param.SelectParam;
import com.wuxudu.mybatis.crudmapper.domain.sort.Sort;
import org.junit.Test;

import java.util.List;

public class CrudMapperUsage {

    CrudMapper<User> mapper = null;

    @Test
    public void usage() {
        Condition byAddress = Condition.by("address").equal("南阳");
        Condition bySkill = Condition.by("skill").equal("谋略");
        Condition byName = Condition.by("name").equal("诸葛亮");
        Condition byAddressAndSkill = Condition.and(byAddress, bySkill);
        Condition byNameOrByAddressAndSkill = Condition.or(byName, byAddressAndSkill);
        SelectParam param = new SelectParam();
        param.where(byNameOrByAddressAndSkill);
        param.limit(0, 100);
        param.sort(Sort.by("name").asc(), Sort.by("address").desc());
        List<User> users = mapper.select(param);
        users.forEach(user -> {
            System.out.println(user.getName());
            System.out.println(user.getAddress());
            System.out.println(user.getSkill());
        });
    }

    static class User {
        private final String name;
        private final String address;
        private final String skill;

        public User(String name, String address, String skill) {
            this.name = name;
            this.address = address;
            this.skill = skill;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public String getSkill() {
            return skill;
        }
    }
}
