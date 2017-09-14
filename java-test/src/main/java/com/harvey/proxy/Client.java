package com.harvey.proxy;

/**
 * Created by hanhui on 2017/9/13 0013 15:39
 */

public class Client {

    public static void main(String[] args) {
        RealSubject realSubject = new RealSubject();
        ProxySubject proxySubject = new ProxySubject(realSubject);
        proxySubject.request();
    }
}
