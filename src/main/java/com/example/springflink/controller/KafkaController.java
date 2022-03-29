package com.example.springflink.controller;

import com.example.springflink.domain.LogEntity;
import com.example.springflink.utils.JsonUtil;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangzuoyu1
 * @description
 */

@RestController
@RequestMapping("/kafka")
@Slf4j
public class KafkaController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${spring.kafka.template.default-topic}")
    private String topic;

    @RequestMapping("/send/{action}")
    public String sendMessage(@PathVariable(name = "action")String action) {
        for (int i = 10; i < 20; i++) {
            LogEntity logEntity = new LogEntity()
                .setAction(action)
                .setUserId(i + 1)
                .setTime(System.currentTimeMillis())
                .setCount(1);
            if (i%2==0) {
                logEntity.setProductId(i+1);
            }else {
                logEntity.setProductId(i);
            }
            try {
                kafkaTemplate.send(topic, JsonUtil.write2JsonStr(logEntity)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

    @RequestMapping("/send/async/{action}")
    public String sendMessageAsync(@PathVariable(name = "action")String action) {
        for (int i = 0; i < 10; i++) {
            LogEntity logEntity = new LogEntity()
                .setAction(action)
                .setUserId(i + 1)
                .setProductId(i+1)
                .setCount(1)
                .setTime(System.currentTimeMillis());
            ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(topic, JsonUtil.write2JsonStr(logEntity));
            send.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.error("send msg to kafka failed {}",throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, String> sendResult) {
                    log.info("topic is : {}, offset is : {}", sendResult.getRecordMetadata().topic(), sendResult.getRecordMetadata().offset());
                    log.info("producer msg is : {}, recordData msg is : {}", sendResult.getProducerRecord().toString(), sendResult.getRecordMetadata().toString());
                    log.info("send msg to kafka success");
                }
            });
        }
        return "success";
    }
}
