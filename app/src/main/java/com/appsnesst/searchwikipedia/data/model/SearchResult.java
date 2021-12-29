package com.appsnesst.searchwikipedia.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Model class representing whole search object returned from the json response
 */

public class SearchResult {

    private static final String LOG_TAG = SearchResult.class.getSimpleName();

    @SerializedName("continue")
    private Map<String, String> continueData;
    private QueryItem query;

    public QueryItem getQuery() {
        return query;
    }

    public Map<String, String> getContinue() {
        return continueData;
    }

    public void setQuery(QueryItem query) {
        this.query = query;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (continueData != null)
            sb.append("continue: " + continueData.toString());
        if (query != null)
            sb.append("\n" + query.toString());
        return sb.toString();
    }
}
