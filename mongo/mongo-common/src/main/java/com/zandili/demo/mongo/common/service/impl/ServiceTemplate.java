package com.zandili.demo.mongo.common.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Package: cn.damai.usercenter.common.service.impl
 * @Description: TODO
 * @author: airfey
 * @date: 13-2-26 - 上午10:09
 * @version: V1.0
 * @company: damai
 */
public class ServiceTemplate {

    private static Boolean status = Boolean.FALSE;

    private static Logger logger = LoggerFactory.getLogger(ServiceTemplate.class);

    public static Boolean doWork(ServiceCallBack serviceCallBack) {
        try{
            serviceCallBack.execute();
            //操作成功
            status = Boolean.TRUE;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return status;
    }
}