package com.harvey.rxjava;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * reduce 操作符每次用一个方法处理一个值，可以有一个 seed 作为初始值。
 * Created by hanhui on 2018/4/16 0016 09:20
 */

public class ReduceOperatorTest {

    public static void main(String[] args) {
        Observable.just(1, 2, 6, 9, 3, 2, 3)
                .reduce(100,new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println("just  accept: reduce : " + integer + "\n");
            }
        });
    }
}
