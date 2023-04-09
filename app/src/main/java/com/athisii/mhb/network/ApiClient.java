package com.athisii.mhb.network;

import com.athisii.mhb.dto.ResponseDto;
import com.athisii.mhb.entity.Hymn;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiClient {
    //    String BASE_URL = "https://api.athisii.com"
    String BASE_URL = "http://192.168.1.5:8080/api/v1/";

    @GET("hymn/all-page")
    Call<ResponseDto<List<Hymn>>> hymns();

    @GET("hymn")
    Call<ResponseDto<List<Hymn>>> hymns(@Query("page") int page, @Query("size") int size);
}
