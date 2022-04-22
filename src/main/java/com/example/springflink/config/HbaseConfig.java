package com.example.springflink.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangzuoyu1
 * @description
 */
@ConfigurationProperties(prefix = "hbase")
@Component
@Data
public class HbaseConfig {
    private String rootdir;
    private Zookeeper zookeeper;
    private Client client;
    private Rpc rpc;

    @Data
    public class Zookeeper{
       private String quorum;
    }

    @Data
    public class Client{
        private int period;
    }

    @Data
    public class Rpc{
        private int timeout;
    }
}
