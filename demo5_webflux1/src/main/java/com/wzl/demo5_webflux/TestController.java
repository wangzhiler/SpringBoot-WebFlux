package com.wzl.demo5_webflux;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TestController {

    //老写法
    @GetMapping("/1")
    public String get1() {
        return "some string";
    }

    //WebFlux写法
    @GetMapping("/2")
    public Mono<String> get2() {
        return Mono.just("some string");
    }

}
