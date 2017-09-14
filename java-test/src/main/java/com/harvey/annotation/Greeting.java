package com.harvey.annotation;


import java.lang.annotation.Inherited;

/**
 * Created by hanhui on 2017/9/14 0014 14:19
 */

@Inherited
public @interface Greeting {
    String name();

    FontColor fontColor() default FontColor.GREEN;

    public enum FontColor {BULE, RED, GREEN}
}
