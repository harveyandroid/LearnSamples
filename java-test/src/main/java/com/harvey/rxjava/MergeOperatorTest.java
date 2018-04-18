package com.harvey.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * merge 的作用是把多个 Observable 结合起来，接受可变参数，也支持迭代器集合。
 * 例如：一组数据来自网络，一组数据来自文件，需要合并两组数据一起展示。
 * 注意它和 concat 的区别在于，不用等到 发射器 A 发送完所有的事件再进行发射器 B 的发送。
 * 相当于 concat 每个发射器发送完后调用complete
 * Created by hanhui on 2018/4/13 0013 16:38
 */

public class MergeOperatorTest {
    public static void main(String[] args) {
        Observable observable1 = Observable.just(5, 9, 6, 8);
        Observable observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(10);
                e.onNext(11);
                e.onNext(13);
                //对比concat 此处不需要调用complete
            }
        });

        Observable observable3 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(4);
                //对比concat 此处不需要调用complete
            }
        });
        Observable.merge(observable1, observable2, observable3).doOnNext(new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                throw new Exception("测试异常");
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println("---merge--accept:" + integer);
            }
        }, new Consumer<ServerException>() {
            @Override
            public void accept(@NonNull ServerException throwable) throws Exception {
                System.out.println("---merge--accept:Throwable" + throwable.getMessage());

            }
        });
    }
}
