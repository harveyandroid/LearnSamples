package com.harvey.proxy_dynamic;

/**
 * Created by hanhui on 2017/9/13 0013 15:39
 */

public class RealSubject implements ISubject {

    @Override
    public void request(String msg) {
        System.out.println("From  RealSubject request " + msg);

    }
}
