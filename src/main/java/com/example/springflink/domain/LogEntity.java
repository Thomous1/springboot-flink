package com.example.springflink.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangzuoyu1
 * @description kafka log 实体
 */

@Data
@Accessors(chain = true)
public class LogEntity {

    private int userId;
    private int productId;
    private Long time;
    private String action;
    private int count;
}
