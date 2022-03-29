package com.example.springflink.sink;

import com.example.springflink.domain.LogEntity;
import com.example.springflink.task.log.ActionManager;
import com.example.springflink.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @author wangzuoyu1
 * @description
 */
@Component
@Lazy(value = false)
@Slf4j
public class LogSink extends RichSinkFunction<LogEntity> {

    private Jedis jedisClient;

    @Override
    public void open(Configuration parameters) throws Exception {
        jedisClient = RedisUtil.getClient();
    }

    @Override
    public void invoke(LogEntity value, Context context) throws Exception {
        new ActionManager().execute(value, jedisClient);
    }


}
