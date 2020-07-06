package com.wuxudu.mybatis.crudmapper.exception;

public final class CrudMapperException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CrudMapperException(String message) {
		super(message);
	}

	public CrudMapperException(Throwable cause) {
		super(cause);
	}
}
