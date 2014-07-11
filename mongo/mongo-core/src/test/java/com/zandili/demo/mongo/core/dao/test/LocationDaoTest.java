package com.zandili.demo.mongo.core.dao.test;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.Point;

import com.zandili.demo.mongo.core.dao.LocationDao;
import com.zandili.demo.mongo.core.domain.Location;
import com.zandili.demo.mongo.core.test.BaseTest;


public class LocationDaoTest extends BaseTest {
	@Autowired
	private LocationDao locationDao;

	@Before
	public void setUp() {
		// 等同db.location.ensureIndex( {position: "2d"} )
		// .indexOps(Location.class).ensureIndex(new
		// GeospatialIndex("position"));
		// // 初始化数据
		// template.save(new Location("A", 0.1, -0.1));
		// template.save(new Location("B", 1, 1));
		// template.save(new Location("C", 0.5, 0.5));
		// template.save(new Location("D", -0.5, -0.5));
	}
    /**
     * 插入保存4个坐标点信息 
     *
     * @Title: save   
     * @Author: airfey 2013-11-12 下午1:33:45
     */
	@Test
	public void save() {
		locationDao.save(new Location("A", 0.1, -0.1));
		locationDao.save(new Location("B", 1, 1));
		locationDao.save(new Location("C", 0.5, 0.5));
		locationDao.save(new Location("D", -0.5, -0.5));

	}

	@Test
	public void findCircleNearTest() {
		// 查询point(0,0),半径0.7附近的点
		List<Location> locations = locationDao.findCircleNear(new Point(0, 0),
				0.7);
		print(locations);
		System.err.println("-----------------------");
		// 查询point(0,0),半径0.75附近的点
		locations = locationDao.findCircleNear(new Point(0, 0), 0.75);
		print(locations);
	}

	@Test
	public void findBoxNearTest() {
		// 查询point(0.2, 0.2)和point(1, 1)组成的矩形内的点
		List<Location> locations = locationDao.findBoxNear(new Point(0.2, 0.2),
				new Point(1, 1));
		print(locations);
	}

	public static void print(Collection<Location> locations) {
		for (Location location : locations) {
			System.err.println(location.getName()+"="+location.getPosition()[0]+","+location.getPosition()[1]);
		}
	}
}
