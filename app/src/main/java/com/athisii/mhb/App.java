package com.athisii.mhb;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.athisii.mhb.network.ApiClient;
import com.athisii.mhb.repository.Repository;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static final String PROPERTY_FILE = "app.properties";
    private Repository repository;
    private ApiClient apiClient;

    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = Repository.getInstance(this);
        var sharedPreferences = getSharedPreferences(PROPERTY_FILE, MODE_PRIVATE);
        if (sharedPreferences.getBoolean("is.initial.setup", true)) {
            Log.d("info", "********** Initial Setup **************");
            sharedPreferences.edit().putBoolean("is.initial.setup", false).apply();
            getApiClient();
            repository.deleteAllHymnsInDb();
            repository.fetchHymnsFromServer();
        }
    }

    public ApiClient getApiClient() {
        synchronized (this) {
            if (apiClient == null) {
                apiClient = new Retrofit.Builder()
                        .baseUrl(ApiClient.BASE_URL)
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

    public Repository getRepository() {
        return repository;
    }
}
