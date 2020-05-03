package com.xdf.zl.redislock.service;

/**
 * @Author:zhanglin
 * @Description:
 * @Date： 2020/5/2 17:24
 */
public interface IRedisService {

    public Long incr();

    public long getNum();

    public String getString(String key);
}
