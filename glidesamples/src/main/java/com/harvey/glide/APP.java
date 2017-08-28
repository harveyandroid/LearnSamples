package com.harvey.glide;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanhui on 2017/7/11 0011 14:50
 */

public class APP extends Application {

    public static List testList = new ArrayList();

    static {
        System.out.println("初始化Application");
    }

    public static void addData() {
        testList.add(1);
    }

    public static List getData() {
        return testList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
