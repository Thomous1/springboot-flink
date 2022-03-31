package com.example.springflink.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wangzuoyu1
 * @description
 */

@Data
@AllArgsConstructor
public class Response {
    private final boolean allowed; // 是否被限流
    private final long tokenRemaining; // 剩余令牌桶数量
}
