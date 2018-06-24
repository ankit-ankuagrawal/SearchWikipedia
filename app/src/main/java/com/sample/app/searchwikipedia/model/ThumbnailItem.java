package com.sample.app.searchwikipedia.model;

/**
 * Model class representing Thumbnail object from the json response
 */

public class ThumbnailItem {

    private static final String LOG_TAG = ThumbnailItem.class.getSimpleName();

    private String source;
    private int width;
    private int height;

    public String getSource() {
        return source;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer()
                .append("\nsource: " + source)
                .append(" width: " + width)
                .append(" height: " + height);
        return sb.toString();
    }
}
