package com.example.demo.annotation;

import java.lang.annotation.*;

/**
 * 加密注解
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encrypt {

	String value() default "";
	
}
