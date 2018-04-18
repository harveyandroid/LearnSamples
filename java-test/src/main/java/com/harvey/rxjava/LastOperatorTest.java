package com.harvey.rxjava;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 操作符仅取出可观察到的最后一个值，或者是满足某些条件的最后一项。
 * Created by hanhui on 2018/4/13 0013 16:24
 */

public class LastOperatorTest {
    public static void main(String[] args) {
        Observable.just(4, 5).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println("---doOnNext--accept:" + integer++);
            }
        }).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(@NonNull Integer integer) throws Exception {
                System.out.println("---map--apply:" + integer);
                return integer + 2;
            }
        }).last(5).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println("---last--accept:" + integer);
            }
        });
    }
}
