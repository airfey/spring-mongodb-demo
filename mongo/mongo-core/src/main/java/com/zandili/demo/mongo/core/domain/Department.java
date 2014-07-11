package com.zandili.demo.mongo.core.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 部门类
 * 
 * @ClassName: Department
 * @author: airfey 2013-10-28 下午2:51:10
 * @version V1.0
 * 
 */
@Document(collection = "demo_department")
public class Department implements Serializable {

	private static final long serialVersionUID = -758636671508945456L;
	@Id
	@Field("id")
	private Long id;
	@Field("dn")
	private String departmentName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
