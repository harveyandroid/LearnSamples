package com.harvey.proxy_dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Created by hanhui on 2017/9/13 0013 15:39
 */

public class ProxySubject implements InvocationHandler {
    Object subject;

    private ProxySubject(Object subject) {
        this.subject = subject;
    }

    public static Object newProxyInstance(Object subject) {
        /**
         *ClassLoader loader：指定当前目标对象使用类加载器，获取加载器的方法是固定的
         *Class<?>[] interfaces：目标对象实现的接口的类型，使用泛型方式确认类型
         * InvocationHandler h：事件处理，执行目标对象的方法时，会触发事件处理器的方法，会把当前执行目标对象的方法作为参数传入
         */
        return Proxy.newProxyInstance(subject.getClass().getClassLoader(), subject
                .getClass().getInterfaces(), new ProxySubject(subject));
    }

    /**
     * @param proxy  代理类
     * @param method 被代理的方法
     * @param args   该方法的参数数组（无参时设置为null）
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before do something");
        System.out.println("proxy:" + proxy.getClass());
        System.out.println("Method:" + method);
        System.out.println("args:" + Arrays.toString(args));
        //当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
        method.invoke(subject, args);
        System.out.println("after do something");
        return null;
    }
}
