package com.yvhkfhvk.java8.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @author 唐超
 * @date 10:30 上午
 */
public class LockTest {

    private static ReentrantLock lock = new ReentrantLock();

    private static int count = 0;

    private static Logger logger = LogManager.getLogger(LockTest.class);


    public static void testLock(){
        ExecutorService executorService = ExecutorUtils.newThreadPool();
        IntStream.range(0,100).forEach(i-> executorService.submit(LockTest::increment));
        ExecutorUtils.closeThreadPool(executorService);
        logger.debug(count);
    }


    public static void testTryLock(){
        ExecutorService executorService = ExecutorUtils.newThreadPool();
        executorService.submit(()->{
            try {
                lock.lock();
                ExecutorUtils.sleep(1);
            }finally {
                lock.unlock();
            }
        });
        executorService.submit(() -> {
            logger.debug("lockd:" + lock.isLocked());
            logger.debug("try lock");
            boolean lk = lock.tryLock();
            logger.debug("try lock result:" + lk);
        });
        ExecutorUtils.closeThreadPool(executorService);
    }

    private static void increment(){
        lock.lock();
        try {
            count++;
        }finally {
            lock.unlock();
        }
        logger.debug("count:"+count);
    }

    public static void main(String[] args) {
        testLock();
        testTryLock();
    }
}
