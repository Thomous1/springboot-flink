package com.example.springflink.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.springflink.domain.LogEntity;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangzuoyu1
 * @description string 的msg 转为entity
 */

@Slf4j
public class LogToEntity {

    public static LogEntity getLog(String s) {
        JSONObject map = new JSONObject();
        LogEntity logEntity = new LogEntity();
        try {
             map = JSON.parseObject(s);
             logEntity.setUserId((Integer) map.getOrDefault("userId", 0))
                     .setProductId((Integer) map.getOrDefault("productId", 0))
                     .setAction((String) map.getOrDefault("action", "nothing"))
                     .setTime((Long) map.getOrDefault("time", System.currentTimeMillis()))
                     .setCount((Integer) map.getOrDefault("count", 0));
        } catch (Exception e) {
            log.error("Transfer to map failed!");
            e.printStackTrace();
            logEntity.setAction("nothing");
        }
        return logEntity;
    }

}
