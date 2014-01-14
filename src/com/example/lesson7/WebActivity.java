package com.example.lesson7;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created with IntelliJ IDEA.
 * User: PWR
 * Date: 14.01.14
 * Time: 1:55
 * To change this template use File | Settings | File Templates.
 */
public class WebActivity extends Activity {

    private WebView browser;
    private String link;
    private String summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        Intent intent = getIntent();
        link = intent.getStringExtra("url");
        summary = intent.getStringExtra("summary");
        browser = (WebView) findViewById(R.id.webView);
        if (summary.length() > 0) {
            browser.loadData(summary, "text/html; charset=UTF-8", null);
        } else {
            browser.loadUrl(link);
        }
    }

}
