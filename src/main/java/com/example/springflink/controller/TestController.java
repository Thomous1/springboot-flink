package com.example.springflink.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author wangzuoyu1
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/get/{name}")
    public Mono<String> test(@PathVariable("name") String name) {
        return Mono.just(name);
    }
}
