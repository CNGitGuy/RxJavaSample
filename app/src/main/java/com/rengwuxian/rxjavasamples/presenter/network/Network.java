// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples.presenter.network;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static ApiGetZhuangbi apiGetZhuangbi;
    private static ApiGetGank apiGetGank;
    private static ApiFake apiFake;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();

    /**
     * 单例，获取Zhuangbi的Observable对象，
     *
     * @return
     */
    public static ApiGetZhuangbi getApiGetZhuangbi() {//不是线程安全的实现
        if (apiGetZhuangbi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)//添加OK Socket client
                    .baseUrl("http://www.zhuangbi.info/")//添加base URL
                    .addConverterFactory(gsonConverterFactory)//添加Retrofit的Json解析器
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            apiGetZhuangbi = retrofit.create(ApiGetZhuangbi.class);
        }
        return apiGetZhuangbi;
    }

    /**
     * 单例的API GetGank
     *
     * @return
     */
    public static ApiGetGank getApiGetGank() {//不是线程安全的实现
        if (apiGetGank == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            apiGetGank = retrofit.create(ApiGetGank.class);
        }
        return apiGetGank;
    }

    /**
     * 单例的APIFake
     *
     * @return
     */
    public static ApiFake getApiFake() {//不是线程安全的实现
        if (apiFake == null) {
            apiFake = new ApiFake();
        }
        return apiFake;
    }
}
