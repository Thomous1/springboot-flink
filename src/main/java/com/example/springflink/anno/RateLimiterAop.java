package com.example.springflink.anno;

import com.example.springflink.exception.RateLimiterRejectException;
import com.google.common.util.concurrent.RateLimiter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author wangzuoyu1
 * @description
 */
@Component
@Aspect
public class RateLimiterAop {

    private static final String DEFAULT_VAL = "default";
    private Map<String, RateLimiter> map = new HashMap<>();

    private Config rateLimiterConfig;

    public RateLimiterAop() {
        rateLimiterConfig = ConfigFactory.load("redis/rateLimiter.conf").resolve().getConfig("rateLimiter");
        Iterator<Entry<String, ConfigValue>> iterator = rateLimiterConfig.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, ConfigValue> entry = iterator.next();
            map.put(entry.getKey(),RateLimiter.create(rateLimiterConfig.getDouble(entry.getKey())));
        }
        map.put("default",RateLimiter.create(5d));
    }

    @Pointcut(value = "@annotation(com.example.springflink.anno.MyRateLimiter)")
    public void rateLimit(){}

    @Around("rateLimit()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        MyRateLimiter rateLimiter = signature.getMethod().getAnnotation(MyRateLimiter.class);
        String value = rateLimiter.value();
        if (!map.containsKey(value)) {
            value = DEFAULT_VAL;
        }
        if (map.get(value).tryAcquire()) {
            return joinPoint.proceed();
        }
        throw new RateLimiterRejectException("被限流啦～");
    }
}
