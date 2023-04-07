package com.athisii.mhb;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

import com.athisii.mhb.database.AppDatabase;
import com.athisii.mhb.network.ApiClient;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private AppDatabase database;
    private ApiClient apiClient;
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS) // Set your desired timeout value in seconds
            .build();
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(this, AppDatabase.class, "app_db").build();
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public ApiClient getApiClient() {
        synchronized (this) {
            if (apiClient == null) {
                apiClient = new Retrofit.Builder()
                        .baseUrl(ApiClient.BASE_URL)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(ApiClient.class);
            }
        }
        return apiClient;
    }

    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }
}
