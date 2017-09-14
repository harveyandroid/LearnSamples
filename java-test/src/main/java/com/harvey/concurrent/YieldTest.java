package com.harvey.concurrent;

/**
 * Created by hanhui on 2017/9/14 0014 11:06
 */
public class YieldTest implements Runnable {
    public static void main(String[] args) {
        YieldTest runn = new YieldTest();
        Thread t1 = new Thread(runn, "FirstThread");
        Thread t2 = new Thread(runn, "SecondThread");

        t1.start();
        t2.start();

    }

    @Override
    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
            Thread.yield();
        }
    }
}
