package com.zandili.demo.mongo.core.vo;

import java.io.Serializable;

public class AgeCount implements Serializable {

	private static final long serialVersionUID = -7057496581105470454L;

	private Float age;
	private Float count;

	public Float getAge() {
		return age;
	}

	public void setAge(Float age) {
		this.age = age;
	}

	public Float getCount() {
		return count;
	}

	public void setCount(Float count) {
		this.count = count;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
