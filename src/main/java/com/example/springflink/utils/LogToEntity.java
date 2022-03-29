package com.example.springflink.utils;

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
        Map<String, Object> map = new HashMap<>();
        LogEntity logEntity = new LogEntity();
        try {
            map = JsonUtil.json2Map(s);
            logEntity.setUserId((Integer) map.getOrDefault("userId", 0))
                     .setProductId((Integer) map.getOrDefault("productId", 0))
                     .setAction((String) map.getOrDefault("action", "nothing"))
                     .setTime((Long) map.getOrDefault("time", System.currentTimeMillis()))
                     .setCount((Integer) map.getOrDefault("count", 0));
        } catch (Exception e) {
            log.error("Transfer to map failed!");
            e.printStackTrace();
        }
        return logEntity;
    }

}
