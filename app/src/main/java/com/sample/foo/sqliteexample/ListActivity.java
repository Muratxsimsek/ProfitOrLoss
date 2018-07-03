package com.sample.foo.sqliteexample;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by msreactjs on 2018-07-03.
 */

public class ListActivity extends AppCompatActivity {

    private int transactionID;
    private ListView listView;
    private ExampleDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transactionID = getIntent().getIntExtra(MainActivity.KEY_EXTRA_CONTACT_ID, 0);

        setContentView(R.layout.activity_list);

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
        listView = (ListView)findViewById(R.id.customlist);
        listView.setAdapter(cursorAdapter);


        Button button = (Button) findViewById(R.id.addbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, CreateOrEditActivity.class);

                startActivity(intent);
            }
        });



    }
}
