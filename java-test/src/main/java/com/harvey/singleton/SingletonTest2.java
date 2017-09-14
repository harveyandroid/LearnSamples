package com.harvey.singleton;

/**
 * 静态内部类创建单例
 * Created by hanhui on 2017/9/12 0012 10:15
 */

public class SingletonTest2 {
    private SingletonTest2() {
        if (SingletonHolder.INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static SingletonTest2 getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        public static SingletonTest2 INSTANCE = new SingletonTest2();
    }
}
