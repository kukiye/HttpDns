package com.jesson.httpdns;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.reactivestreams.Subscription;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
//        request();
        sendRequest();
    }

    // DO SELF
    private void sendRequest(){
        Network.getInstance().getApiService().getSplashPic().subscribeOn(
                Schedulers.io()
        ).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new FlowableSubscriber<SplashPicRes>() {
                    @Override
                    public void onSubscribe(@NonNull Subscription s) {
                        //业务层
                        s.request(1);
                    }

                    @Override
                    public void onNext(SplashPicRes splashPicRes) {
                        //业务bean
                        String s = new Gson().toJson(splashPicRes);
                        Log.d("tag",s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        //记录日志
                    }

                    @Override
                    public void onComplete() {

                    }
                }
        );

    }



    //阿里sdk
    private void request() {
        OkHttpClient client = new OkHttpClient.Builder().dns(new AliDns(getApplicationContext())).build();
        Retrofit retrofit = new Retrofit.Builder().client(client).baseUrl("https://www.wanandroid.com/").addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);
        Call<Bean> call = api.getBanner();
        call.enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                if (!response.isSuccessful()) {
                    tv.setText("请求失败，错误码:" + response.code());
                    return;
                }
                Bean bean = response.body();
                tv.setText(bean.toString());
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                tv.setText(t.getMessage());
            }
        });
    }
}