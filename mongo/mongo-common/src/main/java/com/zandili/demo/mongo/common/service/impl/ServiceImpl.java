package com.zandili.demo.mongo.common.service.impl;

import static java.util.Collections.EMPTY_LIST;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.zandili.demo.mongo.common.dao.Dao;
import com.zandili.demo.mongo.common.exception.BasicException;
import com.zandili.demo.mongo.common.service.Service;
import com.zandili.demo.mongo.common.util.CommonUtils;


@SuppressWarnings({ "rawtypes" })
public abstract class ServiceImpl<T> implements Service<T> {
	/**
	 * 注入dao
	 * 
	 * @Title: getDao
	 * @return
	 * @Author: airfey 2013-11-8 下午1:33:29
	 */
	protected abstract Dao getDao();

	@Override
	public Long getAutoIncreaseID(String name) {
		return getDao().getAutoIncreaseID(name);
	}

	@Override
	public Boolean save(final T entity) throws BasicException {

		return CommonUtils.isEmpty(entity) ? Boolean.FALSE : ServiceTemplate
				.doWork(new ServiceCallBack() {
					@SuppressWarnings("unchecked")
					@Override
					public void execute() {
						getDao().save(entity);
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long saveAndGetId(T entity) throws BasicException {
		return CommonUtils.isEmpty(entity) ? -1 : this.getDao().saveAndGetId(
				entity);
	}

	@Override
	public Boolean saveOrUpdate(final T entity) throws BasicException {
		return CommonUtils.isEmpty(entity) ? Boolean.FALSE : ServiceTemplate
				.doWork(new ServiceCallBack() {
					@SuppressWarnings("unchecked")
					@Override
					public void execute() {
						getDao().saveOrUpdate(entity);
					}
				});
	}

	@Override
	public Boolean update(final T entity) throws BasicException {
		return entity == null ? Boolean.FALSE : ServiceTemplate
				.doWork(new ServiceCallBack() {
					@SuppressWarnings("unchecked")
					@Override
					public void execute() {
						getDao().update(entity);
					}
				});
	}

	@Override
	public Boolean deleteById(final Serializable id) throws BasicException {
		return id == null ? Boolean.FALSE : ServiceTemplate
				.doWork(new ServiceCallBack() {
					@Override
					public void execute() {
						getDao().deleteById(id);
					}
				});
	}

	@Override
	public Boolean deleteByIds(final Collection<? extends Serializable> ids) {
		return CommonUtils.isEmpty(ids) ? Boolean.FALSE : ServiceTemplate
				.doWork(new ServiceCallBack() {
					@SuppressWarnings("unchecked")
					@Override
					public void execute() {
						getDao().deleteByIds(ids);
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getById(Serializable id) throws BasicException {
		return id == null ? null : (T) this.getDao().getById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getByIds(Collection<? extends Serializable> ids)
			throws BasicException {
		return CommonUtils.isEmpty(ids) ? EMPTY_LIST : this.getDao().getByIds(
				ids);
	}
}
