package com.harvey.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by hanhui on 2017/9/14 0014 14:13
 */
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * 数据表名称注解，默认值为类名称
     *
     * @return
     */
    public String tableName() default "className";
}