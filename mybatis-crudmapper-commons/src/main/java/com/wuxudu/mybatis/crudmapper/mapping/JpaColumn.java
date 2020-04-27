package com.wuxudu.mybatis.crudmapper.mapping;

import com.wuxudu.mybatis.crudmapper.exception.CrudMapperException;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class JpaColumn {

    private static final Class<?>[] SUPPORTED = {String.class, Long.class, Integer.class, BigDecimal.class, LocalDate.class, LocalDateTime.class, Boolean.class};
    private final Class<?> tableClass;
    private final Class<?> fieldType;
    private final String fieldName;
    private final String columnName;
    private final boolean insertable;
    private final boolean updatable;
    private final boolean isId;

    public JpaColumn(Class<?> tableClass, Class<?> fieldType, String fieldName, String columnName, boolean insertable, boolean updatable, boolean isId) {

        if (!ArrayUtils.contains(SUPPORTED, fieldType)) {
            String format = "The type [%s] of the property [%s.%s] is not supported. The supported types are[%s]";
            String message = String.format(format, fieldType.getName(), tableClass.getName(), fieldName, ArrayUtils.toString(SUPPORTED));
            throw new CrudMapperException(message);
        }

        this.tableClass = tableClass;
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.columnName = columnName;
        this.insertable = insertable;
        this.updatable = updatable;
        this.isId = isId;
    }

    public Class<?> getTableClass() {
        return tableClass;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public boolean isId() {
        return isId;
    }
}
