package com.yvhkfhvk.java8.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

/**
 * @author 唐超
 * @date 11:22 上午
 */
public class SemaphoreTest {

    private static Logger logger = LogManager.getLogger(SemaphoreTest.class);

    private static int count = 0;


    /**
     * @param
     * @author: eric
     * @date: 2020/4/27 11:44 上午
     * @description:
     */
    public static void testSemaphore() {
        Semaphore semaphore = new Semaphore(1);
        ExecutorService executorService = ExecutorUtils.newThreadPool();
        IntStream.range(0, 10).forEach(i -> {
            executorService.submit(() -> {
                try {
                    semaphore.acquire();
                    logger.debug("get acquire");
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            });
        });
        ExecutorUtils.closeThreadPool(executorService);
        logger.debug("count:" + count);
    }


    /**
     * @param
     * @author: eric
     * @date: 2020/4/27 11:51 上午
     * @description:
     * @return: void
     */
    public static void testSemaphore2() {
        Semaphore semaphore = new Semaphore(10);
        ExecutorService executorService = ExecutorUtils.newThreadPool();
        IntStream.range(0, 10).forEach(i -> {
            executorService.submit(()->{
                boolean b = semaphore.tryAcquire(5);
                if(b){
                    logger.debug("acquire successed");
                    count++;
                }else{
                    logger.debug("acquire failed");
                }
            });
        });
        ExecutorUtils.closeThreadPool(executorService);

        logger.debug("count:" + count);
    }


    public static void main(String[] args) {
        testSemaphore2();
    }
}

