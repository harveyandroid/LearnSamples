package com.harvey.rxjava;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

/**
 * 每次订阅都会创建一个新的 Observable，并且如果没有被订阅，就不会产生新的 Observable。
 * Created by hanhui on 2018/4/13 0013 14:26
 */

public class DeferOperatorTest {
    static int test = 10;

    public static void main(String[] args) {
        Observable observable = Observable.defer(new Callable<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> call() throws Exception {
                System.out.println("---defer--ObservableSource:"+ Thread.currentThread());
                return Observable.just("just result" + test);
            }
        });
        test = 20;
        observable.doOnNext(
                integer -> System.out.println("---doOnNext--accept:" + integer+ Thread.currentThread()))
                .map(integer -> "map 转换" + integer)
                .subscribe(s -> System.out.println("---subscribe--accept:" + s+ Thread.currentThread()));

    }
}
