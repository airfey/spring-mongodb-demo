package com.zandili.demo.mongo.core.domain;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "demo_location")
public class Location extends AbstractDocument implements Serializable {

	private static final long serialVersionUID = -1054609370441746145L;
	/**
	 * 坐标点的名称（模拟现实中的地标）
	 */
	@Field("name")
	private String name;
	/**
	 * 经纬度坐标点
	 */
	@Field("pos")
	private double[] position;

	public String getName() {
		return name;
	}

	public Location(){
		
	}
	public Location(String name, double a, double b) {
		this.name = name;
		this.position = new double[] { a, b };
	}

	public void setName(String name) {
		this.name = name;
	}

	public double[] getPosition() {
		return position;
	}

	public void setPosition(double[] position) {
		this.position = position;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
