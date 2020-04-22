package com.yvhkfhvk.java8.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author 唐超
 * @date 3:59 下午 tangch
 */
public class AtomicTest {

    private static final int NUM_INCREMENTS = 1000;

    private static AtomicInteger atomicInt = new AtomicInteger(0);

    private static Logger logger= LogManager.getLogger(AtomicTest.class);


    /**
     * test atomtic update
     */
    public static void testUpdate(){
        atomicInt.set(0);
        ExecutorService executorService = ExecutorUtils.newThreadPool();
        IntStream.range(0,NUM_INCREMENTS).forEach(count->{
            executorService.submit(() -> atomicInt.updateAndGet(n -> n + 2));
        });
        ExecutorUtils.closeThreadPool(executorService);
        logger.debug(atomicInt.get());
    }

    /**
     * test atomic increment
     */
    public static void testIncrement(){
        atomicInt.set(0);
        ExecutorService executorService = ExecutorUtils.newThreadPool();
        IntStream.range(0,NUM_INCREMENTS).forEach(count->{
            executorService.submit(() -> atomicInt.incrementAndGet());
        });
        ExecutorUtils.closeThreadPool(executorService);
        logger.debug(atomicInt.get());
    }


    /**
     * test atomic decrement
     */
    public static void testDecrement(){
        atomicInt.set(0);
        ExecutorService executorService = ExecutorUtils.newThreadPool();
        IntStream.range(0,NUM_INCREMENTS).forEach(count->{
            executorService.submit(() -> atomicInt.decrementAndGet());
        });
        ExecutorUtils.closeThreadPool(executorService);
        logger.debug(atomicInt.get());
    }

    public static void main(String[] args) {
        testUpdate();
        testIncrement();
        testDecrement();
    }
}
