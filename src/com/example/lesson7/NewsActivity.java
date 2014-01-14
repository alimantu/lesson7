package com.example.lesson7;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.example.lesson7.databases.EntryDatabase;
import com.example.lesson7.databases.RSSDatabase;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: PWR
 * Date: 14.01.14
 * Time: 0:30
 * To change this template use File | Settings | File Templates.
 */
public class NewsActivity extends Activity {
    private ListView listView;
    private EntryDatabase database;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    public static String currentRssUrl;
    private Intent intentService;
    private NewsBroadcast broadcast;
    private IntentFilter intentFilter;
    private ArrayList<Entry> current = new ArrayList<Entry>();
    private long id;
    private boolean isUpdate;
    private ImageButton imageButton;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.news_layout);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        currentRssUrl = intent.getStringExtra("url");
        id = intent.getLongExtra("id", 0);

        TextView textView = (TextView) findViewById(R.id.newsMainTitle);
        textView.setText(title);

        intentService = new Intent(this, NewsService.class);
        intentService.putExtra("url", currentRssUrl);
        startService(intentService);

        broadcast = new NewsBroadcast();
        intentFilter = new IntentFilter(NewsService.KEY);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(broadcast, intentFilter);
        listView = (ListView) findViewById(R.id.newsView);

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewsService.class);
                intent.putExtra("url", currentRssUrl);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                startService(intent);
                imageButton.setEnabled(false);
                isUpdate = true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = current.get(position).getUrl();
                String desc = current.get(position).getDescription();
                Intent intent = new Intent(view.getContext(), WebActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("summary", desc);
                startActivity(intent);
            }
        });
    }

    public class NewsBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            News result = (News) intent.getSerializableExtra("news");
            if (result != null) {
                EntryDatabase entryDatabase = new EntryDatabase(context);
                entryDatabase.open();
                entryDatabase.deleteChannel(id);
                current.clear();
                Collections.sort(result.getEntries());
                for (int i = 0; i < result.getEntries().size(); i++) {
                    Entry currentEntry = result.getEntries().get(i);
                    if (currentEntry == null)
                        continue;
                    entryDatabase.addEntry(id, currentEntry.getTitle(), currentEntry.getUrl(), currentEntry.getDescription(), currentEntry.getDate());
                    current.add(currentEntry);
                }
                Collections.sort(current);
                updateNewsChannels();
            }

        }
    }

    @SuppressWarnings("deprecation")
    void updateNewsChannels() {
        if (isUpdate) {
            isUpdate = false;
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Лента успешна обновлена",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            imageButton.setEnabled(true);
            toast.show();
        }
        database = new EntryDatabase(this);
        database.open();

        cursor = database.getChannelData(id);
        startManagingCursor(cursor);

        String [] from = new String[] {
                EntryDatabase.COLUMN_TITLE,
                EntryDatabase.COLUMN_DATE
        };

        int [] to = new int [] {
                R.id.newsTitle,
                R.id.newsDate
        };

        adapter = new SimpleCursorAdapter(this, R.layout.news_adapter, cursor, from, to);
        listView.setAdapter(adapter);
    }

    protected void onPause() {
        unregisterReceiver(broadcast);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerReceiver(broadcast, intentFilter);
        super.onResume();
    }
}
