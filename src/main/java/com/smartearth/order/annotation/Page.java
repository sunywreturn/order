package com.smartearth.order.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义一个名为 Page 的自定义注解，用于标注方法，表示该方法需要进行分页处理。
 * 该注解在运行时保留，可用于 AOP 或其他运行时处理逻辑。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Page {}
