package com.xdf.zl.redislock.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author:zhanglin
 * @Description:
 * @Date： 2020/5/2 17:25
 */
@Configuration
public class RedisConig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdel;
    @Value("${spring.redis.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.pool.max-wait}")
    private int maxWait;
    @Value("${spring.redis.pool.min-idle}")
    private int minIdel;
    @Value("${spring.redis.timeout}")
    private int timeOut;

    @Bean
    public JedisPool JedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdel);
        config.setMaxWaitMillis(maxWait);
        config.setMinIdle(minIdel);
        config.setMaxTotal(maxActive);
        JedisPool jedisPool = new JedisPool(config, host, port);
        return jedisPool;
    }
}
