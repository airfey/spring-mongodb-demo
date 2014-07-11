package com.zandili.demo.mongo.common.dao.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Package cn.damai.messagecenter.common.dao
 * @Description TODO
 * @author airfey
 * @date 13-3-25 - 下午321
 * @version V1.0
 * @company damai
 */
public class MongoMapper {
    //表名
    private String documentName;
    //类的全名称
    private String className;
    //名称(db里的key:类属性名)
    private Map<String,Field> name = new HashMap<String,Field>(0);
    //主键
    private String id;

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, Field> getName() {
        return name;
    }

    public void setName(Map<String, Field> name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}