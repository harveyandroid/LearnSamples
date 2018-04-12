package com.harvey.webview;

import android.webkit.JavascriptInterface;

/**
 * Created by hanhui on 2018/4/12 0012 11:10
 */
// 继承自Object类
public class AndroidtoJs extends Object {
    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void hello(String msg) {
        System.out.println(msg);
    }
}
