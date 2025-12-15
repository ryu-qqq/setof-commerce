package com.setof.connectly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCaching
@EnableFeignClients
@SpringBootApplication
public class ConnectlyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectlyApplication.class, args);
    }
}
