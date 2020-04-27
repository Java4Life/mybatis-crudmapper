package com.wuxudu.mybatis.crudmapper.sample;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleApp {

    public static void main(String[] args) {
        SpringApplication.run(SampleApp.class, args);
    }

}
