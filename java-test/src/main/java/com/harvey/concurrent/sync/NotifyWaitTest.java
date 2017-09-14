package com.harvey.concurrent.sync;

/**
 * Created by hanhui on 2017/7/26 0026 09:36
 */

public class NotifyWaitTest {
    public static Class lock = NotifyWaitTest.class;

    public static void main(String[] args) throws Exception {
        new TestThread1().start();
        new TestThread1().start();
//        new TestThread2().start();
//        new TestThread2().start();
        Thread.sleep(3000);
        synchronized (NotifyWaitTest.lock) {
            try {
                System.out.println(Thread.currentThread().getName() + " sent notification 1");
                NotifyWaitTest.lock.notify();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Thread.sleep(3000);
        synchronized (NotifyWaitTest.lock) {
            try {
                System.out.println(Thread.currentThread().getName() + " sent notification 2");
                NotifyWaitTest.lock.notify();
                System.out.println(Thread.currentThread().getName() + " sent notification over");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

class TestThread1 extends Thread {
    public void run() {
        synchronized (NotifyWaitTest.lock) {
            try {
                System.out.println(Thread.currentThread().getName() + " wait for notification");
                NotifyWaitTest.lock.wait();

                System.out.println(Thread.currentThread().getName() + " wake up");
                for (int i = 0; i < 3; i++) {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " doing " + i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " finished");
        }
    }
}

class TestThread2 extends Thread {

    public void run() {
        synchronized (NotifyWaitTest.lock) {
            System.out.println(Thread.currentThread().getName() + " wait for notification");
            try {
                NotifyWaitTest.lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " wake up");
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " doing " + i);
        }
    }
}