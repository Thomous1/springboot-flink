package com.example.springflink;

import com.example.springflink.task.log.LogTask;
import com.example.springflink.task.log.LogTaskWindow;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.example.springflink")
public class SpringFlinkApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringFlinkApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //LogTask.main(args);
        LogTaskWindow.main(args);
    }

}
