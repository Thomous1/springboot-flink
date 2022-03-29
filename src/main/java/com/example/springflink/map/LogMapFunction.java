package com.example.springflink.map;

import com.example.springflink.domain.LogEntity;
import com.example.springflink.task.log.ActionManager;
import com.example.springflink.utils.LogToEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author wangzuoyu1
 * @description
 */
@Slf4j
public class LogMapFunction implements MapFunction<String, LogEntity> {

    @Override
    public LogEntity map(String s){
        log.info("form kafka message is: {}", s);
        LogEntity logEntity = LogToEntity.getLog(s);
        return logEntity;
    }

}
