package com.zandili.demo.mongo.core.vo;

import java.io.Serializable;
import java.util.List;

import com.zandili.demo.mongo.core.domain.User;


public class UserVo  implements Serializable{

	 
	private static final long serialVersionUID = -1847593632013302470L;

	private List<User> users;
	
	private int count;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
