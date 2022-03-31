package com.example.springflink.redis;

import com.example.springflink.redis.domain.Response;
import com.example.springflink.utils.RedisUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import redis.clients.jedis.JedisPool;

/**
 * @author wangzuoyu1
 * @description
 */

@Component
public class RedisRateLimiter {

    private static final String KEY_RESOLVER = "key-resolver";
    private static final String REPLENISH_RATE = "redis-rate-limiter.replenishRate";
    private static final String BURST_CAPACITY = "redis-rate-limiter.burstCapacity";
    private String script;
    private JedisPool pool;

    public RedisRateLimiter() {
        pool = RedisUtil.getPool();
        try {
            script = FileUtils.readFileToString(new File("src/main/resources/redis/rateLimiter.lua"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Mono<Response> isAllowed(String routeId) {
        RedisRateLimiter.RouterConfig routerConfig = this.loadConfig(routeId);
        double replenishRate = routerConfig.getReplenishRate();
        double burstCapacity = routerConfig.getBurstCapacity();
        try {
            List<String> keys = getKeys(routerConfig.getKey());
            List<String> scriptArgs = Arrays.asList(replenishRate + "",burstCapacity + "",
                Instant.now().getEpochSecond() + "", "1");
            Flux<List<Long>> luaResult = Flux.just((List<Long>) pool.getResource().eval(script, keys, scriptArgs));
            return luaResult.onErrorResume(throwable -> Flux.just(Arrays.asList(1L, -1L)))
                .reduce(new ArrayList<>(), ((response, l) -> {
                    response.addAll(l);
                    return response;
                })).map((result)-> {
                        boolean allowed = (Long) result.get(0) == 1L;
                        Long tokensLeft = (Long) result.get(1);
                        Response response = new Response(allowed, tokensLeft);
                        return response;
                    });
        }catch (Exception e) {
            e.printStackTrace();
            return Mono.just(new Response(true, -1L));
        }
    }


    /**
     * 生成tokens_key 和timestamp_key
     * @param id
     * @return
     */
    private List<String> getKeys(String id) {
        String prefix = "request_rate_limiter.{" + id;
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }

    private RouterConfig loadConfig(String routeId) {
        Config config = ConfigFactory.load("redis/redis_lua.conf").resolve();
        Config routerConfig = config.getConfig(routeId);
        return new RouterConfig(routerConfig.getString(KEY_RESOLVER), routerConfig.getDouble(REPLENISH_RATE)
            , routerConfig.getDouble(BURST_CAPACITY));
    }

    @Data
    @AllArgsConstructor
    @Accessors(chain = true)
    class RouterConfig {
        private String key; // 限流key
        private double replenishRate; // 每秒处理的请求数量
        private double burstCapacity; // capacity代表1s时间间隔内的允许最大qps
    }

}
