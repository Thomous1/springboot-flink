package com.example.springflink.task.log;

import com.example.springflink.checkpoint.CheckPoint;
import com.example.springflink.checkpoint.StateBackend;
import com.example.springflink.config.KafkaConfig;
import com.example.springflink.map.LogMapFunction;
import com.example.springflink.process.LogProcessFunction;
import com.example.springflink.sink.LogSink;
import com.example.springflink.utils.ApplicationContextUtil;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.springframework.stereotype.Component;

/**
 * @author wangzuoyu1
 * @description 接受日志
 */
@Slf4j
@Component
public class LogTask {

    public static void main(String[] args) {
        KafkaConfig kafkaConfig = ApplicationContextUtil.getBeanByType(KafkaConfig.class);
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        CheckPoint.setCheckPoint(environment);
        StateBackend.setStateBackend(environment);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("zookeeper.connect", "localhost:2181");
        properties.setProperty("group.id", "kafka_group_test");
        DataStreamSource<String> dataStreamSource = environment
            .addSource(new FlinkKafkaConsumer<String>(kafkaConfig.getTopic(),new SimpleStringSchema(),properties));
        dataStreamSource
            .setParallelism(1)
            .map(new LogMapFunction())
            .process(new LogProcessFunction())
            .addSink(new LogSink());
        try {
            environment.execute("log-from-kafka");
        } catch (Exception e) {
            log.error("task[{}] execute failed!", "log-from-kafka");
            e.printStackTrace();
        }
    }
}
