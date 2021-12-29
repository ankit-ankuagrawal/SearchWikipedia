package com.appsnesst.searchwikipedia.data.net;

import com.appsnesst.searchwikipedia.data.model.AppsNesstSaveKeywordModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/*
AppsNesst api's to save the keyword search by the user
* */
public interface AppsNesstApi {

    @POST("https://script.google.com/macros/s/AKfycbypcPMdgdIFb33PxoWqvto6dmtx_9IV2-CeQLl-cCou7oWloSBfGqXPoII-Cp9A6GeF/exec")
    Call<Void> saveSearchQuery(@Body AppsNesstSaveKeywordModel appsNesstSaveKeywordModel);
}
