package com.harvey.concurrent;

import java.util.concurrent.locks.Lock;

/**
 * Created by hanhui on 2018/6/13 0013 16:41
 */

public class ThreadLocalTest {

	static ThreadLocal<Integer> seqnum = ThreadLocal.withInitial(() -> 0);
	public static void main(String[] args) {
		ThreadLocalTest test = new ThreadLocalTest();
		TextClient client1 = new TextClient(test);
		TextClient client2 = new TextClient(test);
		TextClient client3 = new TextClient(test);
		client1.start();
		client2.start();
		client3.start();

	}
	public int getNextNum() {
		seqnum.set(seqnum.get() + 1);
		return seqnum.get();
	}

	static class TextClient extends Thread {
		ThreadLocalTest test;
		public TextClient(ThreadLocalTest t) {
			test = t;
		}
		@Override
		public void run() {
			for (int i = 0; i < 3; i++) {
				System.out.println("thread:" + Thread.currentThread().getName() + ",sum=" + test.getNextNum());
			}
		}
	}
}
