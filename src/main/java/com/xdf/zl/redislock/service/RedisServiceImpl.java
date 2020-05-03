package com.xdf.zl.redislock.service;

import com.xdf.zl.redislock.aspect.RedisLock;
import com.xdf.zl.redislock.util.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:zhanglin
 * @Description:
 * @Date： 2020/5/2 17:24
 */
@Service
public class RedisServiceImpl implements IRedisService{
    public static final String REDIS_LOCK_PREFIX = "redis_lock_prefix";

    @Autowired
    RedisClient redisClient;

    private long num ;
    @Override
    @RedisLock(lock_prefix=REDIS_LOCK_PREFIX )
    public Long incr() {
        num ++;
        return num;
    }

    @Override
    public long getNum() {
        return num;
    }

    @Override
    public String getString(String key) {
        String s = redisClient.get(key);
        return s;
    }
}
