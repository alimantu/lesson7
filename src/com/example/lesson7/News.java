package com.example.lesson7;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: PWR
 * Date: 14.01.14
 * Time: 0:56
 * To change this template use File | Settings | File Templates.
 */
public class News implements Serializable{
    private ArrayList<Entry> entries = new ArrayList<Entry>();
    News() {}

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    public void add(Entry currentEntry) {
        entries.add(currentEntry);
    }
}
