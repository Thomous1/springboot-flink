package com.example.springflink.checkpoint;

import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author wangzuoyu1
 * @description 设置checkPoint
 * Flink checkPoint 大致流程：
 *  1、暂停处理新流入数据，将数据缓存起来
 *  2、将算子自任务的本地状态copy到一个远程持久化存储上
 *  3、继续处理新流出的数据，包括刚才缓存的数据。
 * Flink 是在Chandy-Lamport算法的基础上实现的一种分布式快照算法。Checkpoint Barrier会生成快照，
 * Flink的检查点协调器（Checkpoint Coordinator）触发一次checkPoint会发送到source的各个子任务。
 * 各source算子子任务接收到checkpoint请求之后，会将自己的状态写入到状态后端，生成一次快照，并向下游广播。
 * source算子做完快照之后会向coordinator发送ack确认，包括一些元数据（state Backend 状态句柄或者状态指针）
 *
 * checkpoint barrier 传播和对齐：
 *  1、算子子任务在某个通道收到第一个ID为n的checkpoint barrier，但是其他输入通道ID为n的barrier未到达，
 *  该算子子任务开始准备进行对齐
 *  2、算子子任务将第一个输入通道的数据缓存下来，同时继续处理其他通道的数据，这个过程称之为对齐。
 *  3、第二个输入通道barrier抵达该算子子任务，该算子子任务执行快照，并将状态写入state backend，然后将id为n
 *  的checkpoint barrier向下游所有的输出通道广播。
 *  4、对于这个算子子任务，快照之行结束，继续处理各输入通道新流入数据，包括刚才缓存的数据。
 *  每个算子子任务执行完上述的工作之后，当最后所有的sink算子确认完成快照之后，说明id为n的checkPoint子行结束，
 *  checkpoint coordinator向state backend 写入一些本次的checkpoint元数据。
 *  对齐是要保证一个flink作业所有的算子状态一致。
 */
public class CheckPoint {

    private static final long CHECK_POINT_TIME = 600000;
    private static final long CHECK_POINT_TIMEOUT = 3600*1000; //超时时间设置为1h
    private static final long CHECK_POINT_BETWEEN_TIME = 60*1000; //两次checkPoint的时间间隔为60s
    public static void setCheckPoint(StreamExecutionEnvironment environment) {
        // 设置checkPoint time
        environment.enableCheckpointing(CHECK_POINT_TIME);
        // 这是at_least_once
        environment.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.AT_LEAST_ONCE);
        //environment.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE); //次方式Checkpoint Barrier对齐
        // 检查点完成时间
        environment.getCheckpointConfig().setCheckpointTimeout(CHECK_POINT_TIMEOUT);
        // checkPoint 外部持久化 DELETE_ON_CANCELLATION,在job canceled时候会自动删除externalized state，如果失败则保留
        environment.getCheckpointConfig().enableExternalizedCheckpoints(
            ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);
        // 设置时间间隔
        environment.getCheckpointConfig().setMinPauseBetweenCheckpoints(CHECK_POINT_BETWEEN_TIME);
        // 关闭checkpoint过程失败导致应用重启
        environment.getCheckpointConfig().setPreferCheckpointForRecovery(false);
    }
}
