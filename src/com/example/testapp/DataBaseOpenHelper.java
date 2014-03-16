package com.example.testapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author Administrator
 *Class used to create database
 */

public class DataBaseOpenHelper extends SQLiteOpenHelper {
	
	private static final String PLACES_DB_NAME ="PlacesDB";
	private static final int DATABASE_VERSION = 2;
    private static final String PLACES_TABLE_NAME = "places";
    private static final String PLACES_TABLE_CREATE =
                "CREATE TABLE " + PLACES_TABLE_NAME + " (" +
                "placeNum INTEGER PRIMARY KEY, "+
                "placeName TEXT, " +
                "placeAdd TEXT, " +
                "placeLat REAL, " +
                "placeLng REAL);";

	public DataBaseOpenHelper(Context context) {
		super(context, PLACES_DB_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL(PLACES_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
