package com.appsnesst.searchwikipedia.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.appsnesst.searchwikipedia.data.WikipediaRepository;
import com.appsnesst.searchwikipedia.data.model.SearchResult;

import java.util.Map;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {

    private static final String LOG_TAG = SearchViewModel.class.getSimpleName();

    private WikipediaRepository wikipediaRepository = new WikipediaRepository();

    //mutable live data
    @Getter
    private MutableLiveData<SearchResult> searchResultMutableLiveData = new MutableLiveData<>();


    public void searchWikipedia(String query, Map<String, String> continueQueryParamMap) {
        Call<SearchResult> searchResultCall = wikipediaRepository.searchWikipedia(query, continueQueryParamMap);
        searchResultCall.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                Log.i(LOG_TAG, "searchWikipedia onResponse" + response.code());
                if (response.isSuccessful()) {
                    SearchResult searchResult = response.body();

                    searchResultMutableLiveData.postValue(response.body());

                    if (searchResult.getContinue() != null && !searchResult.getContinue().isEmpty()) {
                        searchWikipedia(query, searchResult.getContinue());
                    }
                } else {
                    searchResultMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e(LOG_TAG, "searchWikipedia onFailure", t);
            }
        });
    }

    public void saveSearchQuery(String searchQuery) {
        Call<Void> call = wikipediaRepository.saveSearchQuery(searchQuery);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
}
