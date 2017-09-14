package com.harvey.concurrent;

/**
 * volatile是无法保证原子性的（否则结果应该是1000）。原因也很简单，i++其实是一个复合操作，包括三步骤：
 * <p>
 * 　　（1）读取i的值。
 * <p>
 * 　　（2）对i加1。
 * <p>
 * 　　（3）将i的值写回内存。
 * <p>
 * volatile是无法保证这三个操作是具有原子性的
 * 我们可以通过AtomicInteger或者Synchronized来保证+1操作的原子性
 * Created by hanhui on 2017/9/14 0014 11:48
 */


public class VolatileTest01 {
    volatile int i;

    public static void main(String[] args) throws InterruptedException {
        final VolatileTest01 test01 = new VolatileTest01();
        for (int n = 0; n < 1000; n++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    test01.addI();
                }
            }).start();
        }

        Thread.sleep(10000);//等待10秒，保证上面程序执行完成

        System.out.println(test01.i);
    }

    public synchronized void addI() {
        i++;
    }
}