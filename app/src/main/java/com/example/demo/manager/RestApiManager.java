package com.example.demo.manager;

import com.example.demo.callback.DownloadVideoCallback;
import com.example.demo.config;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiManager {

    private static RestApiManager mRestApiManager;

    private static Retrofit mRetrofit;

    private RestApiManager() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(config.TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(config.TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(config.TIME_OUT, TimeUnit.MILLISECONDS);
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
//                        .header("Content", "multipart/form-data")
//                        .removeHeader("Content-Type")
                        .method(original.method(), original.body()); // <-- this is the important line
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).addNetworkInterceptor(logging);
        OkHttpClient client = httpClient.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(config.API_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .serializeNulls()
                        .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                        .create()))
                .client(client)
                .build();
    }

    public static RestApiManager getInstance() {
        if (mRestApiManager == null) {
            mRestApiManager = new RestApiManager();
        }
        return mRestApiManager;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }


    DownloadVideoCallback downloadVideoCallback() {
        return mRetrofit.create(DownloadVideoCallback.class);
    }

}
