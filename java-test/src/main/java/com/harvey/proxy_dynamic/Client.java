package com.harvey.proxy_dynamic;

/**
 * 1。代理对象，不需要实现接口
 * 2。代理对象的生成，是利用JDK的API，动态的在内存中构建代理对象(需要我们指定创建代理对象/目标对象实现的接口的类型)
 * Created by hanhui on 2017/9/13 0013 15:39
 */

public class Client {

    public static void main(String[] args) {
        ISubject subject = (ISubject) ProxySubject.newProxyInstance(new RealSubject());
        subject.request("parameter passing");

        ISubject2 subject2 = (ISubject2) ProxySubject.newProxyInstance(new RealSubject2());
        subject2.update();
    }
}
