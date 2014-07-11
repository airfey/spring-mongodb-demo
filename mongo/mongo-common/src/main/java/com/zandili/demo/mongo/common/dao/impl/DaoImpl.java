package com.zandili.demo.mongo.common.dao.impl;

import static com.zandili.demo.mongo.common.util.CommonUtils.isEmpty;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.zandili.demo.mongo.common.dao.Dao;
import com.zandili.demo.mongo.common.dao.annotation.Column;
import com.zandili.demo.mongo.common.exception.ApplicationException;
import com.zandili.demo.mongo.common.exception.BasicException;
import com.zandili.demo.mongo.common.exception.ParameterException;
import com.zandili.demo.mongo.common.util.AssertUtils;
import com.zandili.demo.mongo.common.util.ReflectUitls;

/**
 * 这个 Mongo 便 类 了 常 方 封装 用 用 的 使 各 等 种 等 操 查 作 改 ！ 删 如 增
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class DaoImpl<T> implements Dao<T>, Serializable {
	private static final long serialVersionUID = 6975328597396206191L;

	@Autowired
	private MongoMapperContext mongoMapperContext;

	@Autowired
	private MongoTemplate mongoTemplate;

	private MongoMapper currentMongoMapper;

	private Class<?> entityClass = null;

	private static final Logger logger = LoggerFactory.getLogger(DaoImpl.class);

	@SuppressWarnings("unused")
	@PostConstruct
	private void initialize() {
		this.currentMongoMapper = mongoMapperContext.get(getEntityClass());
		if (this.currentMongoMapper != null)
			return;
		MongoMapper mapper = new MongoMapper();
		mapper.setClassName(getEntityClass().getName());
		Document document = (Document) getEntityClass().getAnnotation(
				Document.class);
		mapper.setDocumentName(document != null ? document.collection()
				: getEntityClass().getSimpleName());

		doInitMapperContext(getEntityClass(), mapper);
		this.setCurrentMongoMapper(mapper);
		this.mongoMapperContext.put(getEntityClass(), mapper);
	}

	private MongoMapper doInitMapperContext(Class clazz, MongoMapper mapper) {
		AssertUtils.notNull(clazz);
		class2MongoMapper(clazz, mapper);
		return clazz.getSuperclass().equals(Object.class) ? mapper
				: doInitMapperContext(clazz.getSuperclass(), mapper);

	}

	private MongoMapper class2MongoMapper(Class clazz, MongoMapper mapper) {
		PropertyDescriptor[] pds = new PropertyDescriptor[0];
		try {
			pds = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		for (PropertyDescriptor propertyDescriptor : pds) {
			if (Class.class == propertyDescriptor.getPropertyType()) {
				continue;
			}
			// 反射出私有属性
			java.lang.reflect.Field propertyField = null;
			try {
				propertyField = clazz.getDeclaredField(propertyDescriptor
						.getName());
			} catch (NoSuchFieldException e) {
				continue;
			}
			propertyField.setAccessible(true);
			// 判断该字段是否参加入库
			if (propertyField.getAnnotation(Transient.class) != null) {
				continue;
			}
			// 拿到@Id注解
			Id idAnnotation = propertyField.getAnnotation(Id.class);
			Field field = propertyField.getAnnotation(Field.class);
			if (idAnnotation != null) {
				mapper.setId(propertyField.getName());
				continue;
			}
			if (field != null) {
				mapper.getName().put(
						propertyField.getAnnotation(Field.class).value(),
						propertyField);
			}
		}

		return mapper;
	}

	/**
	 * 根据集合名称以及级别生成自增id
	 * 
	 * @Title: getAutoIncreaseID
	 * @param cname
	 * @return
	 * @Author: airfey 2013-10-29 下午12:16:06
	 */
	@Override
	public Long getAutoIncreaseID(String cname) {
		return getAutoIncreaseID(cname, 1L);
	}

	/**
	 * 根据集合名称以及级别生成自增id
	 * 
	 * @Title: getAutoIncreaseID
	 * @param cname
	 * @param level
	 * @return
	 * @Author: airfey 2013-10-29 下午12:16:06
	 */
	private long getAutoIncreaseID(String cname, Number level) {
		BasicDBObject query = new BasicDBObject();
		query.put("cname", cname);
		BasicDBObject update = new BasicDBObject();
		update.put("$inc", new BasicDBObject("cid", level));
		DBObject dbObject2 = this.mongoTemplate.getCollection("inc_ids")
				.findAndModify(query, null, null, false, update, true, true);
		return (Long) dbObject2.get("cid");
	}

	public java.lang.reflect.Field getIdField(Class<?> clazz)
			throws NoSuchFieldException {
		AssertUtils.notNull(clazz);
		// 先从自身找，找到则直接返回，如果继承有父类则从递归查找父类的Id
		return clazz.getSuperclass().equals(Object.class) ? clazz
				.getDeclaredField(getCurrentMongoMapper().getId()) : this
				.getIdField(clazz.getSuperclass());
	}

	@SuppressWarnings("unused")
	private java.lang.reflect.Field getIdFieldFromSuperClass(Class<?> clazz) {
		if (clazz.getSuperclass().equals(Object.class)) { // 如果未继承父类
			try {
				return clazz.getDeclaredField(getCurrentMongoMapper().getId());
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		java.lang.reflect.Field idField = null;
		try {
			// 先尝试从自身找
			idField = clazz.getDeclaredField(getCurrentMongoMapper().getId());
		} catch (NoSuchFieldException e) {
			// 自身找不到递归去寻找父类，找到为止
			idField = getIdFieldFromSuperClass(clazz.getSuperclass());
		}
		return idField;
	}

	@Override
	public void save(T entity) {
		this.saveAndGetId(entity);
	}

	@Override
	public Long saveAndGetId(T entity) {
		// 生成主键。。
		if (currentMongoMapper.getId() == null) {
			throw new ParameterException(
					"---<<-To save the object must have a primary key ID[@Id], please check->>---");
		}
		Long id = this.getAutoIncreaseID(currentMongoMapper.getDocumentName());// 根据文档名称
		try {
			java.lang.reflect.Field field = getIdField(entity.getClass());
			field.setAccessible(true);
			field.set(entity, id);
			this.getMongoTemplate().save(entity);
		} catch (Exception e) {
			new ParameterException(e.getMessage());
		}
		return id;
	}

	@Override
	public void saveOrUpdate(T entity) throws BasicException {
		try {
			java.lang.reflect.Field field = getIdField(entity.getClass());
			field.setAccessible(true);
			Object val = field.get(entity); // 获取该对象的ID值
			Long id = null;
			if (val == null) {
				id = this.getAutoIncreaseID(currentMongoMapper.getId());
				field.set(entity, id);
			} else {
				if (!(val instanceof Number)) {
					throw new IllegalArgumentException(
							"this Id propertiey must be Number!Please check it!");

				}
				if (((Number) val).longValue() == 0L) {
					id = this.getAutoIncreaseID(currentMongoMapper.getId());
					field.set(entity, id);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ApplicationException(e);
		}
		this.getMongoTemplate().save(entity);
	}

	@Override
	public void update(T entity) throws BasicException {
		this.update(entity, true);
	}

	private void update(T entity, Boolean nullInBDable) {
		Query query = new Query();
		Update update = new Update();
		try {
			java.lang.reflect.Field idField = getIdField(entity.getClass());
			idField.setAccessible(true);
			query.addCriteria(Criteria.where(currentMongoMapper.getId()).is(
					idField.get(entity)));
			for (Map.Entry<String, java.lang.reflect.Field> entry : currentMongoMapper
					.getName().entrySet()) {
				String key = entry.getKey();
				java.lang.reflect.Field field = entry.getValue();
				field.setAccessible(true);
				// 如果有updateable，则不需要更新，直接忽略
				if (field.getAnnotation(Transient.class) != null
						|| (field.getAnnotation(Column.class) != null && !field
								.getAnnotation(Column.class).updateable())) {
					continue;
				}

				Object value = field.get(entity);
				// 类似关系型数据库的外键关联
				DBRef dbRef = field.getAnnotation(DBRef.class);
				if (dbRef != null) {
					String subDbName = dbRef.db();
					if (isEmpty(subDbName)) {
						Document document = field.getType().getAnnotation(
								Document.class);
						if (document != null) {
							subDbName = document.collection();
						} else {
							String className = field.getClass().getSimpleName();
							subDbName = className.substring(0, 1).toLowerCase()
									+ className.substring(1);
						}
					}
					update.set(key + ".$ref", subDbName);
					if (value != null) {
						// 获取value上的有@Id这个注解的字段
						List<java.lang.reflect.Field> fields = ReflectUitls
								.getAllFieldsByAnnotation(value.getClass(),
										Id.class);
						if (isEmpty(field)) {
							throw new IllegalArgumentException("-the property "
									+ entry.getValue().getName()
									+ " must have Id Field! must be have @Id");
						}
						java.lang.reflect.Field fie = fields.get(0);
						fie.setAccessible(true);
						// value = fie.get(value);
						update.set(key + ".$id", fie.get(value));
					} else {
						update.set(key + ".$id", null);
					}
					continue;
				}

				if (value != null) {
					update.set(key, value);
				} else if (nullInBDable == null || nullInBDable) {
					update.set(key, null);
				}
			}
		} catch (Exception e) {
			throw new ParameterException(e.getMessage());
		}
		getMongoTemplate().findAndModify(query, update, entity.getClass());
	}

	@Override
	public void deleteById(Serializable id) {
		getMongoTemplate().remove(
				new Query().addCriteria(Criteria.where("_id").is(id)),
				this.getEntityClass());
	}

	@Override
	public void deleteByIds(Collection<? extends Serializable> ids) {
		this.getMongoTemplate()
				.remove(Query.query(new Criteria("_id").in(ids)),
						this.getEntityClass());
	}

	@Override
	public T getById(Serializable id) throws BasicException {
		return (T) this.getMongoTemplate().findById(id, this.getEntityClass());
	}

	@Override
	public List<T> getByIds(Collection<? extends Serializable> ids) {
		return this.getMongoTemplate()
				.find(Query.query(new Criteria("_id").in(ids)),
						this.getEntityClass());
	}

	public String getDocumentName() {
		return this.getCurrentMongoMapper().getDocumentName();
	}

	/**
	 * 通过循环map拼装query
	 * 
	 * @Title: getQueryByMap
	 * @param map
	 *            参数列表（mongodb的字段名称为key，值为value） skip,limit为关键词，用于分页
	 * @return Query
	 * @Author: airfey 2013-10-29 下午2:30:45
	 */
	@SuppressWarnings("unused")
	private Query getQueryByMap(Map<String, Object> map) {
		Query query = new Query();
		if (map == null) {
			return query;
		}

		Set<Entry<String, Object>> entries = map.entrySet();
		// 循环添加参数
		for (Iterator<Entry<String, Object>> iterator = entries.iterator(); iterator
				.hasNext();) {
			Map.Entry<String, Object> entry = iterator.next();

			if (entry.getValue() == null) {
				continue;// 为null表示该条件不参与过滤
			}

			if ("skip".equals(entry.getKey())) {
				query.skip(Integer.valueOf(String.valueOf(entry.getValue())));
				continue;
			}
			if ("limit".equals(entry.getKey())) {
				query.limit(Integer.valueOf(String.valueOf(entry.getValue())));
				continue;
			}

			if (entry.getValue() instanceof Collection
					|| entry.getValue().getClass().isArray()) {
				query.addCriteria(Criteria.where(entry.getKey()).in(
						entry.getValue()));
				continue;
			}

			query.addCriteria(Criteria.where(entry.getKey()).is(
					entry.getValue()));
		}
		return query;
	}

	private Class getEntityClass() {
		this.entityClass = ReflectUitls.getGenericType(this.getClass());
		return entityClass;
	}

	public MongoMapperContext getMongoMapperContext() {
		return mongoMapperContext;
	}

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	private MongoMapper getCurrentMongoMapper() {
		return currentMongoMapper;
	}

	private void setCurrentMongoMapper(MongoMapper currentMongoMapper) {
		this.currentMongoMapper = currentMongoMapper;
	}

}