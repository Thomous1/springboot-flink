package com.example.springflink.task.log;

import com.example.springflink.domain.LogEntity;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @author wangzuoyu1
 * @description
 */
@Slf4j
public class AddAction implements Action {

    @Override
    public void execute(LogEntity logEntity, Jedis redisClient) {
        log.info("do add action：{}", logEntity);
        redisClient.set(getKey(logEntity), logEntity.toString());
    }

    private String getKey(LogEntity logEntity) {
        return logEntity.getUserId() + "_" + logEntity.getProductId();
    }
}
