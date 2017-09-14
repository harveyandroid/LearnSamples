package com.harvey.proxy_dynamic;

/**
 * Created by hanhui on 2017/9/13 0013 15:39
 */

public class RealSubject2 implements ISubject2 {

    @Override
    public void update() {
        System.out.println("From  RealSubject2 update");

    }
}
