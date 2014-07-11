package com.zandili.demo.mongo.common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.zandili.demo.mongo.common.exception.BasicException;


/**
 * mongodb基础Dao
 * 
 * @ClassName: MongoBaseDao
 * @author: airfey 2013-11-6 上午9:08:47
 * @param <T>
 * @version V1.0
 * 
 */
public interface Dao<T>   {
	/**
	 * 根据集合名称生成自增id
	 * 
	 * @Title: getAutoIncreaseID
	 * @param name
	 * @return
	 * @Author: airfey 2013-11-6 上午9:09:09
	 */
	Long getAutoIncreaseID(String name);
	/**
	 * 保存
	 * 
	 * @Title: save
	 * @param entity
	 * @throws BasicException
	 * @Author: airfey 2013-10-29 上午9:36:55
	 */
	void save(T entity) throws BasicException;

	/**
	 * 保存实体并返回主键_id
	 * 
	 * @Title: saveAndGetId
	 * @param entity
	 * @return
	 * @throws BasicException
	 * @Author: airfey 2013-10-29 上午9:37:09
	 */
	Long saveAndGetId(T entity) throws BasicException;

	/**
	 * 新增或保存(有id则修改,无id则新增)
	 * 
	 * @Title: saveOrUpdate
	 * @param entity
	 * @throws BasicException
	 * @Author: airfey 2013-11-8 下午12:10:33
	 */
	void saveOrUpdate(T entity) throws BasicException;

	/**
	 * update 修改实体,将实体里全部字段都往库里存（包含值为Null的字段）
	 * 
	 * @Title: update
	 * @param entity
	 * @throws BasicException
	 * @Author: airfey 2013-11-8 下午12:10:50
	 */
	void update(T entity) throws BasicException;

	/**
	 * 根据id删除数据
	 * 
	 * @Title: deleteById
	 * @param id
	 * @throws BasicException
	 * @Author: airfey 2013-11-8 下午12:11:05
	 */
	void deleteById(Serializable id) throws BasicException;

	/**
	 * 根据多个id删除多条记录
	 * 
	 * @Title: deleteByIds
	 * @param ids
	 * @throws BasicException
	 * @Author: airfey 2013-11-8 下午12:11:29
	 */
	void deleteByIds(Collection<? extends Serializable> ids)
			throws BasicException;

	/**
	 * 根据id查询
	 * 
	 * @Title: getById
	 * @param id
	 * @return
	 * @throws BasicException
	 * @Author: airfey 2013-11-8 下午12:11:47
	 */
	T getById(Serializable id) throws BasicException;

	/**
	 * 根据多个id获取多条记录，其原理就是in查询
	 * 
	 * @Title: getByIds
	 * @param ids
	 * @return
	 * @throws BasicException
	 * @Author: airfey 2013-11-8 下午12:12:05
	 */
	List<T> getByIds(Collection<? extends Serializable> ids)
			throws BasicException;
}
