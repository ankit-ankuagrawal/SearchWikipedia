package com.appsnesst.searchwikipedia.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing Terms object from the json response
 */

public class TermsItem {

    private static final String LOG_TAG = TermsItem.class.getSimpleName();

    ArrayList<String> description;

    public List getDescription() {
        return description;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("\n description: ")
                .append(description);
        return sb.toString();
    }
}
