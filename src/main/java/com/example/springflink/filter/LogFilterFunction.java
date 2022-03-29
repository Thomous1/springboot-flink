package com.example.springflink.filter;

import com.example.springflink.domain.LogEntity;
import org.apache.flink.api.common.functions.FilterFunction;

/**
 * @author wangzuoyu1
 * @description 处理各种过滤逻辑
 */
public class LogFilterFunction implements FilterFunction<LogEntity> {

    @Override
    public boolean filter(LogEntity logEntity) throws Exception {
        if (logEntity.getUserId()%2 == 0)
            return true;
        return false;
    }
}
