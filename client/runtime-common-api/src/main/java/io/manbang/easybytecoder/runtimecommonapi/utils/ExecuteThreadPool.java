package io.manbang.easybytecoder.runtimecommonapi.utils;

import java.util.concurrent.*;

public class ExecuteThreadPool {
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(10,100,5,TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(10000));

    public static void runAysnc(Runnable task) {
        pool.execute(task);
    }




}
