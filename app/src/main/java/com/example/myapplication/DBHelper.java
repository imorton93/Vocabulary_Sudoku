package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "CMPT276-1191E1-Delta";

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "wordPairs.db";
    private static final String TABLE_NAME = "EngSpanWords";

    private static final String ENG = "English";
    private static final String SPAN = "Spanish";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +  " ( English TEXT, Spanish TEXT)");
        Log.d(TAG,"Created");
        Log.i("MYDB", "onCreate " + VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        Log.d(TAG,"deleted");
        Log.i("MYDB", "onUpgrade: old " + oldVersion + " new " + newVersion);
        onCreate(db);
    }
    
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean updateWordPairs (String Eng, String Span) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENG,Eng);
        contentValues.put(SPAN,Span);
        db.insert(TABLE_NAME,null,contentValues);
        Log.d(TAG, "addData: Adding " + Eng+ Span + " to " + TABLE_NAME);
        return true;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public void onDestroy(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<String> getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT (English), (Spanish) FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);

        ArrayList<String> itemIds = new ArrayList<>();
        if (data.moveToFirst()) {
            //Loop through the table rows
            do {
               itemIds.add(data.getString(0));
               itemIds.add(data.getString(1));

            } while (data.moveToNext());
        }
        data.close();
        return itemIds;
    }

}

