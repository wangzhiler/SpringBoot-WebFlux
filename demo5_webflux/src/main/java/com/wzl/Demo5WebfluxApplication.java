package com.wzl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class Demo5WebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(Demo5WebfluxApplication.class, args);
    }
}
