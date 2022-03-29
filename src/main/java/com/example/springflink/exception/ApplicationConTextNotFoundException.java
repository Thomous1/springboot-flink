package com.example.springflink.exception;

/**
 * @author wangzuoyu1
 * @description 上下文异常
 */
public class ApplicationConTextNotFoundException extends RuntimeException{

    public ApplicationConTextNotFoundException(String msg) {
        super(msg);
    }

    public ApplicationConTextNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
