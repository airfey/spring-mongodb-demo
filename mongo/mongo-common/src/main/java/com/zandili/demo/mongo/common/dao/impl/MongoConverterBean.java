package com.zandili.demo.mongo.common.dao.impl;


import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoTypeMapper;

/**
 * 去掉映射时的_class
 *
 * @ClassName: MongoConverterBean
 * @author: airfey 2013-11-11 下午1:16:10   
 * @version V1.0   
 *
 */
public class MongoConverterBean implements FactoryBean<MappingMongoConverter> {

    private MappingMongoConverter converter;

    @Override
    public MappingMongoConverter getObject() throws Exception {
        MongoTypeMapper typeMapper = new DefaultMongoTypeMapper(null);
        converter.setTypeMapper(typeMapper);
        return converter;
    }

    public Class<?> getObjectType() {
        return MappingMongoConverter.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public MappingMongoConverter getConverter() {
        return converter;
    }

    public void setConverter(MappingMongoConverter converter) {
        this.converter = converter;
    }

}
