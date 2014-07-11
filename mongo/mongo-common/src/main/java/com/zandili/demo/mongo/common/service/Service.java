package com.zandili.demo.mongo.common.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.zandili.demo.mongo.common.exception.BasicException;


public interface Service<T> {

	@Deprecated
	Long getAutoIncreaseID(String name);

	/**
	 * 新增数据(保存数据)
	 * 
	 * @Title: save
	 * @param entity
	 * @return
	 * @throws BasicException
	 * @Author: airfey 2013-11-11 上午10:28:34
	 */
	Boolean save(T entity) throws BasicException;

	/**
	 * 保存数据并返回主键id
	 * 
	 * @Title: saveAndGetId
	 * @param entity
	 * @return
	 * @throws BasicException
	 * @Author: airfey 2013-11-11 上午10:28:52
	 */
	Long saveAndGetId(T entity) throws BasicException;

	/**
	 * 新增或保存(有id则修改,无id则新增)
	 * 
	 * @Title: saveOrUpdate
	 * @param entity
	 * @return
	 * @throws BasicException
	 * @Author: airfey 2013-11-11 上午10:29:30
	 */
	Boolean saveOrUpdate(T entity) throws BasicException;

	/**
	 * update 修改实体,将实体里全部字段都往库里存（包含值为Null的字段）
	 * 
	 * @Title: update
	 * @param entity
	 * @return
	 * @throws BasicException
	 * @Author: airfey 2013-11-11 上午10:29:53
	 */
	Boolean update(T entity) throws BasicException;

	/**
	 * deleteById
	 * 
	 * @Title: deleteById
	 * @param id
	 * @return
	 * @throws BasicException
	 * @Author: airfey 2013-11-11 上午10:30:04
	 */
	Boolean deleteById(Serializable id) throws BasicException;

	/**
	 * deleteByIds
	 * 
	 * @Title: deleteByIds
	 * @param ids
	 * @return
	 * @throws BasicException
	 * @Author: airfey 2013-11-11 上午10:30:15
	 */
	Boolean deleteByIds(Collection<? extends Serializable> ids)
			throws BasicException;

	/**
	 * getById
	 * 
	 * @Title: getById
	 * @param id
	 * @return
	 * @throws BasicException
	 * @Author: airfey 2013-11-11 上午10:30:25
	 */
	T getById(Serializable id) throws BasicException;

	/**
	 * getByIds
	 * 
	 * @Title: getByIds
	 * @param ids
	 * @return
	 * @throws BasicException
	 * @Author: airfey 2013-11-11 上午10:30:34
	 */
	List<T> getByIds(Collection<? extends Serializable> ids)
			throws BasicException;
}