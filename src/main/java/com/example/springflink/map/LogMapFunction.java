package com.example.springflink.map;

import com.example.springflink.domain.LogEntity;
import com.example.springflink.hbase.HbaseClient;
import com.example.springflink.task.log.ActionManager;
import com.example.springflink.utils.LogToEntity;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author wangzuoyu1
 * @description
 */
@Slf4j
public class LogMapFunction implements MapFunction<String, LogEntity> {

    @Override
    public LogEntity map(String s) throws Exception {
        log.info("form kafka message is: {}", s);
        LogEntity logEntity = LogToEntity.getLog(s);
/*        if (!Objects.isNull(logEntity)) {
            String rowKey = logEntity.getUserId() + "_" + logEntity.getProductId()+ "_"+ logEntity.getTime();
            HbaseClient.putData("con",rowKey,"log","user_id",String.valueOf(logEntity.getUserId()));
            HbaseClient.putData("con",rowKey,"log","product_id",String.valueOf(logEntity.getProductId()));
            HbaseClient.putData("con",rowKey,"log","time",logEntity.getTime().toString());
            HbaseClient.putData("con",rowKey,"log","action",logEntity.getAction());
        }*/
        return logEntity;
    }

}
