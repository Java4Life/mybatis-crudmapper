package com.wuxudu.mybatis.crudmapper.mapping;

import com.wuxudu.mybatis.crudmapper.exception.CrudMapperException;

import java.util.HashMap;
import java.util.Map;

public class JpaTableManager {

    private static JpaTableManager instance = new JpaTableManager();

    private final Map<String, JpaTable> annotatedJpaTables;
    private final Map<String, JpaTable> mappedJpaTables;

    JpaTableManager() {
        this.annotatedJpaTables = new HashMap<>();
        this.mappedJpaTables = new HashMap<>();
    }

    public static JpaTableManager getInstance() {
        return instance;
    }

    public void register(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            JpaTable jpaTable = JpaParser.parse(clazz);
            this.annotatedJpaTables.put(className, jpaTable);
        } catch (Exception e) {
            throw new CrudMapperException(e);
        }
    }

    public void mapping(Class<?> mapperClass, JpaTable jpaTable) {
        this.mappedJpaTables.put(mapperClass.getName(), jpaTable);
    }

    public Map<String, JpaTable> getAnnotatedJpaTables() {
        return annotatedJpaTables;
    }

    public Map<String, JpaTable> getMappedJpaTables() {
        return mappedJpaTables;
    }
}
