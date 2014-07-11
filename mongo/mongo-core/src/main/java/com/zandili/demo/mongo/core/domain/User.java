package com.zandili.demo.mongo.core.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 用户类
 * 
 * @ClassName: User
 * @author: airfey 2013-10-28 下午2:51:25
 * @version V1.0
 * 
 */
// 指定文档集合名称（相当于表名）
@Document(collection = "demo_user")
public class User extends AbstractDocument implements Serializable {

	private static final long serialVersionUID = -1488417779512310678L;

	@Field("name")
	private String name;
	@Field("age")
	@Indexed
	// (name="_age",unique = true)
	private int age;
	@Field("genda")
	private String genda;
	@Field("email")
	private String email;
	@DBRef
	@Field("department")
	private Department department;
	@Transient
	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGenda() {
		return genda;
	}

	public void setGenda(String genda) {
		this.genda = genda;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
