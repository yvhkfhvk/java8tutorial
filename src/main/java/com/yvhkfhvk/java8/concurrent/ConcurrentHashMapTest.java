package com.yvhkfhvk.java8.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 唐超
 * @date 11:19 上午
 */
public class ConcurrentHashMapTest {

    private static Logger logger = LogManager.getLogger(ConcurrentHashMapTest.class);

    /**
     * 测试reduce
     */
    public static void testReduce() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("ecs", "a");
        map.put("c1", "b");
        map.put("planning", "c");
        map.put("database", "d");
        String reduce = map.reduce(1, (key, value) -> key + "=" + value, (s1, s2) -> s1 + "," + s2);
        logger.debug(reduce);
    }

    /**
     * 测试搜索
     */
    public static void testSearch(){
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("ecs", "a");
        map.put("c1", "b");
        map.put("planning", "c");
        map.put("database", "d");
        String ecs = map.search(1, (key, value) -> {
            logger.debug("key:" + key + "," + "value:" + value);
            if (key.equals("ecs")) {
                return value;
            }
            return null;
        });
        logger.debug(ecs);
    }

    public static void main(String[] args) {
        testReduce();
        testSearch();
    }
}
