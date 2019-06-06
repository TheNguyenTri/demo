package com.example.demo.callback;



import com.example.demo.config;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;

public interface DownloadVideoCallback {

    @Streaming
    @GET(config.URL)
    Call<ResponseBody> downloadVideo();

}
