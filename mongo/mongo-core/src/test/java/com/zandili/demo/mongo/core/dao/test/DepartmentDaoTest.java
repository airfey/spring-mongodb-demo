package com.zandili.demo.mongo.core.dao.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zandili.demo.mongo.core.dao.DepartmentDao;
import com.zandili.demo.mongo.core.domain.Department;
import com.zandili.demo.mongo.core.test.BaseTest;


public class DepartmentDaoTest extends BaseTest {

	@Autowired
	private DepartmentDao departmentDao;

	@Test
	public void testInsertDepartment() {
		Department department = new Department();
		department.setDepartmentName("研发部3");

		departmentDao.saveAndGetId(department);
	}
}
