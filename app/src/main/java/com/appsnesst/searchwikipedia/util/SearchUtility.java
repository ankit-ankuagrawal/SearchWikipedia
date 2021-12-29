package com.appsnesst.searchwikipedia.util;

import android.net.Uri;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class to perform some basic operations
 */

//https://en.wikipedia.org/wiki/Special:ApiSandbox#action=query&format=json&prop=info%7Cpageimages%7Cpageterms&generator=prefixsearch&redirects=1&inprop=&gpssearch=Hello

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

    public static final String REDIRECT_STRING = "redirects";
    public static final int REDIRECT_VALUE = 1;

    public static final String FORMAT_VERSION_STRING = "formatversion";
    public static final int FORMAT_VERSION_VALUE = 2;

    //Get basic page information.
    //https://www.mediawiki.org/w/api.php?action=help&modules=query%2Binfo
    public static final String INPROP_STRING = "inprop";
    public static final String URL_STRING = "url";

    //Returns information about images on the page, such as thumbnail and presence of photos.
    //https://www.mediawiki.org/w/api.php?action=help&modules=query%2Bpageimages
    public static final String PIPROP_KEY_STRING = "piprop";
    public static final String THUMBNAIL_VALUE_STRING = "thumbnail";
    public static final String PITHUMBSIZE_KEY_STRING = "pithumbsize";
    public static final int PITHUMBSIZE_VALUE = 50;
    public static final String PILIMIT_KEY_STRING = "pilimit";
    public static final int PILIMIT_VALUE = 10;

    //Get the Wikidata terms (typically labels, descriptions and aliases) associated with a page via a sitelink.
    //https://www.mediawiki.org/w/api.php?action=help&modules=query%2Bpageterms
    public static final String WBPTTERMS_KEY_STRING = "wbptterms";
    public static final String DESCRIPTION_STRING = "description";

    public static final String GENERATOR_KEY_STRING = "generator";
    public static final String PREFIXSEARCH_VALUE_STRING = "prefixsearch";
    //Perform a prefix search for page titles.
    //https://www.mediawiki.org/w/api.php?action=help&modules=query%2Bprefixsearch
    public static final String GPSLIMIT_KEY_STRING = "gpslimit";
    public static final int GPSLIMIT_VALUE = 10;
    public static final String GPSSEARCH_STRING = "gpssearch";

    public static final String FULL_URL_EXTRA = "FULL_URL";
    public static final String PAGE_TITLE_EXTRA = "PAGE_TITLE";

    public static Map<String, String> getConstantQueryParamMap() {
        LinkedHashMap<String, String> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put(ACTION_STRING, QUERY_STRING);
        queryParamMap.put(FORMAT_STRING, JSON_STRING);
        queryParamMap.put(PROP_STRING, PAGE_IMAGES_STRING + "|" + PAGE_TERMS_STRING + "|" + INFO_STRING);
        queryParamMap.put(INPROP_STRING, URL_STRING);
        queryParamMap.put(GENERATOR_KEY_STRING, PREFIXSEARCH_VALUE_STRING);
        queryParamMap.put(REDIRECT_STRING, String.valueOf(REDIRECT_VALUE));
        queryParamMap.put(FORMAT_VERSION_STRING, String.valueOf(FORMAT_VERSION_VALUE));
        queryParamMap.put(PIPROP_KEY_STRING, THUMBNAIL_VALUE_STRING);
        queryParamMap.put(PITHUMBSIZE_KEY_STRING, String.valueOf(PITHUMBSIZE_VALUE));
        queryParamMap.put(PILIMIT_KEY_STRING, String.valueOf(PILIMIT_VALUE));
        queryParamMap.put(WBPTTERMS_KEY_STRING, DESCRIPTION_STRING);
        queryParamMap.put(GPSLIMIT_KEY_STRING, String.valueOf(GPSLIMIT_VALUE));

        return queryParamMap;
    }

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
                .appendQueryParameter(GENERATOR_KEY_STRING, PREFIXSEARCH_VALUE_STRING)
                .appendQueryParameter(REDIRECT_STRING, String.valueOf(REDIRECT_VALUE))
                .appendQueryParameter(FORMAT_VERSION_STRING, String.valueOf(FORMAT_VERSION_VALUE))
                .appendQueryParameter(PIPROP_KEY_STRING, THUMBNAIL_VALUE_STRING)
                .appendQueryParameter(PITHUMBSIZE_KEY_STRING, String.valueOf(PITHUMBSIZE_VALUE))
                .appendQueryParameter(PILIMIT_KEY_STRING, String.valueOf(PILIMIT_VALUE))
                .appendQueryParameter(WBPTTERMS_KEY_STRING, DESCRIPTION_STRING)
                .appendQueryParameter(GPSLIMIT_KEY_STRING, String.valueOf(GPSLIMIT_VALUE))
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
