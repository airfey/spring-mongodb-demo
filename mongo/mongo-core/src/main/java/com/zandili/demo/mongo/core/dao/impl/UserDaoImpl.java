package com.zandili.demo.mongo.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.zandili.demo.mongo.common.dao.impl.DaoImpl;
import com.zandili.demo.mongo.core.dao.UserDao;
import com.zandili.demo.mongo.core.domain.User;
import com.zandili.demo.mongo.core.vo.AgeCount;
import com.zandili.demo.mongo.core.vo.UserVo;

@Repository
public class UserDaoImpl extends DaoImpl<User> implements UserDao {

	private static final long serialVersionUID = 7218051108038638887L;

	@Override
	public List<User> findUserByNameAndAge(String name, int age) {
		Query query = new Query();
		if (null != name && !"".equals(name)) {
			query = query.addCriteria(Criteria.where("name").is(name));// 名称
		}

		query = query.addCriteria(Criteria.where("age").is(age));// 年龄

		return this.getMongoTemplate().find(query, User.class);
	}

	@Override
	public List<User> findUserByDepartment(Long departmentId) {
		Query query = new Query();
		if (null != departmentId) {
			query = query.addCriteria(Criteria.where("department.$id").is(
					departmentId));// 部门
		}

		return this.getMongoTemplate().find(query, User.class);
	}

	@Override
	public List<AgeCount> findUserByGroup() {
		GroupBy groupBy = GroupBy.key("age").initialDocument("{count:0}")
				.reduceFunction("function(doc, prev){prev.count+=1}");

		GroupByResults<User> results = this.getMongoTemplate().group(
				this.getCollectionName(User.class), groupBy, User.class);

		BasicDBList list = (BasicDBList) results.getRawResults().get("retval");

		List<AgeCount> ageCounts = null;
		if (null != list && list.size() > 0) {
			ageCounts = new ArrayList<AgeCount>();
			AgeCount ageCount = new AgeCount();
			for (int i = 0; i < list.size(); i++) {
				BasicDBObject obj = (BasicDBObject) list.get(i);
				ageCount.setAge(Float.valueOf(obj.get("age").toString()));
				ageCount.setCount(Float.valueOf(obj.get("count").toString()));
				System.out.println(obj.get("age"));
				System.out.println(obj.get("count"));
				ageCounts.add(ageCount);
			}
			return ageCounts;
		} else {
			return null;
		}

	}

	/**
	 * 取得实体对应的数据库collection名称
	 * 
	 * @Title: getCollectionName
	 * @param entityClass
	 * @return
	 * @Author: airfey 2013-10-31 下午2:04:21
	 */
	protected String getCollectionName(Class<?> entityClass) {
		// 查看该类上面Document注解
		Document document = entityClass.getAnnotation(Document.class);
		// 如果类上面有Document注解，并且collection属性有值，则数据库collection名称即为该Document注解的collection的值
		if (document != null && document.collection() != null
				&& !"".equals(document.collection().trim())) {
			return document.collection();
		}
		return entityClass.getSimpleName();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> findUserByDistinct() {
		return this.getMongoTemplate()
				.getCollection(this.getCollectionName(User.class))
				.distinct("age");
	}

	@Override
	public List<User> findUserByRegex(String keyword) {
		Query query = new Query();
		if (null != keyword && !"".equals(keyword)) {
			Criteria criteria = new Criteria();
			criteria.orOperator(Criteria.where("name").regex(keyword), Criteria
					.where("email").regex(keyword));// 根据email和name模糊搜索
			query.addCriteria(criteria);
		}
		return this.getMongoTemplate().find(query, User.class);
	}

	@Override
	public UserVo findUsersByPage(Integer age, int beginNum, int pageSize) {
		Query query = new Query();
		UserVo userVo = null;
		if (null != age) {
			query.addCriteria(Criteria.where("age").is(age));
		}
		long count = super.getMongoTemplate().count(query, User.class);
		if (count > 0) {
			query.with(new Sort(Direction.DESC, "_id"));
			query.skip(beginNum).limit(pageSize);
			userVo = new UserVo();
			userVo.setUsers(super.getMongoTemplate().find(query, User.class));
			userVo.setCount((int) count);
		}
		return userVo;
	}

	@Override
	public void insertMutil() {

		List<Long> idss = new ArrayList<Long>();
		for (int i = 0; i < 100; i++) {

			idss.add((long) (i));
		}
		// 使用updateMulti批量更新符合条件的记录，效率较高
		long a = new java.util.Date().getTime();
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(idss));
		Update update = new Update();
		update.set("age", 1);
		getMongoTemplate().updateMulti(query, update, User.class);
		long b = new java.util.Date().getTime();
		System.out.println(b - a);
		// 使用传统的for循环逐条更新符合条件的记录，效率较差
		long c = new java.util.Date().getTime();
		for (Long long1 : idss) {
			Query query1 = new Query();
			query1.addCriteria(Criteria.where("_id").is(long1));
			Update update1 = new Update();
			update1.set("age", 4);
			getMongoTemplate().updateFirst(query1, update1, User.class);
		}
		long d = new java.util.Date().getTime();
		System.out.println(d - c);

	}

	@Override
	public List<User> findUserByOrAnd() {
		Query query = new Query();
		Criteria criteriaOr1 = new Criteria();
		criteriaOr1.orOperator(Criteria.where("age").is(4),
				Criteria.where("emale").is("male"));
		Criteria criteriaOr2 = new Criteria();
		criteriaOr2.orOperator(Criteria.where("name").is("老李"),
				Criteria.where("email").is("airfey@126.com"));
		Criteria criteria3 = new Criteria();
		criteria3.andOperator(criteriaOr1, criteriaOr2);

		query.addCriteria(criteria3);

		return this.getMongoTemplate().find(query, User.class);
	}

}
