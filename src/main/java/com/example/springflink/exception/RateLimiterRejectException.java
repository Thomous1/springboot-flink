package com.example.springflink.exception;

/**
 * @author wangzuoyu1
 * @description
 */
public class RateLimiterRejectException extends RuntimeException{

    public RateLimiterRejectException(String msg) {
        super(msg);
    }

    public RateLimiterRejectException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
