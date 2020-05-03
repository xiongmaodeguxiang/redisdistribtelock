package com.xdf.zl.redislock.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @Author:zhanglin
 * @Description:
 * @Date： 2020/5/2 17:24
 */
@Component
public class RedisClient {

    @Autowired
    JedisPool jedisPool;

    public boolean set(String key , String value ,int expire){
        Jedis jedis = jedisPool.getResource();
        try {
            String res = jedis.set(key, value, "NX", "PX", 100);
            return "OK".equals(res);
        }finally {
            if(null != jedis){
                jedis.close();
            }
        }
    }
    public boolean del(String key){
        Jedis jedis = jedisPool.getResource();
        try {
            Long del = jedis.del(key);
            return 1 == del;
        }finally {
            jedis.close();
        }
    }
    public String get(String key){
        Jedis jedis = jedisPool.getResource();
        String s = jedis.get(key);
        jedis.close();
        return s;
    }

    public long incr(String key){
        Jedis jedis = jedisPool.getResource();
        long incr = jedis.incr(key);
        jedis.close();
        return incr;
    }
    public long decr(String key){
        Jedis jedis = jedisPool.getResource();
        long decr = jedis.decr(key);
        jedis.close();
        return decr;
    }
    public boolean eval(String script, List<String> keys,List<String> vals){
        Jedis jedis = jedisPool.getResource();
        long res = (long) jedis.eval(script,keys,vals);
        jedis.close();
        return res == 1;
    }
}
