package com.example.lesson7;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.lesson7.databases.EntryDatabase;
import com.example.lesson7.databases.RSSDatabase;

public class MyActivity extends Activity {

    private Button button;
    private ListView listView;
    private RSSDatabase database;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        database = new RSSDatabase(this);
        database.open();

        cursor = database.getAllData();
        startManagingCursor(cursor);

        String [] from = new String[] {
            RSSDatabase.COLUMN_TITLE,
            RSSDatabase.COLUMN_URL
        };

        int [] to = new int [] {
            R.id.rssTitle,
            R.id.rssUrl
        };

        adapter = new SimpleCursorAdapter(this, R.layout.adapter, cursor, from, to);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = ((TextView)view.findViewById(R.id.rssTitle)).getText().toString();
                String url = ((TextView)view.findViewById(R.id.rssUrl)).getText().toString();
                Intent intent = new Intent(view.getContext(), NewsActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("url", url);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);    //To change body of overridden methods use File | Settings | File Templates.
        menu.add(0, 1, 0, "DELETE");
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == 1) {
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            database.deleteChannel((int) adapterContextMenuInfo.id);

            EntryDatabase entryDatabase = new EntryDatabase(this);
            entryDatabase.open();
            entryDatabase.deleteChannel(adapterContextMenuInfo.id);
            entryDatabase.close();

            cursor.requery();
            return true;
        }
        return false;
    }
}
