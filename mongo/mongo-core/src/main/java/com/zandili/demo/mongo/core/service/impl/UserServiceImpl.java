package com.zandili.demo.mongo.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zandili.demo.mongo.common.dao.Dao;
import com.zandili.demo.mongo.common.exception.ApplicationException;
import com.zandili.demo.mongo.common.exception.ParameterException;
import com.zandili.demo.mongo.common.page.PageWithData;
import com.zandili.demo.mongo.common.service.impl.ServiceImpl;
import com.zandili.demo.mongo.core.dao.UserDao;
import com.zandili.demo.mongo.core.domain.User;
import com.zandili.demo.mongo.core.service.UserService;
import com.zandili.demo.mongo.core.vo.UserVo;

@Service
public class UserServiceImpl extends ServiceImpl<User> implements UserService {
	@Autowired
	private UserDao userDao;

	public PageWithData<User> queryAdvertByPage(Integer age, int currentPage,
			int pageSize) {
		if (pageSize > 100 || pageSize < 1) {
			throw new ParameterException("parameter pageSize error...");
		}
		PageWithData<User> pwd = null;
		try {
			// int begin = 0;
			// begin = (currentPage - 1) * pageSize;// 计算起始记录
			pwd = new PageWithData<User>(currentPage, pageSize);
			int beginNum = pwd.getFirstResult();// (currentPage - 1) * pageSize;

			// 计算起始记录
			UserVo userVo = userDao.findUsersByPage(age, beginNum, pageSize);
			if (null != userVo) {
				int count = userVo.getCount();
				pwd.setTotalResults(count);

				List<User> users = userVo.getUsers();
				pwd.setData(users);
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return pwd;
	}

	@Override
	protected Dao getDao() {

		return userDao;
	}
}
