package com.example.springflink.task.log;

import com.example.springflink.domain.LogEntity;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;


/**
 * @author wangzuoyu1
 * @description
 */

@Component
public class ActionManager {

    private Map<String, Action> targets = new HashMap<>();
    public ActionManager() {
        targets.put("nothing", new NothingAction());
        targets.put("add", new AddAction());
        targets.put("del", new DelAction());
        targets.put("change", new ChangeAction());
    }

    public void execute(LogEntity logEntity, Jedis redisClient) {
        targets.get(logEntity.getAction()).execute(logEntity, redisClient);
    }
}
