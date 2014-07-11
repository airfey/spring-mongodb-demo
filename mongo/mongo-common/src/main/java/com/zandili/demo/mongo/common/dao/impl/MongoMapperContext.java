package com.zandili.demo.mongo.common.dao.impl;


import java.io.Serializable;

import java.util.concurrent.ConcurrentHashMap;

 
@SuppressWarnings("serial")
public class MongoMapperContext implements Serializable {

    private ConcurrentHashMap<Class<?>,MongoMapper> concurrentHashMap = new ConcurrentHashMap<Class<?>,MongoMapper>();

    public final MongoMapper get(Class<?> clazz){
        return this.concurrentHashMap.get(clazz);
    }

    public final void put(Class<?> clazz,MongoMapper mongoMapper){
        this.concurrentHashMap.put(clazz,mongoMapper);
    }
}



