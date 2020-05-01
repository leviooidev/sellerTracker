package com.techtaro.sellertracker.DBTool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.techtaro.sellertracker.Model.ClsTrackerList;

public class DBInit extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "tracker.db";

    public DBInit(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_TRACKER = "CREATE TABLE " + ClsTrackerList.TABLE + "("
                + ClsTrackerList.KEY_ID  + " INTEGER PRIMARY KEY,"
                + ClsTrackerList.KEY_CODE + " VARCHAR(50), "
                + ClsTrackerList.KEY_DESCRIPTION + " VARCHAR(200), "
                + ClsTrackerList.KEY_COUNTER + " TINYINT(2),  "
                + ClsTrackerList.KEY_STIME + " VARCHAR(200) ) "
                ;

        db.execSQL(CREATE_TABLE_TRACKER);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!

        db.execSQL("DROP TABLE IF EXISTS " + ClsTrackerList.TABLE);

        // Create tables again
        onCreate(db);

    }
}
