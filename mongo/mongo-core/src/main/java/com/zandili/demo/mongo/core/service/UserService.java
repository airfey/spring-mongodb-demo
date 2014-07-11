package com.zandili.demo.mongo.core.service;

import com.zandili.demo.mongo.common.page.PageWithData;
import com.zandili.demo.mongo.common.service.Service;
import com.zandili.demo.mongo.core.domain.User;


public interface UserService extends Service<User> {
	public PageWithData<User> queryAdvertByPage(Integer age, int currentPage,
			int pageSize);
}
