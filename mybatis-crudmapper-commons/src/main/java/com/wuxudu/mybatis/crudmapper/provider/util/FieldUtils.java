package com.wuxudu.mybatis.crudmapper.provider.util;

import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class FieldUtils {

    public static boolean hasValue(Object object, String fieldName) {
        try {
            PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(object, fieldName);

            if (propertyDescriptor == null) {
                throw new RuntimeException("Getter not found for " + object.getClass().getName() + "." + fieldName);
            }

            Method readMethod = propertyDescriptor.getReadMethod();
            Object fieldValue = readMethod.invoke(object);

            return fieldValue != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
