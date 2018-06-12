package com.harvey.collection;

import java.util.Iterator;
import java.util.Stack;

/**
 * 栈 后进先出 Created by hanhui on 2018/6/11 0011 14:21
 */

public class StackTest {

	public static void main(String[] args) {
		testStack();
		testStackQueue();
	}

	public static void testStack() {
		Stack<Integer> stacks = new Stack<>();

		stacks.push(1);
		stacks.push(2);
		stacks.push(3);
		stacks.push(4);
		stacks.push(5);
		stacks.push(6);

		while (!stacks.empty()) {
			System.out.println("Stack=" + stacks.pop());
		}
	}

	public static void testStackQueue() {
		StackQueue1<Integer> stackQueue = new StackQueue1();
		stackQueue.offer(1);
		stackQueue.offer(2);
		stackQueue.offer(3);
		stackQueue.offer(4);
		stackQueue.offer(5);
		stackQueue.offer(6);
		while (!stackQueue.empty()) {
			System.out.println("StackQueue=" + stackQueue.poll());
		}
	}

	public static void printStack(Stack stacks) {
		Iterator it = stacks.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

	/**
	 * 用两个栈来实现一个队列 先进先出
	 * 
	 * @param <T>
	 */
	static class StackQueue<T> {

		Stack<T> stack1 = new Stack<>();

		Stack<T> stack2 = new Stack<>();

		public T poll() {
			return stack2.pop();
		}

		public void offer(T t) {

			while (!stack2.empty()) {
				stack1.push(stack2.pop());
			}

			stack1.push(t);// 1,2

			while (!stack1.empty()) {
				stack2.push(stack1.pop());
			}
		}

		public boolean empty() {
			return stack2.empty();
		}

	}

	static class StackQueue1<T> {

		Stack<T> stack1 = new Stack<>();

		Stack<T> stack2 = new Stack<>();

		public T poll() {

			while (!stack1.empty()) {
				stack2.push(stack1.pop());
			}

			T t = stack2.pop();

			while (!stack2.empty()) {
				stack1.push(stack2.pop());
			}

			return t;
		}

		public void offer(T t) {
			stack1.push(t);
		}

		public boolean empty() {
			return stack1.empty();
		}

	}

}
