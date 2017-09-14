package com.harvey.proxy;

/**
 * Created by hanhui on 2017/9/13 0013 15:39
 */

public class ProxySubject implements ISubject {
    ISubject realSubject;

    public ProxySubject(ISubject realSubject) {
        this.realSubject = realSubject;
    }

    @Override
    public void request() {
        System.out.println("before do something");
        realSubject.request();
        System.out.println("after do something");
    }
}
