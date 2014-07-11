package com.zandili.demo.mongo.common.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 解析properties文件
 * 
 * @ClassName: SpringPropertyPlaceholderConfigurerUtils
 * @author: airfey 2013-10-28 下午1:37:46
 * @version V1.0
 * 
 */
public class SpringPropertyPlaceholderConfigurerUtils extends
		PropertyPlaceholderConfigurer {
	private static Map<String, String> propertiesMap;

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		propertiesMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			propertiesMap.put(keyStr, value);
		}
	}

	public static String getProperty(String name) {
		return propertiesMap.get(name);
	}

	public static Long getLongProperty(String name) {
		return Long.parseLong(getProperty(name));
	}

	public static Integer getIntegerProperty(String name) {
		return Integer.parseInt(getProperty(name));
	}

	public static Double getDoubleProperty(String name) {
		return Double.parseDouble(getProperty(name));
	}

	public static Float getFloatProperty(String name) {
		return Float.parseFloat(getProperty(name));
	}

}