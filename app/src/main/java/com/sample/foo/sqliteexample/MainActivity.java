package com.sample.foo.sqliteexample;

import android.content.Intent;
import android.database.Cursor;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends AppCompatActivity {
    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.listbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra(KEY_EXTRA_CONTACT_ID, 0);
                startActivity(intent);
            }
        });


    }

}