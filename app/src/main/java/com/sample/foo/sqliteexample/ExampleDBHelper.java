package com.sample.foo.sqliteexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by obaro on 02/04/2015.
 */
public class ExampleDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SQLiteExample.db";
    private static final int DATABASE_VERSION = 2;

    public ExampleDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS EX_TRANSACTION" +
                        "(" +
                            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "CDATE TEXT," +
                            "TRANSACTION_AMOUNT REAL," +
                            "TRANSACTION_CURRENCY TEXT," +
                            "EXCHANGE_AMOUNT REAL," +
                            "EXCHANGE_CURRENCY TEXT" +
                        ")"

        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS PRESENT_CURRENCY" +
                        "(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "CDATE TEXT," +
                        "USD_TL REAL," +
                        "EUR_TL REAL," +
                        "GLD_TL REAL" +
                        ")"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS EX_TRANSACTION");
        onCreate(db);
    }

    public boolean insertTransaction(Double transactionAmount, String transactionCurrency, Double exchangeAmount, String exchangeCurrency) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String date = df.format(new Date().getTime());

        contentValues.put("CDATE", date);
        contentValues.put("TRANSACTION_AMOUNT", transactionAmount);
        contentValues.put("TRANSACTION_CURRENCY", transactionCurrency);
        contentValues.put("EXCHANGE_AMOUNT", exchangeAmount);
        contentValues.put("EXCHANGE_CURRENCY", exchangeCurrency);

        db.insert("EX_TRANSACTION", null, contentValues);
        return true;
    }

//    public int numberOfRows() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        int numRows = (int) DatabaseUtils.queryNumEntries(db, PERSON_TABLE_NAME);
//        return numRows;
//    }

    public boolean updateTransaction(Integer id, String cdate, Double transactionAmount, String transactionCurrency,Double exchangeAmount,String exchangeCurrency) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CDATE", cdate);
        contentValues.put("TRANSACTION_AMOUNT", transactionAmount);
        contentValues.put("TRANSACTION_CURRENCY", transactionCurrency);
        contentValues.put("EXCHANGE_AMOUNT", exchangeAmount);
        contentValues.put("EXCHANGE_CURRENCY", exchangeCurrency);
        db.update("EX_TRANSACTION", contentValues, "_id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteTransaction(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("EX_TRANSACTION",
                "_id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Cursor getTransaction(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM EX_TRANSACTION  WHERE _id = ?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM EX_TRANSACTION", null );
        return res;
    }

    public Cursor getCurrencies() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM PRESENT_CURRENCY", new String[]{});
        return res;
    }

    public void updateCurrencies(Double usd,Double eur,Double gld) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM PRESENT_CURRENCY", new String[]{});
        SQLiteDatabase dbWritable = this.getWritableDatabase();
        cursor.moveToFirst();

        if(cursor.getCount() > 0){

            ContentValues contentValues = new ContentValues();

            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            String date = df.format(new Date().getTime());

            contentValues.put("CDATE", date);
            contentValues.put("USD_TL", usd);
            contentValues.put("EUR_TL", eur);
            contentValues.put("GLD_TL", gld);

            dbWritable.update("PRESENT_CURRENCY", contentValues, "_id = ?", new String[] { Integer.toString(cursor.getInt(cursor.getColumnIndex("_id"))) } );
        }
        else{

            ContentValues contentValues = new ContentValues();


            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            String date = df.format(new Date().getTime());

            contentValues.put("CDATE", date);
            contentValues.put("USD_TL", usd);
            contentValues.put("EUR_TL", eur);
            contentValues.put("GLD_TL", gld);

            dbWritable.insert("PRESENT_CURRENCY", null, contentValues);
        }
    }
}