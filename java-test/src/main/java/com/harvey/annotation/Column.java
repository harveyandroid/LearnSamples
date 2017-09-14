package com.harvey.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hanhui on 2017/9/14 0014 14:18
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    public String name() default "fieldName";

    public String setFuncName() default "setField";

    public String getFuncName() default "getField";

    public boolean defaultDBValue() default false;
}