package com.harvey.rxjava;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 它的作用是对发射时间发送的每一个事件应用一个函数，是的每一个事件都按照指定的函数去变化
 * map 基本作用就是将一个 Observable 通过某种函数关系，转换为另一种 Observable
 * Created by hanhui on 2018/4/16 0016 09:53
 */

public class MapOperatorTest {
    public static void main(String[] args) {
        Observable.just(1, 2, 3, 4).map(new Function<Integer, String>() {

            @Override
            public String apply(@NonNull Integer integer) throws Exception {
                return "map result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String string) throws Exception {
                System.out.println("---map--accept value:" + string);

            }
        });
    }
}
