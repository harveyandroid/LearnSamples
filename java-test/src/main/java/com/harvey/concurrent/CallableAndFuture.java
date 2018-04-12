package com.harvey.concurrent;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by hanhui on 2017/11/7 0007 11:46
 */

public class CallableAndFuture {
    public static void main(String[] args) {
        CallableAndFuture callableAndFuture = new CallableAndFuture();
        callableAndFuture.testFutureTask();
        callableAndFuture.testExecutorServiceFuture();
        callableAndFuture.testExecutorServiceFutures();
    }

    public void testFutureTask() {
        Callable<Integer> callable = () -> new Random().nextInt(100);
        FutureTask<Integer> future = new FutureTask<>(callable);
        new Thread(future).start();
        try {
            Thread.sleep(5000);// 可能做一些事情
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void testExecutorServiceFuture() {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        Future<Integer> future = threadPool.submit(() -> new Random().nextInt(100));
        try {
            Thread.sleep(5000);// 可能做一些事情
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void testExecutorServiceFutures() {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        //是按照完成的顺序排列的
        CompletionService<Integer> cs = new ExecutorCompletionService<Integer>(threadPool);
        for (int i = 1; i < 5; i++) {
            final int taskID = i;
            cs.submit(() -> taskID);
        }
        // 可能做一些事情
        for (int i = 1; i < 5; i++) {
            try {
                System.out.println(cs.take().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
