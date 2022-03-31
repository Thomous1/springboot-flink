package com.example.springflink.exception;

/**
 * @author wangzuoyu1
 * @description
 */
public class RedisNotReadyException extends IllegalStateException{

    public RedisNotReadyException(String msg) {
        super(msg);
    }

    public RedisNotReadyException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
