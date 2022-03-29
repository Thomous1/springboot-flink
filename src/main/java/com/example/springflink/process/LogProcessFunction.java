package com.example.springflink.process;

import com.example.springflink.domain.LogEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author wangzuoyu1
 * @description
 * ProcessFunction是一个低级的流处理操作,收到一个请求处理一次
 * 可以对数据内容进行加工，RPC等各种数据加工都可
 * 使用processFunction 处理，未使用ProcessWindowFunction
 * ProcessWindowFunction 最好和reduce一起使用，不然是缓存所有落入该时间窗口的数据，等待窗口触发时候再一起执行，
 * 大数据量容易发生OOM
 */

@Slf4j
public class LogProcessFunction extends ProcessFunction<LogEntity, LogEntity> {

    @Override
    public void open(Configuration parameters) throws Exception {
        log.info("get before process env");
    }

    @Override
    public void processElement(LogEntity logEntity, Context context, Collector<LogEntity> collector)
        throws Exception {
        collector.collect(logEntity);
    }
}
