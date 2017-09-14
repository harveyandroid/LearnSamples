package com.harvey.singleton;

/**
 * 懒汉创建单例
 * Created by hanhui on 2017/9/12 0012 10:15
 */

public class SingletonTest5 {
    private static SingletonTest5 INSTANCE = null;

    private SingletonTest5() {
    }

    public synchronized static SingletonTest5 getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SingletonTest5();
        }
        return INSTANCE;
    }

}
