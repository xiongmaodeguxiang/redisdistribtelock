package com.xdf.zl.redislock.controller;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.xdf.zl.redislock.util.RedisLockUtil2;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author:zhanglin
 * @Description:
 * @Date： 2020/5/2 18:55
 */
public class TestMain {

    public static void main(String[] args) throws IOException {
        Jedis jedis = new Jedis();
//        String script = RedisLockUtil2.getScript("/luaScript/lock.lua");
//        List<String> keyList = new ArrayList<>();
//        keyList.add("sss1");
//        keyList.add("sss1_count");
//        List<String> valList = new ArrayList<>();
//        valList.add("sss_value");
//        valList.add("1000");
//        long eval = (long) jedis.eval(script, keyList, valList);
//        String script = RedisLockUtil2.getScript("/luaScript/unlock.lua");
        List<String> keyList = new ArrayList<>();
        keyList.add("sss1");
        keyList.add("sss1_count");
        keyList.add("sss1_count1");
        keyList.add("sss1_count2");
        keyList.add("sss1_count3");
//        List<String> valList = new ArrayList<>();
//        valList.add("sss_value");
//        long eval = (long) jedis.eval(script, keyList, valList);
//        System.out.println(eval);
        keyList.forEach(item -> System.out.println(item));
    }
}
