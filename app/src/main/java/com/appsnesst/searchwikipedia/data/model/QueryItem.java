package com.appsnesst.searchwikipedia.data.model;

import java.util.List;

/**
 * Model class representing Queries object from the json response
 */

public class QueryItem {
    private static final String LOG_TAG = QueryItem.class.getSimpleName();

    private List<PageItem> pages;

    // we can add redirect array here also

    public List<PageItem> getPages() {
        return pages;
    }

    public String toString() {
        StringBuffer bf = new StringBuffer();
        for (PageItem pageItem : pages) {
            bf.append(pageItem.toString() + "\n");
        }
        return bf.toString();
    }
}
