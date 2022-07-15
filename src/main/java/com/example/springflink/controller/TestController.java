/*
package com.example.springflink.controller;

import com.example.springflink.anno.MyRateLimiter;
import com.example.springflink.redis.RedisRateLimiter;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

*/
/**
 * @author wangzuoyu1
 *//*


@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    RedisRateLimiter redisRateLimiter;

    @RequestMapping("/get/{name}")
    @MyRateLimiter(value = "test")
    public Mono<String> test(@PathVariable("name") String name) {
        AtomicBoolean flag = new AtomicBoolean(false);
        redisRateLimiter.isAllowed("test").subscribe(response -> {
            if (response.isAllowed()) {
                flag.set(true);
            }
        });
        if (flag.get()) {
            return Mono.just(name);
        }
        throw new RuntimeException("error！！！！");
    }
}
*/
