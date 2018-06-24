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

public class HttpGetAsyncTask extends AsyncTask<String, Integer, Void> {

    private static final String LOG_TAG = HttpGetAsyncTask.class.getSimpleName();

    private SearchActivity searchActivity;

    public HttpGetAsyncTask(SearchActivity searchActivity) {
        this.searchActivity = searchActivity;
    }

    public void execute() {
        super.execute();
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
                urlConnection.connect();

                if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    Gson gson = new Gson();
                    result = gson.fromJson(new InputStreamReader(urlConnection.getInputStream()), SearchResult.class);

                    if (result.getQuery() != null) {
                        int requestItemCount = result.getQuery().getPages().size();
                        searchActivity.getPageItemList().addAll(result.getQuery().getPages());
                        publishProgress(totalPageItemCount, requestItemCount);
                        totalPageItemCount += requestItemCount;
                        Log.i(LOG_TAG, totalPageItemCount + " " + requestItemCount + " " + urlConnection.getResponseCode());
                    }

                    if (result.getContinue() != null && !result.getContinue().isEmpty()) {
                        morePageAvailable = true;
                    }
                }
            }
            while (morePageAvailable);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception while making a network request", e);
        }

        return null;

    }

    @Override
    protected void onProgressUpdate(Integer... data) {
        searchActivity.cancelProgressDialog();
        searchActivity.updateRecyclerView(data[0], data[1]);
    }
}
