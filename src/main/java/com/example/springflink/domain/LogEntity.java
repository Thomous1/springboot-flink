package com.example.springflink.domain;

import com.alibaba.fastjson.JSON;
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

    public static void main(String[] args) {
        LogEntity logEntity = new LogEntity().setCount(1).setUserId(18).setAction("add").setProductId(18);
        System.out.println(JSON.toJSON(logEntity));
    }
}
