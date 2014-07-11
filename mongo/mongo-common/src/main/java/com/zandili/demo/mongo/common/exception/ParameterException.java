package com.zandili.demo.mongo.common.exception;

/**
 * 参数异常， 继承自基本异常
 * 
 * @author airfey
 * 
 */
public class ParameterException extends BasicException {

	private static final long serialVersionUID = 2513495667924595876L;

	public ParameterException() {
		super();
	}

	public ParameterException(String msg) {
		super(msg);
	}

	public ParameterException(Throwable nestedThrowable) {
		super(nestedThrowable);
	}

	public ParameterException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
	}
}
