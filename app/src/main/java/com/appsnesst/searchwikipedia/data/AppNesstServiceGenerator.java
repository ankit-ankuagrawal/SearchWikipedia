package com.appsnesst.searchwikipedia.data;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppNesstServiceGenerator {

    private static Retrofit retrofit = new Retrofit.Builder()
            .client(new OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
