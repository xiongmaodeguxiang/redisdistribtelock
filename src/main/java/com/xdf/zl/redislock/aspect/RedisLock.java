package com.xdf.zl.redislock.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author:zhanglin
 * @Description:
 * @Date： 2020/5/2 19:18
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {
    String lock_prefix() default "";
    long expire() default 1000 * 60;
    long timeout() default 1000 * 60 * 5;
}
