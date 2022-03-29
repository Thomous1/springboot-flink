package com.example.springflink.checkpoint;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author wangzuoyu1
 * @description
 *  state backend起到了持久化存储数据的重要功能，有三种state
 *  1、memoryStateBackend
 *  是基于内存的，将数据存储在java的堆区，当进行分布式快照时，所有算子子任务将自己的内存上状态同步到jobManager的对上
 *  一个作业的所有状态要小于JobManager的内存大小，所有这种方式不能存储过大的状态数据。不建议生产环境使用。
 *  2、FsStateBackend
 *  这种方式，数据持久化到文件系统，包括本地磁盘、HDFS以及各种云存储服务。
 *  3、RocksDBStateBackend
 *  存在本地的RocksDB上。kv数据库，存在本地磁盘。可以存储的本地状态更大，但是每次都需要序列化和反序列化，导致读写成本
 *  变高，执行快照时，也要配置分布式存储的地址。
 */
public class StateBackend {

    private static final String CHECKPOINT_PATH = "file:///usr/local/Cellar/apache-flink/checkpoint/data";
    public static void setStateBackend(StreamExecutionEnvironment environment) {
        environment.setStateBackend(new FsStateBackend(CHECKPOINT_PATH, false));
    }
}
