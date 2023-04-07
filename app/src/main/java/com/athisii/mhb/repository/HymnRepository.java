package com.athisii.mhb.repository;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.athisii.mhb.App;
import com.athisii.mhb.dto.ResponseDto;
import com.athisii.mhb.entity.Hymn;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HymnRepository {
    private final App application;

    public HymnRepository(App application) {
        this.application = application;
    }

    public List<Hymn> getHymns() {
        return application.getDatabase().hymnDao().getHymns();
    }

    public void fetchHymns() {
        application.getApiClient().hymns().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto<List<Hymn>>> call, @NonNull Response<ResponseDto<List<Hymn>>> response) {
                if (response.isSuccessful() && (response.body() != null) && (response.body().isStatus())) {
                    insertHymn(response.body().getData());
                    Toast.makeText(application, "Data fetched successfully.", Toast.LENGTH_LONG).show();
                    return;
                }// else something went wrong.
                Toast.makeText(application, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseDto<List<Hymn>>> call, @NonNull Throwable t) {
                Toast.makeText(application, "Not connected to internet or connection not stable.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void insertHymn(List<Hymn> hymns) {
        // Transform and save in database
        ForkJoinPool.commonPool().execute(() -> application.getDatabase().hymnDao().insertHymns(hymns));
    }

}
