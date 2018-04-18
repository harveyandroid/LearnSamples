package com.harvey.rxjava;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 顾名思义，Single 只会接收一个参数，而 SingleObserver 只会调用 onError() 或者 onSuccess()。
 * Created by hanhui on 2018/4/13 0013 15:27
 */

public class SingleOperatorTest {

    public static void main(String[] args) {
        Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Integer> e) throws Exception {
                Thread.sleep(1000);
                e.onSuccess(111);
                System.out.println("---create--onSubscribe:" + Thread.currentThread());

            }
        }).observeOn(Schedulers.io()).map(new Function<Integer, String>() {
            @Override
            public String apply(@NonNull Integer integer) throws Exception {
                System.out.println("---map--apply:" + Thread.currentThread());
                return "map " + integer;
            }
        }).subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("---subscribe--onSubscribe:" + Thread.currentThread());

            }

            @Override
            public void onSuccess(@NonNull String string) {
                System.out.println("---subscribe--onSuccess:" + string + Thread.currentThread());

            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("---Single--onError:" + e.getMessage());

            }
        });
    }
}
