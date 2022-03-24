package com.example.demo.annotation;

import java.lang.annotation.*;

/**
 * 数据权限自定义注解
 * @author 李佳其
 * @date 2022-03-14
 */
@Inherited
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {

    // 权限的类型
    String type() default "";

    // 限制的字段
    String column() default "";

}