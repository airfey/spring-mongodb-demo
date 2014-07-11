package com.zandili.demo.mongo.core.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.geo.Box;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.zandili.demo.mongo.common.dao.impl.DaoImpl;
import com.zandili.demo.mongo.core.dao.LocationDao;
import com.zandili.demo.mongo.core.domain.Location;


@Repository
public class LocationDaoImpl extends DaoImpl<Location> implements LocationDao {

	private static final long serialVersionUID = -8617442467345557297L;

	 
	public List<Location> findCircleNear(Point point, double maxDistance) {
		// this.getMongoTemplate().indexOps(Location.class)
		// .ensureIndex(new GeospatialIndex("pos"));
		return this.getMongoTemplate().find(
				new Query(Criteria.where("pos").near(point)
						.maxDistance(maxDistance)), Location.class);
	}

 
	public List<Location> findBoxNear(Point lowerLeft, Point upperRight) {
		// this.getMongoTemplate().indexOps(Location.class)
		// .ensureIndex(new GeospatialIndex("pos"));
		return this.getMongoTemplate().find(
				new Query(Criteria.where("pos").within(
						new Box(lowerLeft, upperRight))), Location.class);
	}

}
