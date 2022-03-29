package com.example.springflink.task.log;

import com.example.springflink.domain.LogEntity;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @author wangzuoyu1
 * @description
 */

@Slf4j
public class NothingAction implements Action {

    @Override
    public void execute(LogEntity logEntity, Jedis redisClient) {
        log.info("do nothing");
    }
}
