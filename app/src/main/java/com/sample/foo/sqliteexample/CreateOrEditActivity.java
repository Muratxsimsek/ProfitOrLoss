package com.sample.foo.sqliteexample;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;


public class CreateOrEditActivity extends AppCompatActivity implements View.OnClickListener {


    private ExampleDBHelper dbHelper ;
    EditText dateEditText;
    EditText transactionAmountEditText;
    Spinner transactionCurrencySpinner;
    EditText exchangeAmountEditText;
    Spinner exchangeCurrencySpinner;

    Button saveButton;

    Button editButton, deleteButton;

    int transactionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transactionID = getIntent().getIntExtra(MainActivity.KEY_EXTRA_CONTACT_ID, 0);

        setContentView(R.layout.activity_edit);

        dateEditText = (EditText) findViewById(R.id.editDate);
        transactionAmountEditText = (EditText) findViewById(R.id.editTextTransactionAmount);
        transactionCurrencySpinner = (Spinner) findViewById(R.id.spinnerTransactionCurrency);
        exchangeAmountEditText = (EditText) findViewById(R.id.editTextExchangeAmount);
        exchangeCurrencySpinner = (Spinner) findViewById(R.id.spinnerExchangeCurrency);

        transactionAmountEditText.addTextChangedListener(new NumberTextWatcher(transactionAmountEditText));
        exchangeAmountEditText.addTextChangedListener(new NumberTextWatcher(exchangeAmountEditText));

        ArrayAdapter<String> adapterCurrency = new ArrayAdapter<String>(CreateOrEditActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.currency));
        adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transactionCurrencySpinner.setAdapter(adapterCurrency);

        exchangeCurrencySpinner.setAdapter(adapterCurrency);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

//        editButton = (Button) findViewById(R.id.editButton);
//        editButton.setOnClickListener(this);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        dbHelper = new ExampleDBHelper(this);

        dateEditText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
                        final Calendar takvim = Calendar.getInstance();
                        int yil = takvim.get(Calendar.YEAR);
                        int ay = takvim.get(Calendar.MONTH);
                        int gun = takvim.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dpd = new DatePickerDialog(CreateOrEditActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        // ay değeri 0 dan başladığı için (Ocak=0, Şubat=1,..,Aralık=11)
                                        // değeri 1 artırarak gösteriyoruz.
                                        month += 1;
                                        // year, month ve dayOfMonth değerleri seçilen tarihin değerleridir.
                                        // Edittextte bu değerleri gösteriyoruz.
                                        dateEditText.setText(String.format("%02d", dayOfMonth) + "." + String.format("%02d", month) + "." + year);
                                    }
                                }, yil, ay, gun);
                        // datepicker açıldığında set edilecek değerleri buraya yazıyoruz.
                        // şimdiki zamanı göstermesi için yukarda tanmladığımz değşkenleri kullanyoruz.

                        // dialog penceresinin button bilgilerini ayarlıyoruz ve ekranda gösteriyoruz.
                        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                        dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                        dpd.show();

                    }
                }
        );
//
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        if(transactionID > 0) {

            deleteButton.setVisibility(View.VISIBLE);

            Cursor rs = dbHelper.getTransaction(transactionID);
            rs.moveToFirst();
            String date = rs.getString(rs.getColumnIndex("CDATE"));
            Double transactionAmount = rs.getDouble(rs.getColumnIndex("TRANSACTION_AMOUNT"));
            String transactionCurrency = rs.getString(rs.getColumnIndex("TRANSACTION_CURRENCY"));
            Double exchangeAmount = rs.getDouble(rs.getColumnIndex("EXCHANGE_AMOUNT"));
            String exchangeCurrency = rs.getString(rs.getColumnIndex("EXCHANGE_CURRENCY"));

            if (!rs.isClosed()) {
                rs.close();
            }

            dateEditText.setText(date);
            dateEditText.setFocusable(false);
            dateEditText.setClickable(false);

            transactionAmountEditText.setText(transactionAmount.toString());
//            transactionAmountEditText.setFocusable(false);
//            transactionAmountEditText.setClickable(false);

            selectSpinnerItemByValue(transactionCurrencySpinner,transactionCurrency);
//            transactionCurrencySpinner.setFocusable(false);
//            transactionCurrencySpinner.setClickable(false);

            exchangeAmountEditText.setText(exchangeAmount.toString());
//            exchangeAmountEditText.setFocusable(false);
//            exchangeAmountEditText.setClickable(false);

            selectSpinnerItemByValue(exchangeCurrencySpinner,exchangeCurrency);
//            exchangeCurrencySpinner.setFocusable(false);
//            exchangeCurrencySpinner.setClickable(false);
        }
        else{

            deleteButton.setVisibility(View.GONE);

        }
    }

    public static void selectSpinnerItemByValue(Spinner spnr, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if(adapter.getItem(position).equals(value)) {
                spnr.setSelection(position);
                return;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                persistPerson();
                return;
//            case R.id.editButton:
//                saveButton.setVisibility(View.VISIBLE);
//                buttonLayout.setVisibility(View.GONE);
////                nameEditText.setEnabled(true);
////                nameEditText.setFocusableInTouchMode(true);
////                nameEditText.setClickable(true);
////
////                genderEditText.setEnabled(true);
////                genderEditText.setFocusableInTouchMode(true);
////                genderEditText.setClickable(true);
////
////                ageEditText.setEnabled(true);
////                ageEditText.setFocusableInTouchMode(true);
////                ageEditText.setClickable(true);
//                return;
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deletePerson)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deleteTransaction(transactionID);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Delete Transaction ?");
                d.show();
                return;
        }
    }

    public void persistPerson() {
        if(transactionID > 0) {
            if(dbHelper.updateTransaction(transactionID,
                    dateEditText.getText().toString(),
                    Double.parseDouble(NumberTextWatcher.trimCommaOfString(transactionAmountEditText.getText().toString())),
                    transactionCurrencySpinner.getSelectedItem().toString(),
                    Double.parseDouble(NumberTextWatcher.trimCommaOfString(exchangeAmountEditText.getText().toString())),
                    exchangeCurrencySpinner.getSelectedItem().toString())) {

                Toast.makeText(getApplicationContext(), "Transaction Update Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Transaction Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(dbHelper.insertTransaction(Double.parseDouble(NumberTextWatcher.trimCommaOfString(transactionAmountEditText.getText().toString())),
                    transactionCurrencySpinner.getSelectedItem().toString(),
                    Double.parseDouble(NumberTextWatcher.trimCommaOfString(exchangeAmountEditText.getText().toString())),
                    exchangeCurrencySpinner.getSelectedItem().toString()
                    )) {
                Toast.makeText(getApplicationContext(), "Transaction Inserted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Could not Transaction", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


}
