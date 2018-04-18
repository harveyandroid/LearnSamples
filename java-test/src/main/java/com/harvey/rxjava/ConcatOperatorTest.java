package com.harvey.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * concat 对于单一的把两个发射器连接成一个发射器
 * 注意: 上一个数据源不可用或者结束，请务必subscriber.onCompleted(); 这样才能继续走下一个数据源。
 * 例如：依次检查memory、disk和network中是否存在数据，任何一步一旦发现数据后面的操作都不执行。
 * Created by hanhui on 2018/4/13 0013 16:38
 */

public class ConcatOperatorTest {
    public static void main(String[] args) {
        Observable observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(10);
                e.onNext(11);
                e.onNext(13);
                //如果没有调用onComplete 后面的不执行
                e.onComplete();
            }
        });

        Observable observable3 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(4);
                e.onComplete();
            }
        });
        Observable.concat(observable2, observable3).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println("---concat--accept:" + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                System.out.println("---concat--accept:Throwable" + throwable.getMessage());

            }
        });
    }
}
