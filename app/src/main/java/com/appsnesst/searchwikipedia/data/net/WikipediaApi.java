package com.appsnesst.searchwikipedia.data.net;

import com.appsnesst.searchwikipedia.data.model.SearchResult;
import com.appsnesst.searchwikipedia.util.SearchUtility;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/*
Wikipedia api's
* */
public interface WikipediaApi {
    //https://en.wikipedia.org/w/api.php
    @GET("/w/api.php")
    Call<SearchResult> searchWikipedia(@QueryMap Map<String, String> constantQueryParamMap,
                                       @Query(SearchUtility.GPSSEARCH_STRING) String query,
                                       @QueryMap Map<String, String> continueQueryParamMap);
}
