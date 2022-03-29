package com.example.springflink.reduce;

import com.example.springflink.domain.LogEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.ReduceFunction;

/**
 * @author wangzuoyu1
 * @description
 */

@Slf4j
public class LogReduceFunction implements ReduceFunction<LogEntity> {

    @Override
    public LogEntity reduce(LogEntity logEntity, LogEntity t1) throws Exception {
        LogEntity logResult = new LogEntity();
        if (logEntity.getCount() == t1.getCount()
            && logEntity.getUserId() == t1.getUserId()
            && logEntity.getProductId() == t1.getProductId()) {
            logResult.setCount(logEntity.getCount() + t1.getCount());
            logResult.setProductId(logEntity.getProductId());
            logResult.setUserId(logEntity.getUserId());
            if (logEntity.getTime().compareTo(t1.getTime()) >=0) {
                logResult.setTime(logEntity.getTime());
            }else {
                logResult.setTime(t1.getTime());
            }
        }
        return logResult;
    }
}
