package com.appsnesst.searchwikipedia.data;

import com.appsnesst.searchwikipedia.util.SearchUtility;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String BASE_URL = SearchUtility.HTTPS_SCHEME+":////"+SearchUtility.SEARCH_AUTHORITY_URL;

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(new OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
