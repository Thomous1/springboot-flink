/*
package com.example.springflink.redis.domain;

import com.example.springflink.redis.RedisRateLimiter;


*/
/**
 * @author wangzuoyu1
 * @description
 *//*

public class Test {

    public static void main(String[] args) throws InterruptedException {
        RedisRateLimiter redisRateLimiter = new RedisRateLimiter();
        for (int i = 1; i <= 20; i++) {
            Thread.sleep(10);
            final int j = i;
            redisRateLimiter.isAllowed("test").subscribe(response -> {
                if (response.isAllowed()) {
                    System.out.println(j + "未被限流" + System.currentTimeMillis());
                }else {
                    System.out.println(j + "已被限流" + System.currentTimeMillis());
                }
            });

        }
    }
}
*/
