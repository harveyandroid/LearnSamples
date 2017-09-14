package com.harvey.singleton;

/**
 * 饿汉创建单例
 * Created by hanhui on 2017/9/12 0012 10:15
 */

public class SingletonTest4 {
    private static SingletonTest4 INSTANCE = new SingletonTest4();

    private SingletonTest4() {
    }

    public static SingletonTest4 getInstance() {
        return INSTANCE;
    }

}
