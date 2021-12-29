package com.appsnesst.searchwikipedia.data.model;

import com.appsnesst.searchwikipedia.util.AppUtility;
import com.sample.app.searchwikipedia.BuildConfig;

public class AppsNesstSaveKeywordModel {

    private String appVersion;
    private String searchQuery;
    private String locale;

    public AppsNesstSaveKeywordModel(String searchQuery)
    {
        this.appVersion = BuildConfig.VERSION_NAME;
        this.searchQuery = searchQuery;
        this.locale = AppUtility.getCurrentLocale().getDisplayName();
    }
}
