package com.example.lesson7;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: PWR
 * Date: 14.01.14
 * Time: 0:55
 * To change this template use File | Settings | File Templates.
 */
public class Entry implements Serializable, Comparable<Entry> {
    private String title;
    private String url;
    private String description;
    private String date;

    public Entry() {}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Entry(String title, String url, String description, String date) {

        this.title = title;
        this.url = url;
        this.description = description;
        this.date = date;
    }

    @Override
    public int compareTo(Entry another) {
        return -this.getDate().compareTo(another.getDate());
    }
}
