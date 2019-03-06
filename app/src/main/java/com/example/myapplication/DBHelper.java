package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "CMPT276-1191E1-Delta";

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "wordPairs.db";
    private static final String TABLE_NAME_IMPORT = "wordsImport";
    private static final String TABLE_NAME_WRONG = "WrongWords";

    //private static final String ID = "id";
    private static final String ENG = "English";
    private static final String SPAN = "Spanish";
    private static final String TOTAL = "wrongTotal";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //create two tables
    //one is for My words that keeps track of wrong words from users
    //the other is for users loaded words
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_WRONG + "( English TEXT, Spanish TEXT, wrongTotal INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_IMPORT+ "( English TEXT, Spanish TEXT)");
        Log.d(TAG,"Created");
        Log.i("MYDB", "onCreate " + VERSION);
    }

    //if exists same name tables
    //delete them
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WRONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_IMPORT);
        Log.d(TAG,"deleted");
        Log.i("MYDB", "onUpgrade: old " + oldVersion + " new " + newVersion);
        onCreate(db);
    }
    
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //save words that uploaded by users
    public void importWord (WordsPairs mPairs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENG,mPairs.getENG());
        contentValues.put(SPAN,mPairs.getSPAN());
        db.insert(TABLE_NAME_IMPORT,null,contentValues);
        Log.d(TAG, "addData: Adding " + mPairs.getENG()+ mPairs.getSPAN() + " to " + TABLE_NAME_IMPORT);
    }

    //keep track of words which users get wrong
    public void updateWrongWord (WordsPairs mPairs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENG,mPairs.getENG());
        contentValues.put(SPAN,mPairs.getSPAN());
        contentValues.put(TOTAL,mPairs.getTotal());
        db.insert(TABLE_NAME_WRONG,null,contentValues);
        Log.d(TAG, "addData: Adding " + mPairs.getENG()+ mPairs.getSPAN() + " to " + TABLE_NAME_WRONG);
    }

    //update how many times users get wrong
    public void updateWrongNum(WordsPairs mPairs){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENG, mPairs.getENG());
        contentValues.put(SPAN, mPairs.getSPAN());
        contentValues.put(TOTAL, mPairs.getTotal());
        db.update(TABLE_NAME_WRONG, contentValues, " English =? and Spanish =?",
                new String []{mPairs.getENG(), mPairs.getSPAN()});
    }

    //check if words already exsit in the table
    //return true if it is there
    public boolean hasWord(WordsPairs mPairs){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean hasWord = false;
        Cursor cursor = db.query(TABLE_NAME_WRONG, new String[]{ENG, SPAN},  "English =? and Spanish =?",
                new String[]{mPairs.getENG(), mPairs.getSPAN()},null,null,null);


        if(cursor.getCount() > 0){
            Log.d(TAG, "\"Already Exist!\"" + cursor.getCount());
            hasWord = true;
        }
        cursor.close();
        return hasWord;
    }

    public boolean hasImported(WordsPairs mPairs){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean hasWord = false;
        Cursor data = db.query(TABLE_NAME_IMPORT, new String[]{ENG, SPAN},  "English =? and Spanish =?",
                new String[]{mPairs.getENG(), mPairs.getSPAN()},null,null,null);

        if(data.getCount() > 0){
            Log.d(TAG, "\"Already Exist!\"" + data.getCount());
            hasWord = true;
        }
        data.close();
        return hasWord;
    }

    //return total number that user get wrong
    public int numWrong(WordsPairs mPairs){
        SQLiteDatabase db = this.getWritableDatabase();
        int num = 0;
        Cursor data = db.query(TABLE_NAME_WRONG, new String[]{ENG, SPAN, TOTAL},  "English =? and Spanish =?",
                new String[]{mPairs.getENG(), mPairs.getSPAN()},null,null,null);

        if (data.moveToFirst()) {
            //Loop through the table rows
            do {
                num = Integer.parseInt(data.getString(2));
                Log.d(TAG,"NUM in MUN WRONG IS " + num);

            } while (data.moveToNext());
        }
        data.close();
        return num;
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public void onDestroy(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WRONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_IMPORT);
        onCreate(db);
    }

    //fetch all data
    public ArrayList<WordsPairs> getImportedData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  (English), (Spanish) FROM " + TABLE_NAME_IMPORT;
        Cursor data = db.rawQuery(query, null);

        ArrayList<WordsPairs> words = new ArrayList();
        if (data.moveToFirst()) {
            //Loop through the table rows
            do {
                WordsPairs mPairs = new WordsPairs();
                mPairs.setENG(data.getString(0));
                mPairs.setSPAN(data.getString(1));
                words.add(mPairs);
            } while (data.moveToNext());
        }
        data.close();
        return words;
    }

    public ArrayList<WordsPairs> getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  (English), (Spanish), (wrongTotal) FROM " + TABLE_NAME_WRONG;
        Cursor data = db.rawQuery(query, null);

        ArrayList<WordsPairs> words = new ArrayList();
        if (data.moveToFirst()) {
            //Loop through the table rows
            do {
                WordsPairs mPairs = new WordsPairs();
                mPairs.setENG(data.getString(0));
                mPairs.setSPAN(data.getString(1));
                mPairs.setTotal(Integer.parseInt(data.getString(2)));
                words.add(mPairs);
            } while (data.moveToNext());
        }
        data.close();
        return words;
    }


}

