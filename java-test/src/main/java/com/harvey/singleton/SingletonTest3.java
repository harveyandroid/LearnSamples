package com.harvey.singleton;

/**
 * 双重校验锁创建单例
 * Created by hanhui on 2017/9/12 0012 10:15
 */

public class SingletonTest3 {
    private static SingletonTest3 instance;

    private SingletonTest3() {
    }

    public static SingletonTest3 getInstance() {
        if (instance == null) {
            synchronized (SingletonTest3.class) {
                if (instance == null) instance = new SingletonTest3();
            }
        }
        return instance;
    }

}
