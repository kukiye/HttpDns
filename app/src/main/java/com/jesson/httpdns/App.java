package com.jesson.httpdns;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //inject httpdns
        HttpDnsCache dnsCache = HttpDnsCache.getInstance();
        dnsCache.init(100,"app.bilibili.com","api.bilibili.com");
        dnsCache.loadDnsCache();
    }
}