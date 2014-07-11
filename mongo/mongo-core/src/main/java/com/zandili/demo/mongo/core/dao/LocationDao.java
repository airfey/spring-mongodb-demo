package com.zandili.demo.mongo.core.dao;

import java.util.List;

import org.springframework.data.mongodb.core.geo.Point;

import com.zandili.demo.mongo.common.dao.Dao;
import com.zandili.demo.mongo.core.domain.Location;


public interface LocationDao extends Dao<Location> {

	/**
	 * 按照圆形区域查找附近地点
	 * 
	 * @Title: findCircleNear
	 * @param point
	 * @param maxDistance
	 * @return
	 * @Author: airfey 2013-11-11 下午3:41:08
	 */
	public List<Location> findCircleNear(Point point, double maxDistance);

	/**
	 * 按照正方形区域查找附近地点
	 * 
	 * @Title: findBoxNear
	 * @param lowerLeft
	 * @param upperRight
	 * @return
	 * @Author: airfey 2013-11-11 下午3:42:01
	 */
	public List<Location> findBoxNear(Point lowerLeft, Point upperRight);
}
