package com.zandili.demo.mongo.common.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 列注解，用于判断使用MongoDB时是否更新某字段(update方法)
 *
 * @ClassName: Column
 * @author: airfey 2013-11-8 下午12:14:10   
 * @version V1.0   
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	boolean updateable() default true;
}
