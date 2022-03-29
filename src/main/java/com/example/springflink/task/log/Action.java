package com.example.springflink.task.log;

import com.example.springflink.domain.LogEntity;
import redis.clients.jedis.Jedis;

/**
 * @author wangzuoyu1
 * @description 日志增删改查
 */
public interface Action {
    void execute(LogEntity log, Jedis redisClient);
}
