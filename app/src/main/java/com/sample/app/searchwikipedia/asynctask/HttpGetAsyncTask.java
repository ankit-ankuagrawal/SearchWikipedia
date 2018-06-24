package com.sample.app.searchwikipedia.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.sample.app.searchwikipedia.activity.SearchActivity;
import com.sample.app.searchwikipedia.model.SearchResult;
import com.sample.app.searchwikipedia.util.SearchUtility;

import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Async task responsible to make network call to get search result from wikipedia server and update the ui
 */

public class HttpGetAsyncTask extends AsyncTask<String, Integer, Void> {

    private static final String LOG_TAG = HttpGetAsyncTask.class.getSimpleName();

    private SearchActivity searchActivity;

    public HttpGetAsyncTask(SearchActivity searchActivity) {
        this.searchActivity = searchActivity;
    }

    @Override
    protected Void doInBackground(String... data) {
        URL url = null;
        HttpsURLConnection urlConnection = null;
        SearchResult result = new SearchResult();
        boolean morePageAvailable = false;
        int totalPageItemCount = 0;

        try {

            do {
                morePageAvailable = false;
                url = new URL(SearchUtility.getQueryStringUri(data[0], result.getContinue()));
                urlConnection = (HttpsURLConnection) url.openConnection();

                urlConnection.addRequestProperty("Cache-Control", "public, max-stale=" + 60 * 60 * 24 * 28);
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setUseCaches(true);
                urlConnection.connect();

                int getResponseCode = urlConnection.getResponseCode();
                Log.i(LOG_TAG, "response code: " + getResponseCode);
                if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    Gson gson = new Gson();
                    result = gson.fromJson(new InputStreamReader(urlConnection.getInputStream()), SearchResult.class);

                    int requestItemCount = 0;
                    if (result.getQuery() != null) {
                        requestItemCount = result.getQuery().getPages().size();
                        searchActivity.getPageItemList().addAll(result.getQuery().getPages());
                        totalPageItemCount += requestItemCount;
                        Log.i(LOG_TAG, totalPageItemCount + " " + requestItemCount + " " + urlConnection.getResponseCode());
                    }

                    publishProgress(totalPageItemCount, requestItemCount);

                    if (result.getContinue() != null && !result.getContinue().isEmpty()) {
                        morePageAvailable = true;
                    }
                } else {
                    publishProgress(0, 0);
                }
            }
            while (morePageAvailable);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception while making a network request" + e);
            publishProgress(0, 0);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... data) {
        searchActivity.cancelProgressDialog();
        searchActivity.updateRecyclerView(data[0], data[1]);
    }
}
