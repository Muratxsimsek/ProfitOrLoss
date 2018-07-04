package com.sample.foo.sqliteexample;

import android.content.Intent;
import android.database.Cursor;
//import android.support.v7.app.ActionBarActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";
    ExampleDBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ExampleDBHelper(this);
        Cursor cursor = dbHelper.getCurrencies();

        EditText usd = (EditText) findViewById(R.id.textViewCurrentUSD_TLRationValue);
        EditText eur = (EditText) findViewById(R.id.textViewCurrentEUR_TLRationValue);
        EditText gld = (EditText) findViewById(R.id.textViewCurrentGLD_TLRationValue);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            usd.setText(Double.valueOf(cursor.getDouble(cursor.getColumnIndex("USD_TL"))).toString());
            eur.setText(Double.valueOf(cursor.getDouble(cursor.getColumnIndex("EUR_TL"))).toString());
            gld.setText(Double.valueOf(cursor.getDouble(cursor.getColumnIndex("GLD_TL"))).toString());
        }

        Button button = (Button) findViewById(R.id.listbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra(KEY_EXTRA_CONTACT_ID, 0);
                startActivity(intent);
            }
        });

        Button buttonSave = (Button) findViewById(R.id.savebutton);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usd = (EditText) findViewById(R.id.textViewCurrentUSD_TLRationValue);
                EditText eur = (EditText) findViewById(R.id.textViewCurrentEUR_TLRationValue);
                EditText gld = (EditText) findViewById(R.id.textViewCurrentGLD_TLRationValue);

                ExampleDBHelper dbHelper = new ExampleDBHelper(MainActivity.this);

                dbHelper.updateCurrencies(Double.valueOf(usd.getText().toString()),Double.valueOf(eur.getText().toString()),Double.valueOf(gld.getText().toString()));
                Toast.makeText(getApplicationContext(), "Currencies Updated!", Toast.LENGTH_SHORT).show();

                evaluateProfitOrLoss();



            }
        });



    }

    private void evaluateProfitOrLoss(){

        EditText usd = (EditText) findViewById(R.id.textViewCurrentUSD_TLRationValue);
        EditText eur = (EditText) findViewById(R.id.textViewCurrentEUR_TLRationValue);
        EditText gld = (EditText) findViewById(R.id.textViewCurrentGLD_TLRationValue);

        Double profitOrLoss = 0.0;
        TextView totalUsdView = (TextView) findViewById(R.id.textViewTotalUSD);
        TextView totalEurView = (TextView) findViewById(R.id.textViewTotalEUR);
        TextView totalGldView = (TextView) findViewById(R.id.textViewTotalGLD);
        TextView totalTryView = (TextView) findViewById(R.id.textViewCurrentTotalTLValue);
        TextView profitOrLossView = (TextView) findViewById(R.id.textViewProfitOrLossTLValue);



        profitOrLoss = Double.valueOf(NumberTextWatcher.trimCommaOfString(totalUsdView.getText().toString()))*Double.valueOf(NumberTextWatcher.trimCommaOfString(usd.getText().toString())) +
                Double.valueOf(NumberTextWatcher.trimCommaOfString(totalEurView.getText().toString()))*Double.valueOf(NumberTextWatcher.trimCommaOfString(eur.getText().toString())) +
                Double.valueOf(NumberTextWatcher.trimCommaOfString(totalGldView.getText().toString()))*Double.valueOf(NumberTextWatcher.trimCommaOfString(gld.getText().toString()));

        profitOrLoss = profitOrLoss - Double.valueOf(NumberTextWatcher.trimCommaOfString(totalTryView.getText().toString()));
        if(profitOrLoss>0){
            profitOrLossView.setTextColor(Color.GREEN);
            profitOrLossView.setText("PROFIT : " + NumberTextWatcher.getDecimalFormat(profitOrLoss.toString()));
        }
        else{
            profitOrLossView.setTextColor(Color.RED);
            profitOrLossView.setText("LOSS : " + NumberTextWatcher.getDecimalFormat(profitOrLoss.toString()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Cursor curAllTransactions = dbHelper.getAllTransactions();

        Double totalTry=0.0;
        Double totalUsd=0.0;
        Double totalEur=0.0;
        Double totalGld=0.0;
        while (curAllTransactions.moveToNext()) {
            if(curAllTransactions.getString(curAllTransactions.getColumnIndex("TRANSACTION_CURRENCY")).equals("TRY") &&
                    curAllTransactions.getString(curAllTransactions.getColumnIndex("EXCHANGE_CURRENCY")).equals("USD")     ){
                totalUsd += curAllTransactions.getDouble(curAllTransactions.getColumnIndex("EXCHANGE_AMOUNT"));
                totalTry += curAllTransactions.getDouble(curAllTransactions.getColumnIndex("TRANSACTION_AMOUNT"));
            }

            if(curAllTransactions.getString(curAllTransactions.getColumnIndex("TRANSACTION_CURRENCY")).equals("USD") &&
                    curAllTransactions.getString(curAllTransactions.getColumnIndex("EXCHANGE_CURRENCY")).equals("TRY")     ){
                totalUsd -= curAllTransactions.getDouble(curAllTransactions.getColumnIndex("TRANSACTION_AMOUNT"));
                totalTry -= curAllTransactions.getDouble(curAllTransactions.getColumnIndex("TRANSACTION_AMOUNT"));
            }

            if(curAllTransactions.getString(curAllTransactions.getColumnIndex("TRANSACTION_CURRENCY")).equals("TRY") &&
                    curAllTransactions.getString(curAllTransactions.getColumnIndex("EXCHANGE_CURRENCY")).equals("EUR")     ){
                totalEur += curAllTransactions.getDouble(curAllTransactions.getColumnIndex("EXCHANGE_AMOUNT"));
                totalTry += curAllTransactions.getDouble(curAllTransactions.getColumnIndex("TRANSACTION_AMOUNT"));
            }

            if(curAllTransactions.getString(curAllTransactions.getColumnIndex("TRANSACTION_CURRENCY")).equals("EUR") &&
                    curAllTransactions.getString(curAllTransactions.getColumnIndex("EXCHANGE_CURRENCY")).equals("TRY")     ){
                totalEur -= curAllTransactions.getDouble(curAllTransactions.getColumnIndex("TRANSACTION_AMOUNT"));
                totalTry -= curAllTransactions.getDouble(curAllTransactions.getColumnIndex("TRANSACTION_AMOUNT"));
            }

            if(curAllTransactions.getString(curAllTransactions.getColumnIndex("TRANSACTION_CURRENCY")).equals("TRY") &&
                    curAllTransactions.getString(curAllTransactions.getColumnIndex("EXCHANGE_CURRENCY")).equals("GLD")     ){
                totalGld += curAllTransactions.getDouble(curAllTransactions.getColumnIndex("EXCHANGE_AMOUNT"));
                totalTry += curAllTransactions.getDouble(curAllTransactions.getColumnIndex("TRANSACTION_AMOUNT"));
            }

            if(curAllTransactions.getString(curAllTransactions.getColumnIndex("TRANSACTION_CURRENCY")).equals("GLD") &&
                    curAllTransactions.getString(curAllTransactions.getColumnIndex("EXCHANGE_CURRENCY")).equals("TRY")     ){
                totalGld -= curAllTransactions.getDouble(curAllTransactions.getColumnIndex("TRANSACTION_AMOUNT"));
                totalTry -= curAllTransactions.getDouble(curAllTransactions.getColumnIndex("TRANSACTION_AMOUNT"));
            }
        }

        TextView totalUsdView = (TextView) findViewById(R.id.textViewTotalUSD);
        TextView totalEurView = (TextView) findViewById(R.id.textViewTotalEUR);
        TextView totalGldView = (TextView) findViewById(R.id.textViewTotalGLD);
        TextView totalTryView = (TextView) findViewById(R.id.textViewCurrentTotalTLValue);

        totalUsdView.setText(NumberTextWatcher.getDecimalFormat(totalUsd.toString()));
        totalEurView.setText(NumberTextWatcher.getDecimalFormat(totalEur.toString()));
        totalGldView.setText(NumberTextWatcher.getDecimalFormat(totalGld.toString()));
        totalTryView.setText(NumberTextWatcher.getDecimalFormat(totalTry.toString()));

        evaluateProfitOrLoss();
    }
}