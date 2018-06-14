package com.harvey.concurrent;

/**
 * 对volatile变量的单次读/写操作可以保证原子性的，如long和double类型变量，但是并不能保证i++这种操作的原子性，因为本质上i++是读、写两次操作
 * 可见性问题主要指一个线程修改了共享变量值，而另一个线程却看不到。
 * 引起可见性问题的主要原因是每个线程拥有自己的一个高速缓存区——线程工作内存。
 * volatile关键字能有效的解决这个问题，我们看下下面的例子，就可以知道其作用：
 * 　在前文中已经提及过，线程本身并不直接与主内存进行数据的交互，
 * 而是通过线程的工作内存来完成相应的操作。这也是导致线程间数据不可见的本质原因
 * 。因此要实现volatile变量的可见性，直接从这方面入手即可。对volatile变量的写操作与普通变量的主要区别有两点：
 * <p>
 * 　　（1）修改volatile变量时会强制将修改后的值刷新的主内存中。
 * <p>
 * 　　（2）修改volatile变量后会导致其他线程工作内存中对应的变量值失效。因此，再读取该变量值的时候就需要重新从读取主内存中的值。
 */
public class VolatileTest {
    volatile int a = 1;
    volatile int b = 2;

    public static void main(String[] args) {
        while (true) {
            final VolatileTest test = new VolatileTest();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    test.change();
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    test.print();
                }
            }).start();

        }
    }

    public void change() {
        a = 3;
        b = a;
    }

    public void print() {
        System.out.println("b=" + b + ";a=" + a);
    }
}