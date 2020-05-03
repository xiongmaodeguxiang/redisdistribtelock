package com.xdf.zl.redislock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class RedislistributelockApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedislistributelockApplication.class, args);
    }

}
