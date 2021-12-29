package com.appsnesst.searchwikipedia.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model class representing Pages object from the json response
 */

public class PageItem {

    private static final String LOG_TAG = PageItem.class.getSimpleName();

    @SerializedName("pageid")
    private int pageId;
    private int ns;
    private String title;
    private int index;
    @SerializedName("fullurl")
    private String fullUrl;
    private ThumbnailItem thumbnail;
    private TermsItem terms;

    public int getPageId() {
        return pageId;
    }

    public int getNs() {
        return ns;
    }

    public String getTitle() {
        return title;
    }

    public int getIndex() {
        return index;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public ThumbnailItem getThumbnail() {
        return thumbnail;
    }

    public TermsItem getTerms() {
        return terms;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("pageId: " + pageId);
        sb.append("\nns: " + ns).
                append("\ntitle: " + title).
                append("\nindex: " + index).
                append("\nfullUrl: " + fullUrl);
        if (thumbnail != null) {
            sb.append(thumbnail.toString());
        }
        if (terms != null) {
            sb.append(terms.toString());
        }
        sb.append("\n");
        return sb.toString();
    }
}
