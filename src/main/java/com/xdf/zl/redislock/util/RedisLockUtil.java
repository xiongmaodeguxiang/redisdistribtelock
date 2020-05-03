package com.xdf.zl.redislock.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author:zhanglin
 * @Description:
 * @Date： 2020/5/2 18:59
 */
@Component
public class RedisLockUtil {
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
            flag = redisClient.set(key, thread_num, 1000 * 60 * 5);
            if(flag){
                redisClient.incr(key + "_count");
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
        String val = redisClient.get(key);
        if(StringUtils.isEmpty(val)){
            return;
        }
        String thread_num = threalLocal.get();
        if(thread_num.equals(val) ){//当前线程释放锁
            long decr = redisClient.decr(key+"_count");
            System.out.println(Thread.currentThread().getName()+"释放了锁："+decr);
            if(decr == 0){//注意一定要在删除了key_counth后再删除key对应的值，因为删除了key意味着就释放了锁
                System.out.println(Thread.currentThread().getName()+"变成了0进行删除锁");
                redisClient.del(key+"_count");
                redisClient.del(key);
            }
        }
    }

}
