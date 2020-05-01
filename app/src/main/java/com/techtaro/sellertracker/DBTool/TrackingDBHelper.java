package com.techtaro.sellertracker.DBTool;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.techtaro.sellertracker.Model.ClsTrackerList;

import java.util.LinkedList;
import java.util.List;

public class TrackingDBHelper {
    private Context c;
    private SQLiteDatabase db;
    private DBInit dbTracker;

    public TrackingDBHelper(Context ctx)
    {
        this.c=ctx;
        dbTracker=new DBInit(c);
    }

    //OPEN DB
    public TrackingDBHelper openDB()
    {
        try
        {
            db=dbTracker.getWritableDatabase();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return this;
    }

    //CLOSE
    public void close()
    {
        try
        {
            dbTracker.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public long addScannedJob(String strScannedCode)
    {
        try
        {

            SQLiteDatabase db = dbTracker.getWritableDatabase();
            //Execute sql query to remove from database
            //NOTE: When removing by String in SQL, value must be enclosed with ''
            db.execSQL("INSERT INTO TrackerList (code,counter,stime)\n" +
                    "VALUES ('"+strScannedCode+"',\"1\",strftime('%Y-%m-%d %H:%M:%S','now'))");


            //Close the database
            db.close();

            return 1;

        }catch (SQLException e)
        {
            e.printStackTrace();
        }


        return 0;
    }



    public long deleteScannedJob(String strScannedCode)
    {
        try
        {

            SQLiteDatabase db = dbTracker.getWritableDatabase();
            //Execute sql query to remove from database
            //NOTE: When removing by String in SQL, value must be enclosed with ''
            db.execSQL("DELETE FROM TrackerList WHERE code = "+strScannedCode+"");


            //Close the database
            db.close();

            return 1;

        }catch (SQLException e)
        {
            e.printStackTrace();
        }


        return 0;
    }

    /**Query records, give options to filter results**/
    public List<ClsTrackerList> trackerList(String strSearch, String strDateFrom, String strDateTo) {
        String query;

        query = "SELECT * FROM TrackerList WHERE code LIKE '%"+strSearch+"%' AND (stime BETWEEN '"+strDateFrom+" 00:00:00' AND '"+strDateTo+" 23:59:59') ORDER BY stime DESC;";


        List<ClsTrackerList> trackingLinkedList = new LinkedList<>();
        SQLiteDatabase db = dbTracker.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ClsTrackerList clsTrackerList;

        if (cursor.moveToFirst()) {
            do {
                clsTrackerList = new ClsTrackerList();

                clsTrackerList.setId(cursor.getString(cursor.getColumnIndex(ClsTrackerList.KEY_ID)));
                clsTrackerList.setCode(cursor.getString(cursor.getColumnIndex(ClsTrackerList.KEY_CODE)));
                clsTrackerList.setDescription(cursor.getString(cursor.getColumnIndex(ClsTrackerList.KEY_DESCRIPTION)));
                clsTrackerList.setCounter(cursor.getString(cursor.getColumnIndex(ClsTrackerList.KEY_COUNTER)));
                clsTrackerList.setStime(cursor.getString(cursor.getColumnIndex(ClsTrackerList.KEY_STIME)));
                trackingLinkedList.add(clsTrackerList);
            } while (cursor.moveToNext());
        }


        return trackingLinkedList;
    }
}
