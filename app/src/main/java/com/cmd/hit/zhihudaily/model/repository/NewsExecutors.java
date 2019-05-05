package com.cmd.hit.zhihudaily.model.repository;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by PC-0775 on 2019/5/1.
 */

public class NewsExecutors {
    private final ExecutorService diskIO = Executors.newSingleThreadExecutor();
    public final ExecutorService networkIO = Executors.newFixedThreadPool(6);
    private final ExecutorService mainThread = Executors.newFixedThreadPool(3);

    class MainThreadExecutor implements Executor{

        private Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {
            handler.post(command);
        }
    }

}
