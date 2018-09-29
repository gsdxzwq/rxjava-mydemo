package com.zhaowq.rxjava;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

/**
 * @author zhaowq
 * @date 2017/9/1
 */
public class RxJavaTest {

    @Test
    public void test() {
        Flowable.fromCallable(() -> {
            Thread.sleep(1000); //imitate expensive computation
            return "Done";
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(System.out::println, Throwable::printStackTrace);
        try {
            Thread.sleep(2000); //wait for the flow to finish
        } catch (InterruptedException e) {

        }
    }

    @Test
    public void test2(){
        Flowable.range(1, 10)
                .observeOn(Schedulers.computation())
                .map(v -> v * v)
                .blockingSubscribe(System.out::println);
    }

    @Test
    public void test3(){
        Flowable.range(1, 10)
                .flatMap(v ->
                        Flowable.just(v)
                                .subscribeOn(Schedulers.computation())
                                .map(w -> w * w)
                )
                .blockingSubscribe(System.out::println);
    }

    @Test
    public void test4(){
        Flowable.range(1, 10)
                .parallel()
                .runOn(Schedulers.computation())
                .map(v -> v * v)
                .sequential()
                .blockingSubscribe(System.out::println);
    }
}
