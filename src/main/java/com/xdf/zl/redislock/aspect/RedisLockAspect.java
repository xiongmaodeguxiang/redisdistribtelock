package com.xdf.zl.redislock.aspect;

import com.xdf.zl.redislock.util.RedisClient;
import com.xdf.zl.redislock.util.RedisLockUtil;
import com.xdf.zl.redislock.util.RedisLockUtil2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author:zhanglin
 * @Description:
 * @Date： 2020/5/2 19:22
 */
@EnableAspectJAutoProxy
@Aspect
@Component
public class RedisLockAspect {
    @Autowired
    RedisLockUtil redisLockUtil;

    @Autowired
    RedisLockUtil2 redisLockUtil2;

    @Pointcut("@annotation(RedisLock)")
    public void point(){}

    @Around("point()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //判断方法上是否有存在注解
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        RedisLock lock = method.getAnnotation(RedisLock.class);
        long expire = lock.expire();
        long timeout = lock.timeout();
        String lock_prefix = lock.lock_prefix();
        boolean success = redisLockUtil2.lock(lock_prefix,expire,timeout);
        System.out.println(Thread.currentThread().getName()+"获取到了锁");
        Object object = null;
        try {
            if(success){
                object = joinPoint.proceed();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            redisLockUtil2.unlock(lock_prefix);
            System.out.println(Thread.currentThread().getName()+"释放了锁");
        }
        return object;
    }
}
