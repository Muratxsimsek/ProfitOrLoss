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
                            "TRANSACTION_AMOUNT INTEGER," +
                            "TRANSACTION_CURRENCY TEXT," +
                            "EXCHANGE_AMOUNT INTEGER," +
                            "EXCHANGE_CURRENCY TEXT" +
                        ")"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS EX_TRANSACTION");
        onCreate(db);
    }

    public boolean insertTransaction(Integer transactionAmount, String transactionCurrency, Integer exchangeAmount, String exchangeCurrency) {
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

    public boolean updatePerson(Integer id, String name, String gender, int age) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(PERSON_COLUMN_NAME, name);
//        contentValues.put(PERSON_COLUMN_GENDER, gender);
//        contentValues.put(PERSON_COLUMN_AGE, age);
//        db.update(PERSON_TABLE_NAME, contentValues, PERSON_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deletePerson(Integer id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete(PERSON_TABLE_NAME,
//                PERSON_COLUMN_ID + " = ? ",
//                new String[] { Integer.toString(id) });
        return 0;
    }

    public Cursor getTransaction(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM EX_TRANSACTION  WHERE EX_TRANSACTION_ID = ?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM EX_TRANSACTION", null );
        return res;
    }
}