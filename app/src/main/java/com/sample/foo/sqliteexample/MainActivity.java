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

    private ListView listView;
    ExampleDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton button = (ImageButton) findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateOrEditActivity.class);
                intent.putExtra(KEY_EXTRA_CONTACT_ID, 0);
                startActivity(intent);
            }
        });

        dbHelper = new ExampleDBHelper(this);

        final Cursor cursor = dbHelper.getAllTransactions();
        String [] columns = new String[] {
                "CDATE",
                "TRANSACTION_AMOUNT",
                "TRANSACTION_CURRENCY",
                "EXCHANGE_AMOUNT",
                "EXCHANGE_CURRENCY"
        };
        int [] widgets = new int[] {
                R.id.cdate,
                R.id.transactionAmount,
                R.id.transactionCurrency,
                R.id.exchangeAmount,
                R.id.exchangeCurrency
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_view,
                cursor, columns, widgets, 0);
        listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(cursorAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> listView, View view,
//                                    int position, long id) {
//                Cursor itemCursor = (Cursor) MainActivity.this.listView.getItemAtPosition(position);
//                int personID = itemCursor.getInt(itemCursor.getColumnIndex(ExampleDBHelper.PERSON_COLUMN_ID));
//                Intent intent = new Intent(getApplicationContext(), CreateOrEditActivity.class);
//                intent.putExtra(KEY_EXTRA_CONTACT_ID, personID);
//                startActivity(intent);
//            }
//        });

    }

}