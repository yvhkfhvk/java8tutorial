package com.yvhkfhvk.java8.concurrent;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 唐超
 * @date 4:01 下午
 */
public class ExecutorUtils {

    private static Logger logger= LogManager.getLogger(ExecutorUtils.class);


    /**
     * build thread pool
     * @return
     */
    public static ExecutorService newThreadPool() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(8,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        logger.debug("executorService builded success");
        return executorService;
    }

    /**
     * close thread pool
     * @param executor
     */
    public static void closeThreadPool(ExecutorService executor){
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
            logger.debug("executor shutdown");
        }
        catch (InterruptedException e) {
            logger.error("termination interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                logger.error("killing non-finished tasks");
            }
            executor.shutdownNow();
        }

    }

    /**
     * 按秒睡眠
     * @param second
     */
    public static void sleep(long second){
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
        }
    }
}
