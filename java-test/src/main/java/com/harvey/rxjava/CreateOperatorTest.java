package com.harvey.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * create 操作符应该是最常见的操作符了，主要用于产生一个 Obserable 被观察者对象，
 * Created by hanhui on 2018/4/16 0016 09:20
 */

public class CreateOperatorTest {

    public static void main(String[] args) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {

                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onNext(4);
                e.onNext(5);
                e.onNext(6);
                //直接调用了 e.onComplete()，虽然无法接收事件，但发送事件还是继续的。
                e.onComplete();
                e.onNext(7);
            }
        }).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                throw new Exception("测试异常");
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("---create--onSubscribe ");
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                if (integer == 4) {
                    //Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
                    mDisposable.dispose();
                }
                System.out.println("---create--onNext value:" + integer);
                System.out.println("---create--onNext isDisposed:" + mDisposable.isDisposed());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("---create--onError " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("---create--onComplete ");

            }
        });
    }
}
