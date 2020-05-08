package com.xdf.zl.redislock.controller;

import com.xdf.zl.redislock.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Retention;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author:zhanglin
 * @Description:
 * @Date： 2020/5/2 19:42
 */
@RestController
public class RedisLockController {
    @Autowired
    IRedisService redisService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @GetMapping("/incr")
    public Object incr() throws InterruptedException {
        long start =System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(100);
        for(int k = 0; k < 100; k++){
            new Thread(()->{
                int i = 0;
                while ( i < 100){
                    redisService.incr();
                    i++;
                }
                latch.countDown();
            } ).start();
        }
        latch.await();
        long num = redisService.getNum();
        long end =System.currentTimeMillis();
        System.out.println("得到结果："+num+",耗时为："+(end - start));
        return num;
    }
    @GetMapping("/get")
    public Object get() throws InterruptedException {
        String num_1 = redisService.getString("num_1");
        return num_1;
    }
    @GetMapping("/get/{key}/{val}")
    public Object setval(@PathVariable("key") String key,@PathVariable("val")String val) throws InterruptedException {
//        redisTemplate.opsForValue().set(key,val);
        stringRedisTemplate.opsForValue().set(key,val);
        return "设置成功："+key+"---"+val;
    }
}
