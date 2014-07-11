package com.zandili.demo.mongo.core.dao.test;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zandili.demo.mongo.core.dao.UserDao;
import com.zandili.demo.mongo.core.domain.Department;
import com.zandili.demo.mongo.core.domain.User;
import com.zandili.demo.mongo.core.test.BaseTest;
import com.zandili.demo.mongo.core.vo.AgeCount;


public class UserDaoTest extends BaseTest {
	@Autowired
	private UserDao userDao;

	/**
	 * 添加用户
	 * 
	 * @Title: testInsertUser
	 * @Author: airfey 2013-10-29 上午8:59:40
	 */
	@Test
	public void testInsertUser() {
		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.setName("萝莉" + i);
			user.setAge(i);
			user.setEmail("airfey" + i + "@126.com");
			user.setGenda("male");
			Department department = new Department();
			department.setDepartmentName("研发部");
			department.setId(1L);
			user.setDepartment(department);
			userDao.saveAndGetId(user);
			System.out.println("提交到" + i);
		}
	}

	/**
	 * 修改用户的部门信息
	 * 
	 * @Title: testGetUser
	 * @Author: airfey 2013-10-29 上午9:05:51
	 */
	@Test
	public void testGetUser() {
		User user = userDao.getById(2L);
		Department department = new Department();
		department.setId(3L);
		user.setDepartment(department);
		userDao.update(user);
	}

	/**
	 * where name = "stephen" and age = 35
	 * 
	 * @Title: testFind1
	 * @Author: airfey 2013-10-29 上午9:07:17
	 */
	@Test
	public void testFindNameAndAge() {
		List<User> users = userDao.findUserByNameAndAge(null, 25);
		System.out.println(users.size());
	}

	@Test
	public void testFindUserByDepartment() {
		List<User> users = userDao.findUserByDepartment(1L);
		System.out.println(users.size());
	}

	@Test
	public void testFindUserByGroup() {
		List<AgeCount> users = userDao.findUserByGroup();
		System.out.println(users.size());
	}

	@Test
	public void testFindUserByDistinct() {
		List<Integer> users = userDao.findUserByDistinct();
		System.out.println(users.size());
	}

	@Test
	public void testFindUserByRegex() {
		String key = "{1";
		List<User> users = userDao.findUserByRegex("\\" + key);
		System.out.println(users.size());
	}
    /**
     * 测试单条循环修改与mutil多条修改的效率 
     *
     * @Title: testMutil   
     * @Author: airfey 2013-11-8 上午11:30:54
     */
	@Test
	public void testMutil() {
		//userDao.insertMutil();
	}
	@Test
	public void testFindUserByOrAnd() {
		List<User> users = userDao.findUserByOrAnd();
		System.out.println(users.size());
	}
	
	
}
