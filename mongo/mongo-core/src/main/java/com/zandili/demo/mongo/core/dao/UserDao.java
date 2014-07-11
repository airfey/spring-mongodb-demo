package com.zandili.demo.mongo.core.dao;

import java.util.List;

import com.zandili.demo.mongo.common.dao.Dao;
import com.zandili.demo.mongo.core.domain.User;
import com.zandili.demo.mongo.core.vo.AgeCount;
import com.zandili.demo.mongo.core.vo.UserVo;


public interface UserDao extends Dao<User> {
	/**
	 * 根据姓名和年龄查询(演示and查询)
	 * 
	 * @Title: findUserByNameAndAge
	 * @param name
	 *            可以为空，为空表示不过滤该条件
	 * @param age
	 *            年龄
	 * @return
	 * @Author: airfey 2013-10-29 下午6:08:57
	 */
	public List<User> findUserByNameAndAge(String name, int age);

	/**
	 * 根据部门id查询用户 (演示用子文档的id进行查询)
	 * 
	 * @Title: findUserByDepartment
	 * @param departmentId
	 * @return
	 * @Author: airfey 2013-10-29 下午6:10:59
	 */
	public List<User> findUserByDepartment(Long departmentId);

	/**
	 * 按照年龄age分组(演示group by)
	 * 
	 * @Title: findUserByGroup
	 * @return
	 * @Author: airfey 2013-11-4 下午3:08:44
	 */
	public List<AgeCount> findUserByGroup();

	/**
	 * 按照年龄age排重 ，返回不重复的每个年龄值(演示Distinct)
	 * 
	 * @Title: findUserByDistinct
	 * @return
	 * @Author: airfey 2013-11-4 下午3:09:45
	 */
	public List<Integer> findUserByDistinct();

	/**
	 * 演示根据用户名称模糊查询（正则方式） 模糊查询用户姓名或者email，同时也简单演示一下or查询的用法
	 * 
	 * @Title: findUserByRegex
	 * @param keyword
	 * @return
	 * @Author: airfey 2013-11-11 下午2:58:51
	 */
	public List<User> findUserByRegex(String keyword);

	/**
	 * 演示查询分页
	 * 
	 * @Title: findUsersByPage
	 * @param age
	 * @param beginNum
	 * @param pageSize
	 * @return
	 * @Author: airfey 2013-11-12 下午1:55:37
	 */
	public UserVo findUsersByPage(Integer age, int beginNum, int pageSize);

	public void insertMutil();

	/**
	 * 演示类似于(a=1 or b=2) and (c=3 or d=4)此类的查询 { "$and" : [ { "$or" : [ { "a" :
	 * 1} , { "b" : 2}]} , { "$or" : [ { "c" : 3} , { "d" : 4}]}]}
	 * 
	 * @Title: findUserByOrAnd
	 * @return
	 * @Author: airfey 2013-11-12 下午1:55:00
	 */
	public List<User> findUserByOrAnd();
}
