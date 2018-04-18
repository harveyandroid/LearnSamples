package com.harvey.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * onErrorResumeNext()	指示Observable在遇到错误时发射一个数据序列
 * onErrorReturn()	让Observable遇到错误时发射一个特殊的项并且正常终止。方法返回一个镜像原有Observable行为的新Observable，后者会忽略前者的onError调用，不会将错误传递给观察者，作为替代，它会发发射一个特殊的项并调用观察者的onCompleted方法。
 * onExceptionResumeNext()	指示Observable遇到错误时继续发射数据
 * retry()	指示Observable遇到错误时重试
 * retryWhen()	指示Observable遇到错误时，将错误传递给另一个Observable来决定是否要重新给订阅这个Observable
 * retryUntil()	指示Observable遇到错误时，是否让Observable重新订阅
 * Created by hanhui on 2018/4/18 0018 16:18
 */

public class ErrorOperatorTest {

    public static void main(String[] args) {
        testRetryWhen();
//        testOnErrorReturn();
//        testOnExceptionResumeNext();
//        testOnErrorResumeNext();
    }

    /**
     * - retry(): 让被观察者重新发射数据，要是一直错误就一直发送了
     * - retry(BiPredicate)： interger是第几次重新发送，Throwable是错误的内容
     * - retry(long time): 最多让被观察者重新发射数据多少次
     * - retry(long time,Predicate predicate)： 最多让被观察者重新发射数据多少次，在predicate里面进行判断拦截 返回是否继续
     * - retry(Predicate predicate)： 在predicate里面进行判断拦截 返回是否继续
     */
    public static void testRetry() {
        Observable.just("发送1", "发送5", "", "发送7", "发送8").flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull String s) throws Exception {
                if (s == null || s.length() == 0) {
                    return Observable.error(new NullPointerException("发生错误了!"));
                }
                return Observable.just(s);
            }
        }).retry(new BiPredicate<Integer, Throwable>() {
            @Override
            public boolean test(@NonNull Integer integer, @NonNull Throwable throwable) throws Exception {
                System.out.println("--Retry---test: value:" + integer + throwable.getMessage());
                if (integer == 3)
                    return false;
                return true;
                //返回false就是不让重新发射数据了，调用观察者的onError就终止了。
                //返回true就是让被观察者重新发射请求
            }
        }).subscribeWith(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("--Retry---onSubscribe:");
            }

            @Override
            public void onNext(@NonNull String string) {
                System.out.println("--Retry---onNext:" + string);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("--Retry---onError:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("--Retry---onComplete:");
            }
        });
    }

    /**
     * : retryWhen将onError中的Throwable传递给一个函数，这个函数产生另一个Observable，
     * retryWhen观察它的结果再决定是不是要重新订阅原始的Observable。
     * 如果这个Observable发射了一项数据，它就重新订阅，
     * 如果这个Observable发射的是onError通知，它就将这个通知传递给观察者然后终止。
     */
    public static void testRetryWhen() {
        Observable.just("发送1", "发送5", "", "发送7", "发送8").flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull String s) throws Exception {
                if (s == null || s.length() == 0) {
                    return Observable.error(new NullPointerException("发生错误了!"));
                }
                return Observable.just(s);
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                        return Observable.error(new Throwable("retryWhen终止啦"));
                    }
                });
            }
        }).subscribeWith(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("--Retry---onSubscribe:");
            }

            @Override
            public void onNext(@NonNull String string) {
                System.out.println("--Retry---onNext:" + string);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("--Retry---onError:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("--Retry---onComplete:");
            }
        });
    }

    public static void testOnErrorReturn() {
        Observable.just("发送1", "发送5", "", "发送7", "发送8").flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull String s) throws Exception {
                if (s == null || s.length() == 0) {
                    return Observable.error(new NullPointerException("发生错误了!"));
                }
                return Observable.just(s);
            }
        }).onErrorReturn(new Function<Throwable, String>() {
            @Override
            public String apply(@NonNull Throwable throwable) throws Exception {
                return "错误后重新发送";
            }
        }).subscribeWith(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("--onErrorReturn---onSubscribe:");
            }

            @Override
            public void onNext(@NonNull String string) {
                System.out.println("--onErrorReturn---onNext:" + string);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("--onErrorReturn---onError:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("--onErrorReturn---onComplete:");
            }
        });
    }

    /**
     * 只能拦截Exception
     */
    public static void testOnExceptionResumeNext() {
        Observable.just("发送1", "发送5", "", "发送7", "发送8").flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull String s) throws Exception {
                if (s == null || s.length() == 0) {
                    return Observable.error(new NullPointerException("发生错误了!"));
                }
                return Observable.just(s);
            }
        }).onExceptionResumeNext(new Observable<String>() {
            @Override
            protected void subscribeActual(Observer<? super String> observer) {
                observer.onNext("错误后重新发送");
            }
        }).subscribeWith(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("--onExceptionResumeNext---onSubscribe:");
            }

            @Override
            public void onNext(@NonNull String string) {
                System.out.println("--onExceptionResumeNext---onNext:" + string);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("--onExceptionResumeNext---onError:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("--onExceptionResumeNext---onComplete:");
            }
        });
    }


    /**
     * onErrorResumeNext是返回一个重新定义的Observable
     * ps: onErrorResumeNext 都可以拦截
     */
    public static void testOnErrorResumeNext() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("发送" + 1);
                e.onNext("发送" + 2);
                e.onError(new Throwable("发生错误了"));
            }
        }).onErrorResumeNext(new Observable<String>() {
            @Override
            protected void subscribeActual(Observer<? super String> observer) {
                observer.onNext("错误后重新发送");
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("--onErrorResumeNext---onSubscribe:");
            }

            @Override
            public void onNext(@NonNull String string) {
                System.out.println("--onErrorResumeNext---onNext:" + string);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("--onErrorResumeNext---onError:" + e.getMessage());

            }

            @Override
            public void onComplete() {
                System.out.println("--onErrorResumeNext---onComplete:");
            }
        });
    }
}
