package com.wuxudu.mybatis.crudmapper.mapping;

import com.wuxudu.mybatis.crudmapper.exception.CrudMapperException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JpaTable {

    private final Class<?> tableClass;
    private final String tableName;
    private final List<JpaColumn> columnList;
    private final Map<String, JpaColumn> columnMap;
    private JpaColumn idColumn;

    public JpaTable(Class<?> tableClass, String tableName) {
        this.tableClass = tableClass;
        this.tableName = tableName;
        this.columnList = new ArrayList<>();
        this.columnMap = new HashMap<>();
    }

    public void addColumn(JpaColumn column) {
        String fieldName = column.getFieldName();
        if (this.columnMap.containsKey(fieldName)) {
            String format = "Column map already exists for %s.%s";
            String message = String.format(format, this.tableName, fieldName);
            throw new CrudMapperException(message);
        } else {
            if (column.isId()) {
                if (this.idColumn == null) {
                    this.idColumn = column;
                    this.columnList.add(0, column);
                } else {
                    throw new CrudMapperException("Multiple ID columns defined");
                }
            } else {
                this.columnList.add(column);
            }
            this.columnMap.put(fieldName, column);
        }
    }

    public JpaColumn[] getColumns() {
        return this.columnList.toArray(new JpaColumn[0]);
    }

    public Class<?> getTableClass() {
        return tableClass;
    }

    public JpaColumn getColumn(String fieldName) {
        JpaColumn jpaColumn = this.columnMap.get(fieldName);
        if (jpaColumn == null) {
            String format = "Column map not found for field %s.%s";
            String message = String.format(format, this.tableName, fieldName);
            throw new CrudMapperException(message);
        }
        return jpaColumn;
    }

    public String getTableName() {
        return tableName;
    }

    public JpaColumn getIdColumn() {
        return idColumn;
    }
}
