package com.example.demo.manager;


import com.example.demo.callback.ResponseCallbackListener;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadVideoManager {

    private ResponseCallbackListener<ResponseBody> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String DOWNLOAD_VIDEO = "DOWNLOAD_VIDEO";

    public DownloadVideoManager(ResponseCallbackListener<ResponseBody> mListener) {
        this.mListener = mListener;
    }

    public void startGetData() {
        Call<ResponseBody> call = mRestApiManager.downloadVideoCallback()
                .downloadVideo();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(DOWNLOAD_VIDEO, response.body());
                } else {
                    mListener.onResponseFailed(DOWNLOAD_VIDEO, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mListener.onResponseFailed(DOWNLOAD_VIDEO, t.getMessage());
            }
        });
    }
}
