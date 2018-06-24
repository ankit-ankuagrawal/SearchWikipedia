package com.sample.app.searchwikipedia.model;

import java.util.ArrayList;
import java.util.List;

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
