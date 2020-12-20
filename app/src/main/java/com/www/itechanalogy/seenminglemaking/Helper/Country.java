package com.www.itechanalogy.seenminglemaking.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Country extends SQLiteOpenHelper {
    private static final String DB_NAME = "country.sqlite";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "countryTable";
    public static final String COUNTRY_NAME = "name";
    public static final String COUNTRY_FLAG = "flag";
    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ( "+COUNTRY_NAME+" VARCHAR, "+COUNTRY_FLAG+" VARCHAR )";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    public Country(Context context){
        super(context,DB_NAME,null,DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
    }
    public void putData(String name, String image, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COUNTRY_NAME,name);
        contentValues.put(COUNTRY_FLAG,image);
        long l = db.insert(TABLE_NAME,null,contentValues);
    }
}
