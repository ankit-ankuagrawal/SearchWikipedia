package com.appsnesst.searchwikipedia.data;

import com.appsnesst.searchwikipedia.data.model.AppsNesstSaveKeywordModel;
import com.appsnesst.searchwikipedia.data.model.SearchResult;
import com.appsnesst.searchwikipedia.data.net.AppsNesstApi;
import com.appsnesst.searchwikipedia.data.net.WikipediaApi;
import com.appsnesst.searchwikipedia.util.SearchUtility;

import java.util.Map;

import retrofit2.Call;

public class WikipediaRepository {

    private static final String LOG_TAG = WikipediaRepository.class.getSimpleName();

    private WikipediaApi wikipediaApiNetworkService;
    private AppsNesstApi appsNesstApiNetworkService;

    public WikipediaRepository() {
        wikipediaApiNetworkService = ServiceGenerator.createService(WikipediaApi.class);
        appsNesstApiNetworkService = ServiceGenerator.createService(AppsNesstApi.class);
    }

    public Call<SearchResult> searchWikipedia(String searchQuery, Map<String, String> continueQueryParamMap) {
        return wikipediaApiNetworkService.searchWikipedia(SearchUtility.getConstantQueryParamMap(), searchQuery, continueQueryParamMap);
    }

    public Call<Void> saveSearchQuery(String searchQuery) {
        return appsNesstApiNetworkService.saveSearchQuery(new AppsNesstSaveKeywordModel(searchQuery));
    }
}
