package com.athisii.mhb.repository;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.athisii.mhb.App;
import com.athisii.mhb.database.AppDatabase;
import com.athisii.mhb.dto.ResponseDto;
import com.athisii.mhb.entity.Hymn;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    int size;
    private static final String DB_NAME = "app_db";
    private static final short NUM_OF_HYMN = 1000;
    private static final short NUM_OF_HYMN_PER_REQUEST = 100;
    private static final short NUM_OF_REQUEST = NUM_OF_HYMN / NUM_OF_HYMN_PER_REQUEST;
    private final App application;
    private final AppDatabase database;
    private static Repository repository;

    private Repository(App application) {
        this.application = application;
        database = Room.databaseBuilder(application, AppDatabase.class, DB_NAME).build();
    }

    public static synchronized Repository getInstance(App application) {
        if (repository == null) {
            repository = new Repository(application);
        }
        return repository;
    }


    public void fetchHymnsFromServer() {
        for (short page = 0; page < NUM_OF_REQUEST; page++) {
            application.getApiClient().hymns(page, NUM_OF_HYMN_PER_REQUEST).enqueue(new MyCallback());
        }
    }

    private class MyCallback implements Callback<ResponseDto<List<Hymn>>> {
        @Override
        public void onResponse(@NonNull Call<ResponseDto<List<Hymn>>> call, @NonNull Response<ResponseDto<List<Hymn>>> response) {
            if (response.isSuccessful() && (response.body() != null) && (response.body().isStatus())) {
                insertHymn(response.body().getData());
                Toast.makeText(application, "Data fetched successfully.", Toast.LENGTH_SHORT).show();
                return;
            }// else something went wrong.
            Toast.makeText(application, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(@NonNull Call<ResponseDto<List<Hymn>>> call, @NonNull Throwable t) {
            Toast.makeText(application, "Not connected to internet or connection not stable.", Toast.LENGTH_LONG).show();
        }

        private void insertHymn(List<Hymn> hymns) {
            ForkJoinPool.commonPool().execute(() -> {
                size += hymns.size();
                Log.d("info", "**********Size: " + size);
                database.hymnDao().insertHymns(hymns);
            });
        }
    }

    public void deleteAllHymnsInDb() {
        database.hymnDao().deleteAllHymns();
    }

    public List<Hymn> getAllHymns() {
        return database.hymnDao().getAllHymns();
    }

}
