package cn.tongchengyuan.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ****************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：维护一个线程池的工具类
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/2
 * *****************************************************
 */
public class ThreadPoolUtil {

    private ThreadPoolUtil() { }

    // 线程池核心线程数
    private static final int CORE_POOL_SIZE = 5;
    // 线程池最大线程数
    private static final int MAX_POOL_SIZE = 100;
    // 额外线程空状态生存时间
    private static final int KEEP_ALIVE_TIME = 10000;
    // 阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(20);

    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger atomicInteger = new AtomicInteger();
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "currentThread : " + atomicInteger.getAndIncrement());
        }
    };

    private static ThreadPoolExecutor sExecutor;
    static {
        sExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory);
    }

    public static void execute(Runnable r){
        sExecutor.execute(r);
    }

    public static <V>  Future<V> submit(Callable<V> callable){
        return sExecutor.submit(callable);
    }

    public static void shutDown(){
        sExecutor.shutdown();
    }

}
