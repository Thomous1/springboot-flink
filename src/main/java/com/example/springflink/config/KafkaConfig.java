package com.example.springflink.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangzuoyu1
 * @description
 */

@ConfigurationProperties("spring.kafka.template")
@EnableConfigurationProperties
@Configuration
@Data
public class KafkaConfig {
    private String topic;
}
