package com.wuxudu.mybatis.crudmapper.mapping;

import com.wuxudu.mybatis.crudmapper.exception.CrudMapperException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;

public final class JpaParser {

    public static JpaTable parse(Class<?> clazz) {

        if (clazz == null) {
            throw new CrudMapperException("Class to be parsed must not be null");
        }

        String className = clazz.getName();

        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            throw new CrudMapperException("Annotation @Table not found in class " + className);
        }

        String tableName = table.name();
        if (StringUtils.isBlank(tableName)) {
            throw new CrudMapperException("Annotation @Table not named in class " + className);
        }

        JpaTable jpaTable = new JpaTable(clazz, tableName);

        Field[] fields = FieldUtils.getAllFields(clazz);

        for (Field field : fields) {

            String fieldName = field.getName();
            Class<?> fieldType = field.getType();

            Id id = field.getDeclaredAnnotation(Id.class);
            Column column = field.getDeclaredAnnotation(Column.class);

            if (column != null) {

                String columnName = column.name();
                boolean insertable = column.insertable();
                boolean updatable = column.updatable();
                boolean isId = id != null;

                if (StringUtils.isBlank(columnName)) {
                    throw new CrudMapperException("Annotation @Column not named for field " + className + "." + fieldName);
                }

                JpaColumn jpaColumn = new JpaColumn(clazz, fieldType, fieldName, columnName, insertable, updatable, isId);
                jpaTable.addColumn(jpaColumn);
            }
        }

        int columnCount = jpaTable.getColumns().length;
        if (columnCount == 0) {
            throw new CrudMapperException("Annotation @Column not found in class " + className);
        }

        return jpaTable;
    }
}
