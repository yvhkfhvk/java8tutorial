package com.yvhkfhvk.java8.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.*;
import java.util.stream.IntStream;

/**
 * @author 唐超
 * @date 10:30 上午
 */
public class LockTest {

    private static ReentrantLock lock = new ReentrantLock();

    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static Lock readLock = readWriteLock.readLock();

    private static Lock writeLock = readWriteLock.writeLock();

    private static StampedLock stampedLock = new StampedLock();

    private static Map<String, String> bookShelf = new HashMap<>();

    private static int count = 0;

    private static Logger logger = LogManager.getLogger(LockTest.class);


    /**
     * 测试锁
     */
    public static void testLock() {
        ExecutorService executorService = ExecutorUtils.newThreadPool();
        IntStream.range(0, 100).forEach(i -> executorService.submit(LockTest::increment));
        ExecutorUtils.closeThreadPool(executorService);
        logger.debug(count);
    }


    /**
     * 测试trylock
     */
    public static void testTryLock() {
        ExecutorService executorService = ExecutorUtils.newThreadPool();
        executorService.submit(() -> {
            try {
                lock.lock();
                ExecutorUtils.sleep(1);
            } finally {
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

    /**
     * 新增
     */
    private static void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
        logger.debug("count:" + count);
    }

    /**
     * 读写锁
     */
    public static void testReadWriteLock() {
        ExecutorService executorService = ExecutorUtils.newThreadPool();
        executorService.submit(() -> {

            try {
                writeLock.lock();
                logger.debug("write lock");
                ExecutorUtils.sleep(1);
                bookShelf.put("math", "one");
                logger.debug("put book");

            } finally {
                writeLock.unlock();
                logger.debug("write unlock");
            }
        });
        executorService.submit(()->{
            try {
                readLock.lock();
                logger.debug("read lock");
                String math = bookShelf.get("math");
                logger.debug("book count:"+ math);
            }finally {
                readLock.unlock();
                logger.debug("read unlock");
            }
        });
        ExecutorUtils.closeThreadPool(executorService);
    }

    /**
     * StampedLock 乐观锁
     */
    public static void testStampedLock(){
        ExecutorService executorService = ExecutorUtils.newThreadPool();

        executorService.submit(()->{
            long l = 0;
            try {
                l = stampedLock.tryOptimisticRead();
                logger.debug("Optimistic Lock Valid: " + stampedLock.validate(l));
                ExecutorUtils.sleep(1);
                logger.debug("Optimistic Lock Valid: " + stampedLock.validate(l));
                ExecutorUtils.sleep(1);
                logger.debug("Optimistic Lock Valid: " + stampedLock.validate(l));

            }finally {
                stampedLock.unlockRead(l);
                logger.debug("read unlock");
            }
        });

        executorService.submit(() -> {
            long l = 0;
            try {
                l = stampedLock.writeLock();
                logger.debug("write lock");
                ExecutorUtils.sleep(1);
                bookShelf.put("math", "one");
                logger.debug("put book");

            } finally {
                stampedLock.unlockWrite(l);
                logger.debug("write unlock");
            }
        });

        ExecutorUtils.closeThreadPool(executorService);
    }

    /**
     * 读锁转写锁
     */
    public static void testStampedLock2() {
        long l = stampedLock.readLock();
        try {
            if (count == 0) {
                l =
                        stampedLock.tryConvertToWriteLock(l);
                if (l == 0) {
                    logger.debug("tryConvertToWriteLock failed");
                    stampedLock.writeLock();
                }
                count = 415;
            }
        } finally {
            stampedLock.unlock(l);
        }
        logger.debug(count);

    }
    
  

    public static void main(String[] args) {
//        testLock();
//        testTryLock();
//        testReadWriteLock();
       // testStampedLock();
        testStampedLock2();
    }
}
