package com.athisii.mhb;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.athisii.mhb.database.Repository;
import com.athisii.mhb.database.Repository.FileType;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class App extends Application {
    public static final String PROPERTY_FILE = "app.properties";
    public static final String IS_LANGUAGE_ENGLISH = "is.language.english";
    public static final String IS_INITIAL_SETUP = "is.initial.setup";
    public static final String FONT_SIZE = "font.size";
    // for keeping track of last visited page in HomeHymn List
    private int currentHymnNumber = 1;
    private SharedPreferences sharedPreferences;
    private Repository repository;

    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = Repository.getInstance(this);
        sharedPreferences = getSharedPreferences(PROPERTY_FILE, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(IS_INITIAL_SETUP, true)) {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
            threadPoolExecutor.execute(() -> repository.saveDataToDb(FileType.HYMN));
//            threadPoolExecutor.execute(() -> repository.saveDataToDb(FileType.BIBLE))
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_LANGUAGE_ENGLISH, false);
            editor.putBoolean(IS_INITIAL_SETUP, false);
            editor.putInt(FONT_SIZE, 16);
            editor.apply();
            threadPoolExecutor.shutdown();
            // can wait for termination if wanted. that way all db will be initialized before activity startup.
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

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public int getCurrentHymnNumber() {
        return currentHymnNumber;
    }

    public void setCurrentHymnNumber(int hymnNumber) {
        currentHymnNumber = hymnNumber;
    }

}
