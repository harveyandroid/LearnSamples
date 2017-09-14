package com.harvey.proxy;

/**
 * Created by hanhui on 2017/9/13 0013 15:39
 */

public class RealSubject implements ISubject {

    @Override
    public void request() {
        System.out.println("From  RealSubject");

    }
}
