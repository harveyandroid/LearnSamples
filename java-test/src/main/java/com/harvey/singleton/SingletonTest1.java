package com.harvey.singleton;

import java.util.Arrays;

/**
 * 枚举创建单例
 * Created by hanhui on 2017/9/12 0012 10:15
 */

public enum SingletonTest1 {
    INSTANCE;

    private final String[] favoriteSongs =
            {"Hound Dog", "Heartbreak Hotel"};

    public static SingletonTest1 getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        SingletonTest1.getInstance().printFavorites();
    }

    public void printFavorites() {
        System.out.println(Arrays.toString(favoriteSongs));
    }
}
