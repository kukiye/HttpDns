package com.jesson.httpdns;

import android.util.Log;

import androidx.annotation.NonNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Dns;

public class OkHttpDns implements Dns {
    private static OkHttpDns instance;

    private OkHttpDns() {

    }

    public static synchronized OkHttpDns getInstance() {
        if (instance == null) {
            instance = new OkHttpDns();
        }

        return instance;
    }

    private static final String TAG = "OkHttpDns";

    private HttpDnsCache mDnsCache = HttpDnsCache.getInstance();


    @Override
    public List<InetAddress> lookup(@NonNull String hostname) throws UnknownHostException {

        String ip = mDnsCache.getIpByHostAsync(hostname);
        Log.d(TAG, "mDnsCache IP: " + ip);

        if (ip != null) {
            //如果ip不为null，直接使用该ip进行网络请求
            return Arrays.asList(InetAddress.getAllByName(ip));
        }
        //如果返回null，走系统DNS服务解析域名
        return Dns.SYSTEM.lookup(hostname);

    }

}