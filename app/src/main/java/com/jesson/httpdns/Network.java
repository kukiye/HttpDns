package com.jesson.httpdns;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    private static Network INSTANCE;

    private Network() {

    }

    public static synchronized Network getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Network();
        }
        return INSTANCE;
    }

    private Retrofit retrofit;

    private Retrofit getRetrofit() {
        if (retrofit == null) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .dns(OkHttpDns.getInstance())
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(interceptor);
            }

            retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .baseUrl(Api.BiliBili_API_HOST)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public Api getApiService() {
        return getRetrofit().create(Api.class);
    }
}

