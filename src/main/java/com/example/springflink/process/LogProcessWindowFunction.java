package com.example.springflink.process;

import com.example.springflink.domain.LogEntity;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author wangzuoyu1
 * @description
 */
@Slf4j
public class LogProcessWindowFunction extends ProcessAllWindowFunction<LogEntity, LogEntity,TimeWindow> {

    @Override
    public void open(Configuration parameters) throws Exception {
        log.info("get before process env");
    }

    @Override
    public void process(Context context, Iterable<LogEntity> iterable,
        Collector<LogEntity> collector) throws Exception {
        Iterator<LogEntity> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            LogEntity logEntity = iterator.next();
            collector.collect(logEntity);
        }
    }
}
