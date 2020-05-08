package com.xdf.zl.redislock.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author:zhanglin
 * @Description: 基于jedis的执行lua脚本实现分布锁
 * @Date： 2020/5/3 14:04
 */
@Component
public class RedisLockUtil2 {
    private static final ThreadLocal<String> threalLocal = new ThreadLocal<>();

    @Autowired
    RedisClient redisClient;
    /**
     * redis分布式加锁
     * @param key
     * @param expire
     * @param timeout
     * @return
     */
    public boolean lock(String key , long expire , long timeout){
        long now = System.currentTimeMillis();
        long expireAt = now + timeout;
        String thread_num = threalLocal.get();
        if(StringUtils.isEmpty(thread_num)){
            thread_num  = UUID.randomUUID().toString().replaceAll("-","");
            threalLocal.set(thread_num);
        }
        System.out.println(Thread.currentThread().getName()+"----"+thread_num);
        boolean flag = false;
        //判断是否当前线程拥有锁如果是则不用重新获取
        String lock_thread = redisClient.get(key);
        if(!StringUtils.isEmpty(lock_thread) && thread_num.equals(lock_thread)){
            long incr = redisClient.incr(key + "_count");
            System.out.println(Thread.currentThread().getName()+"重入了锁："+incr);
            return true;
        }
        while(now < expireAt){
            String script = null;
            try {
                script = getScript("/luaScript/lock.lua");
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> keyList = new ArrayList<>();
            keyList.add(key);
            keyList.add(key+"_count");
            List<String> args = new ArrayList<>();
            args.add(thread_num);
            args.add(300000+"");
            flag = redisClient.eval(script, keyList,args);
            if(flag){
                break;
            }
            try {
                TimeUnit.MICROSECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread.yield();
        }
        return flag;
    }

    public void unlock(String key){
        String thread_num = threalLocal.get();
        String script = null;
        try {
            script = getScript("/luaScript/unlock.lua");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> keylist = new ArrayList<>();
        keylist.add(key);
        keylist.add(key+"_count");
        List<String> argList = new ArrayList<>();
        argList.add(thread_num);
        redisClient.eval(script, keylist, argList);
    }

    public static String getScript(String path) throws IOException {
        InputStream in = RedisLockUtil2.class.getResourceAsStream(path);
        byte[] buffer = new byte[1024];
        StringBuilder str = new StringBuilder();
        int byteRead = 0;
        while((byteRead = in.read(buffer)) != -1){
            String s = new String(buffer, 0, byteRead);
            str.append(s);
        }
        return str.toString();
    }

}
