package com.sample.app.searchwikipedia.util;

import android.net.Uri;

import java.util.Map;

/**
 * Utility class to perform some basic operations
 */

public class SearchUtility {

    public static final String HTTPS_SCHEME = "https";

    public static final String SEARCH_AUTHORITY_URL = "en.wikipedia.org";
    public static final String SEARCH_PATH_1_URL = "w";
    public static final String SEARCH_PATH_2_URL = "api.php";

    public static final String ACTION_STRING = "action";
    public static final String QUERY_STRING = "query";

    public static final String FORMAT_STRING = "format";
    public static final String JSON_STRING = "json";

    public static final String PROP_STRING = "prop";
    public static final String PAGE_IMAGES_STRING = "pageimages";
    public static final String PAGE_TERMS_STRING = "pageterms";
    public static final String INFO_STRING = "info";

    public static final String INPROP_STRING = "inprop";
    public static final String URL_STRING = "url";

    public static final String GENERATOR_STRING = "generator";
    public static final String PREFIXSEARCH_STRING = "prefixsearch";

    public static final String REDIRECT_STRING = "redirects";
    public static final int REDIRECT_VALUE = 1;

    public static final String FORMAT_VERSION_STRING = "formatversion";
    public static final int FORMAT_VERSION_VALUE = 2;

    public static final String PIPROP_STRING = "piprop";
    public static final String THUMBNAIL_STRING = "thumbnail";

    public static final String PITHUMBSIZE_STRING = "pithumbsize";
    public static final int PITHUMBSIZE_VALUE = 50;

    public static final String PILIMIT_STRING = "pilimit";
    public static final int PILIMIT_VALUE = 10;

    public static final String WBPTTERMS_STRING = "wbptterms";
    public static final String DESCRIPTION_STRING = "description";

    public static final String GPSLIMIT_STRING = "gpslimit";
    public static final int GPSLIMIT_VALUE = 10;

    public static final String GPSSEARCH_STRING = "gpssearch";

    public static final String FULL_URL_EXTRA = "FULL_URL";
    public static final String PAGE_TITLE_EXTRA = "PAGE_TITLE";

    public static String getQueryStringUri(String query, Map<String, String> queryParameterMap) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(HTTPS_SCHEME)
                .authority(SEARCH_AUTHORITY_URL)
                .appendPath(SEARCH_PATH_1_URL)
                .appendPath(SEARCH_PATH_2_URL)
                .appendQueryParameter(ACTION_STRING, QUERY_STRING)
                .appendQueryParameter(FORMAT_STRING, JSON_STRING)
                .appendQueryParameter(PROP_STRING, PAGE_IMAGES_STRING + "|" + PAGE_TERMS_STRING + "|" + INFO_STRING)
                .appendQueryParameter(INPROP_STRING, URL_STRING)
                .appendQueryParameter(GENERATOR_STRING, PREFIXSEARCH_STRING)
                .appendQueryParameter(REDIRECT_STRING, String.valueOf(REDIRECT_VALUE))
                .appendQueryParameter(FORMAT_VERSION_STRING, String.valueOf(FORMAT_VERSION_VALUE))
                .appendQueryParameter(PIPROP_STRING, THUMBNAIL_STRING)
                .appendQueryParameter(PITHUMBSIZE_STRING, String.valueOf(PITHUMBSIZE_VALUE))
                .appendQueryParameter(PILIMIT_STRING, String.valueOf(PILIMIT_VALUE))
                .appendQueryParameter(WBPTTERMS_STRING, DESCRIPTION_STRING)
                .appendQueryParameter(GPSLIMIT_STRING, String.valueOf(GPSLIMIT_VALUE))
                .appendQueryParameter(GPSSEARCH_STRING, query);
        if (queryParameterMap != null) {
            for (Map.Entry<String, String> queryParam : queryParameterMap.entrySet()) {
                uriBuilder.appendQueryParameter(queryParam.getKey(), queryParam.getValue());
            }
        }

        return uriBuilder.build().toString();
    }

    public static final int ACTIVITY_CLOSED = 100;
    public static final int SEARCH_NEW_ARTICLE = 101;
}
