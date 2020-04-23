package com.yvhkfhvk.java8.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
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

    public static void main(String[] args) {
        testComplete();
        testSupplyAsync();
    }
}
