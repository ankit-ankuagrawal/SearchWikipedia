package com.sample.app.searchwikipedia.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rakagarw on 22/06/2018.
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
        sb.append("continue: " + continueData.toString())
                .append("\n" + query.toString());
        return sb.toString();
    }
}
