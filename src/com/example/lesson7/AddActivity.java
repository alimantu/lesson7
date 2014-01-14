package com.example.lesson7;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.lesson7.databases.RSSDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: PWR
 * Date: 14.01.14
 * Time: 0:19
 * To change this template use File | Settings | File Templates.
 */
public class AddActivity extends Activity {
    private RSSDatabase database;
    private EditText title, url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.add_activity);

        database = new RSSDatabase(this);
        database.open();

        title = (EditText) findViewById(R.id.editText1);
         url = (EditText) findViewById(R.id.editText2);
        Button button = (Button) findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rssTitle = title.getText().toString();
                String rssUrl = url.getText().toString();
                database.addChannel(rssTitle, rssUrl);
                Intent intent = new Intent(v.getContext(), MyActivity.class);
                startActivity(intent);
            }
        });
    }
}
