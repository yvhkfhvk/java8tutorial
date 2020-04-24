package com.yvhkfhvk.java8.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author 唐超
 * @date 4:44 下午 tangch
 */
public class CompletableFutureTest {

    private static Logger logger = LogManager.getLogger(CompletableFutureTest.class);

    /**
     * 测试complete方法
     */
    public static void testComplete(){
        CompletableFuture future = new CompletableFuture();
        future.complete(42);
        future.thenAccept(logger::debug).thenAccept(t->logger.debug("done"));
    }

    /**
     * 测试supplyAsync
     * 执行并有返回值
     */
    public static void testSupplyAsync(){
        CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 43;
        }).whenComplete((value,throwable)->{
            logger.debug(value);
        });
    }

    /**
     * 线程合并执行
     */
    public static void testCombine(){
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> ThreadLocalRandom.current().nextInt());
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> ThreadLocalRandom.current().nextInt());
        CompletableFuture<Integer> future3 = future1.thenCombine(future2, (t1, t2) -> {
            logger.debug(t1 + "," + t2);
            return t1 + t2;
        });
        try {
            logger.debug(future3.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试thenapply
     * 某个线程依赖另一个线程，让线程串行执行
     */
    public static void testThenApply() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> ThreadLocalRandom.current().nextInt());
        future.thenApply(i ->
                i + ThreadLocalRandom.current().nextInt()
        );
        try {
            logger.debug(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试handle
     * 与testThenApply ，不同在于出现异常也会执行
     */
    public static void testHandle(){
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> ThreadLocalRandom.current().nextInt());
        future.handle((i,t) ->
                i + ThreadLocalRandom.current().nextInt()
        );
        try {
            logger.debug(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        testComplete();
        testSupplyAsync();
        testCombine();
        testThenApply();
        testHandle();
    }
}
