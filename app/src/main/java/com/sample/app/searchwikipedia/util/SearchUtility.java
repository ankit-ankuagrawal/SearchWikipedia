package com.sample.app.searchwikipedia.util;

import java.util.Map;

public class SearchUtility {
    public static String getQueryUri(String search, Map<String, String> queryParameterMap) {
        StringBuffer sb = new StringBuffer("https://en.wikipedia.org/w/api.php?")
                .append("action=query")
                .append("&format=json")
                .append("&prop=pageimages|pageterms")
                .append("&generator=prefixsearch")
                .append("&redirects=1")
                .append("&formatversion=2")
                .append("&piprop=thumbnail")
                .append("&pithumbsize=50")
                .append("&pilimit=10")
                .append("&wbptterms=description")
                .append("&gpslimit=10")
                .append("&gpssearch=" + search);
        if (queryParameterMap != null) {
            for (Map.Entry<String, String> queryParam : queryParameterMap.entrySet()) {
                sb.append("&" + queryParam.getKey() + "=" + queryParam.getValue());
            }
        }
        return sb.toString();
    }
}
