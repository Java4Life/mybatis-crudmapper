package com.wuxudu.mybatis.crudmapper.validator;

import java.util.Collection;

public final class Validator {

    private final String argumentName;

    Validator(String argumentName) {
        this.argumentName = argumentName;
    }

    public static Validator argName(String argumentName) {
        return new Validator(argumentName);
    }

    public void notNull(Object argument) {
        if (argument == null) {
            throw new IllegalArgumentException(String.format("Argument %s must not be null", argumentName));
        }
    }

    public void notEmpty(Object[] argument) {
        this.notNull(argument);
        if (argument.length == 0) {
            throw new IllegalArgumentException(String.format("Argument %s must not be empty", argumentName));
        } else {
            for (Object item : argument) {
                if (item == null) {
                    throw new IllegalArgumentException(String.format("Argument %s must not contain null value", argumentName));
                }
            }
        }
    }

    public void notEmpty(Collection<?> argument) {
        this.notNull(argument);
        this.notEmpty(argument.toArray());
    }

    public void notNegative(long number) {
        if (number < 0) {
            throw new IllegalArgumentException(String.format("Argument %s must not be negative number", argumentName));
        }
    }

    public void notNegativeOrZero(long number) {
        this.notNegative(number);
        if (number == 0) {
            throw new IllegalArgumentException(String.format("Argument %s must not be zero", argumentName));
        }
    }
}
