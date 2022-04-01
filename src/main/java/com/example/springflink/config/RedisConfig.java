package com.example.springflink.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @author wangzuoyu1
 * @description
 */

@Data
@ConfigurationProperties("spring.redis")
@EnableConfigurationProperties
@Configuration
public class RedisConfig {
    private String host;
    private int port;

    @Bean(name = "redisTemplate")
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean(name = "reactiveRedisTemplate")
    @Primary
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(LettuceConnectionFactory connectionFactory) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        RedisSerializationContext.SerializationPair<String> keySerializationPair =
            RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer);
        RedisSerializationContext.SerializationPair<Object> valueSerializationPair =
            RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer);
        RedisSerializationContext.SerializationPair<Object> hashValueSerializationPair =
            RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer);
        RedisSerializationContext<String, Object> context = new RedisSerializationContext<String, Object>() {
            @Override
            public SerializationPair getKeySerializationPair() {
                return keySerializationPair;
            }

            @Override
            public SerializationPair getValueSerializationPair() {
                return valueSerializationPair;
            }

            @Override
            public SerializationPair getHashKeySerializationPair() {
                return keySerializationPair;
            }

            @Override
            public SerializationPair getHashValueSerializationPair() {
                return hashValueSerializationPair;
            }

            @Override
            public SerializationPair<String> getStringSerializationPair() {
                return keySerializationPair;
            }
        };
        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }

}

