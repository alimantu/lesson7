package com.example.lesson7;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created with IntelliJ IDEA.
 * User: PWR
 * Date: 14.01.14
 * Time: 0:48
 * To change this template use File | Settings | File Templates.
 */
public class NewsService extends IntentService {

    public static final String KEY = "com.example.lesson7.NewsService";
    private static final long TIME = 300000;
    private News result;

    public NewsService() {
        super("NewsService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String currentUrl = intent.getStringExtra("url");
        if (!currentUrl.equals(NewsActivity.currentRssUrl))
            return;
        result = null;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(currentUrl));
            HttpEntity httpEntity = httpResponse.getEntity();

            String xml = EntityUtils.toString(httpEntity);
            InputSource is = new InputSource(new StringReader(xml));

            NewsDownload handler = new NewsDownload();
            parser.parse(is, handler);

            result = handler.getResult();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClientProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Intent newIntent = new Intent();
        newIntent.setAction(KEY);
        newIntent.addCategory(Intent.CATEGORY_DEFAULT);
        newIntent.putExtra("news", result);
        sendBroadcast(newIntent);

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + TIME, TIME, pi);
    }
}
