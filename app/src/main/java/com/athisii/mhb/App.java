package com.athisii.mhb;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.athisii.mhb.database.Repository;
import com.athisii.mhb.database.Repository.FileType;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class App extends Application {
    private static final String PROPERTY_FILE = "app.properties";
    private Repository repository;

    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = Repository.getInstance(this);
        var sharedPreferences = getSharedPreferences(PROPERTY_FILE, MODE_PRIVATE);
        if (sharedPreferences.getBoolean("is.initial.setup", true)) {
            Log.d("info", "********** Initial Setup **************");
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
            threadPoolExecutor.execute(() -> repository.saveDataToDb(FileType.HYMN));
//            threadPoolExecutor.execute(() -> repository.saveDataToDb(FileType.BIBLE))
            sharedPreferences.edit().putBoolean("is.initial.setup", false).apply();
            threadPoolExecutor.shutdown();
        }
    }


    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    public Repository getRepository() {
        return repository;
    }
}
