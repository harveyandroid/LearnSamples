package com.harvey.rxjava;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * zip 专用于合并事件，该合并不是连接（连接操作符后面会说），而是两两配对，也就意味着，最终配对出的 Observable 发射事件数目只和少的那个相同。
 * Created by hanhui on 2018/4/16 0016 09:53
 */

public class ZipOperatorTest {
    public static void main(String[] args) {
        Observable.zip(Observable.just(1, 2, 3, 6), Observable.just(4, 5, 7), new BiFunction<Integer, Integer, String>() {
            @Override
            public String apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                return "zip result :" + (integer + integer2);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                System.out.println("zip  accept:  " + s + "\n");
            }
        });
    }
}
