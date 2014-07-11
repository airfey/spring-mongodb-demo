package com.zandili.demo.mongo.common.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zandili.demo.mongo.common.exception.ApplicationException;

import static com.zandili.demo.mongo.common.util.CommonUtils.isEmpty;


/**
 * 用JAVA Bean 反射得到set,get方法工具类
 * 
 * @ClassName: ReflectUitls
 * @author: airfey 2013-11-8 下午1:25:21
 * @version V1.0
 * 
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReflectUitls {

	// 获取泛型上的具体类型（第一个）
	public static Class<?> getGenericType(Class<?> clazz) {
		return getGenericType(clazz, 0);
	}

	// 获取泛型上的具体类型（指定哪个类型）
	public static Class<?> getGenericType(Class<?> clazz, int i) {
		Type type = clazz.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] types = ((ParameterizedType) type).getActualTypeArguments();
			return (Class<?>) types[i];
		}
		return null;
	}

	/**
	 * 
	 * java反射bean的get方法
	 * 
	 * @param clazz
	 *            javaBean对象类型
	 * @param fieldName
	 *            字段名称
	 * 
	 * @return get方法
	 */
	public static Method getGetMethod(Class<?> clazz, String fieldName)
			throws NoSuchMethodException {
		// get+字段名第一个字母小写，得到get方法名

		// 拿到拷贝源上的属性器数组
		try {
			PropertyDescriptor[] objPds = Introspector.getBeanInfo(clazz)
					.getPropertyDescriptors();

			for (int i = 0; objPds.length > 1 && i < objPds.length; i++) {
				// 跳出从object继承的class属性,源上必须有get方法
				if (Class.class == objPds[i].getPropertyType()
						|| objPds[i].getReadMethod() == null) {
					continue;
				}

				if (objPds[i].getName().equals(fieldName)) {
					return objPds[i].getReadMethod();
				}
			}

		} catch (IntrospectionException e) {
			throw new NoSuchMethodException("----");
		}

		return null;

	}

	/**
	 * 
	 * java反射bean的set方法
	 * 
	 * @param clazz
	 *            javaBean对象
	 * @param fieldName
	 *            字段名称
	 * 
	 * @return set方法
	 */
	public static Method getSetMethod(Class<?> clazz, String fieldName) {
		try {
			PropertyDescriptor[] objPds = Introspector.getBeanInfo(
					clazz.getClass()).getPropertyDescriptors();

			for (int i = 0; objPds.length > 1 && i < objPds.length; i++) {
				// 跳出从object继承的class属性,源上必须有get方法
				if (Class.class == objPds[i].getPropertyType()
						|| objPds[i].getReadMethod() == null) {
					continue;
				}

				if (objPds[i].getName().equals(fieldName)) {
					return objPds[i].getWriteMethod();
				}
			}

		} catch (IntrospectionException e) {
			throw new ApplicationException(e.getMessage());
		}

		return null;
	}

	/**
	 * 
	 * 执行set方法
	 * 
	 * @param o
	 *            执行对象
	 * @param fieldName
	 *            属性
	 * @param value
	 *            值
	 */
	public static void invokeSet(Object o, String fieldName, Object value) {
		Method method = getSetMethod(o.getClass(), fieldName);
		try {
			method.invoke(o, value);
		} catch (Exception e) {
			throw new ApplicationException("invoke" + o.getClass().getName()
					+ "set Method Error,Detail：" + e.getMessage());
		}
	}

	/**
	 * 
	 * 执行get方法
	 * 
	 * @param o
	 *            执行对象
	 * @param fieldName
	 *            属性
	 * @return 该get方法的返回值
	 */
	public static Object invokeGet(Object o, String fieldName) {
		try {
			Method method = getGetMethod(o.getClass(), fieldName);
			return method.invoke(o);
		} catch (Exception e) {
			throw new ApplicationException("invoke " + o.getClass().getName()
					+ "get Method Error，Detail：" + e.getMessage());
		}
	}

	/**
	 * 获得目标类中含有某注解的某些个Field
	 * 
	 * @Title: getFieldsByAnnotation
	 * @param objClass
	 *            目标类Class
	 * @param annotationClass
	 *            查询的注解
	 * @param index
	 *            查询多少个
	 * @return Field
	 * @author airfey 2013-1-5下午3:35:07
	 */
	/**
	 * 根据注解获取类上有该注解的全部字段
	 * 
	 * @param objClass
	 * @param annotationClass
	 * @return
	 */

	public static List<Field> getFieldsByAnnotation(Class objClass,
			Class annotationClass) {
		if (isEmpty(objClass) || isEmpty(annotationClass)) {
			// logger.error("objClass or annotationClass Must be not Null!");
			return Collections.EMPTY_LIST;
		}

		List<Field> fieldList = new ArrayList<Field>(0);
		for (Field field : objClass.getDeclaredFields()) {
			if (field.getAnnotation(annotationClass) != null) {
				fieldList.add(field);
			}
		}
		return fieldList;
	}

	public static List<Field> getAllFieldsByAnnotation(Class objClass,
			Class annotationClass) {
		if (isEmpty(objClass) || isEmpty(annotationClass)) {
			// logger.error("objClass or annotationClass Must be not Null!");
			return Collections.EMPTY_LIST;
		}
		List<Field> fieldList = new ArrayList<Field>(0);
		getFieldFromSuperClass(fieldList, objClass, annotationClass);
		return fieldList;
	}

	public static void getFieldFromSuperClass(List<Field> fieldList,
			Class<?> objClass, Class annotationClass) {

		if (objClass.getSuperclass().equals(Object.class)) {
			for (Field field : objClass.getDeclaredFields()) {
				if (field.getAnnotation(annotationClass) != null) {
					fieldList.add(field);
				}
			}
			return;
		} else {
			for (Field field : objClass.getDeclaredFields()) {
				if (field.getAnnotation(annotationClass) != null) {
					fieldList.add(field);
				}
			}
			getFieldFromSuperClass(fieldList, objClass.getSuperclass(),
					annotationClass);
		}
	}

	public static Field getFieldByAnnotation(Class objClass,
			Class annotationClass) {
		if (isEmpty(objClass) || isEmpty(annotationClass)) {
			// logger.error("objClass or annotationClass Must be not Null!");
			return null;
		}
		for (Field field : objClass.getDeclaredFields()) {
			if (field.getAnnotation(annotationClass) != null) {
				return field;
			}
		}
		return null;
	}

}
