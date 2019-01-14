package tianchi.com.risksourcecontrol2.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * 线程
 */
public class ThreadUtil {

    private static ExecutorService executorService;

    public static void execute(Runnable task){
        if(executorService == null){
            executorService = Executors.newFixedThreadPool(10);
        }
        executorService.submit(task);
    }

}