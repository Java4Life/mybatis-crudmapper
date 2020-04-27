package com.wuxudu.mybatis.crudmapper.exception;

public final class CrudMapperException extends RuntimeException {

    public CrudMapperException(String message) {
        super(message);
    }

    public CrudMapperException(Throwable cause) {
        super(cause);
    }
}
