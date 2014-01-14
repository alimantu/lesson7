package com.example.lesson7;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: PWR
 * Date: 14.01.14
 * Time: 1:01
 * To change this template use File | Settings | File Templates.
 */
public class NewsDownload extends DefaultHandler {
    public static final int ITEM = 0;
    public static final int TITLE = 1;
    public static final int DESC = 2;
    public static final int DATE = 3;
    public static final int URL = 4;

    private News result;
    private StringBuilder builder;
    private Entry currentEntry;
    private Stack<Integer> newsStack;
    private int state;
    public News getResult() {
        return result;
    }

    @Override
    public void startDocument() throws SAXException {
        currentEntry = new Entry();
        result = new News();
        newsStack = new Stack<Integer>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("entry") || qName.equals("item")) {
            state = ITEM;
            newsStack.push(state);
        currentEntry = new Entry();
        } else {
            if (qName.equals("title")) {
                state = TITLE;
            } else if (qName.equals("description") || qName.equals("summary")) {
                state = DESC;
            } else if (qName.equals("link")) {
                state = URL;
            } else if (qName.equals("pubDate") || qName.equals("published"))
                state = DATE;
            else
                state = Integer.MAX_VALUE;
            builder = new StringBuilder();
            newsStack.push(state);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        int stackTopElement = newsStack.peek();
        if (stackTopElement == ITEM) {
            result.add(currentEntry);
            currentEntry = null;
        } else if (stackTopElement == TITLE)
            currentEntry.setTitle(builder.toString());
        else if (stackTopElement == URL)
            currentEntry.setUrl(builder.toString());
        else if (stackTopElement == DESC)
            currentEntry.setDescription(builder.toString());
        else if (stackTopElement == DATE)
            currentEntry.setDate(builder.toString());
        newsStack.pop();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        builder.append(ch, start, length);
    }

    @Override
    public void endDocument() throws SAXException {
    }
}
