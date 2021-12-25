package com.pradeep.app;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.util.Log;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class LocalDatabaseInter {
    private static LocalDatabaseInter mInstance;
    private final static String TAG = "LocalDatabaseInter";
    private final static String mPath = "/data/data/com.pradeep.app/localDatabase.db";
    private final static String CREATE_TABLE = "CREATE TABLE DishData ( id INTEGER NOT NULL, dishName TEXT, dishImgURL TEXT, shareDishURL TEXT," +
            " wikiDishURL TEXT, moreImgURLList TEXT,endTime TEXT);";
    private LocalDatabaseInter(Context context) {
        File data = new File(mPath);
        if(!data.exists()) {
            new myDbHelper(context,mPath).getWritableDatabase().close();
        }
    }
    public static LocalDatabaseInter getinstance(Context context) {
        synchronized (LocalDatabaseInter.class) {
            if (mInstance == null) {
                Log.e(TAG,"new database");
                mInstance = new LocalDatabaseInter(context);
            }
        }
        return mInstance;
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);

    }

    public void getData(Parcel data)  {
        data.setDataPosition(0);
        SQLiteDatabase mydatabase = null;
        Cursor cursor = null;
        String insertQuery = "SELECT * FROM DishData;";
        try {
            mydatabase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
            Log.v(TAG, "default now query = " + insertQuery);
            cursor = mydatabase.rawQuery(insertQuery, null);
            if (cursor != null && cursor.getCount() > 0) {
                data.writeInt(cursor.getCount());
                while (cursor.moveToNext()) {
                    int ID = cursor.getInt(cursor.getColumnIndex("ID"));
                    data.writeInt(ID);
                    String dishName = cursor.getString(cursor.getColumnIndex("dishName"));
                    data.writeString(dishName);
                    String dishImgURL = cursor.getString(cursor.getColumnIndex("dishImgURL"));
                    data.writeString(dishImgURL);
                    String shareDishURL = cursor.getString(cursor.getColumnIndex("shareDishURL"));
                    data.writeString(shareDishURL);
                    String wikiDishURL = cursor.getString(cursor.getColumnIndex("wikiDishURL"));
                    data.writeString(wikiDishURL);
                    String moreImgURLList = cursor.getString(cursor.getColumnIndex("moreImgURLList"));
                    data.writeString(moreImgURLList);
                }
            }
            cursor.close();
            mydatabase.close();
        } catch (SQLiteException e) {
            if (cursor != null) {
                cursor.close();
            }
            if (mydatabase != null) {
                mydatabase.close();
            }
        }
    }

    public void addData(Parcel data)  {
        data.setDataPosition(0);
        int id = data.readInt();
        String dishName = data.readString();
        String dishImgURL = data.readString();
        String shareDishURL = data.readString();
        String wikiDishURL = data.readString();
        String moreImgURLList = data.readString();
        String endTime = getDateTime();
        SQLiteDatabase mydatabase = null;
        Cursor cursor = null;
        String insertQuery = "INSERT INTO DishData (id,dishName,dishImgURL,shareDishURL,wikiDishURL,moreImgURLList,endTime) Values("+id+
                ","+dishName+","+dishImgURL+","+shareDishURL+","+wikiDishURL+","+moreImgURLList+","+endTime+");";
        try {
            mydatabase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
            Log.v(TAG, "default now query = " + insertQuery);
            cursor = mydatabase.rawQuery(insertQuery, null);
            cursor.moveToNext();
            cursor.close();
            mydatabase.close();
        } catch (SQLiteException e) {
            if (cursor != null) {
                cursor.close();
            }
            if (mydatabase != null) {
                mydatabase.close();
            }
        }
    }

    public void checkOlderDataBase() {
        SQLiteDatabase mydatabase = null;
        Cursor cursor = null;
        String insertQuery = "DELETE FROM DishData WHERE  endTime>='"+getDateTime()+"'";
        try {
            mydatabase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
            Log.v(TAG, "default now query = " + insertQuery);
            cursor = mydatabase.rawQuery(insertQuery, null);
            cursor.moveToNext();
            cursor.close();
            mydatabase.close();
        } catch (SQLiteException e) {
            if (cursor != null) {
                cursor.close();
            }
            if (mydatabase != null) {
                mydatabase.close();
            }
        }
    }


    class myDbHelper extends SQLiteOpenHelper {
        private String database;
        public myDbHelper(Context context, String database) {
            super(context, database, null, 1);
            this.database = database;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        protected void finalize() throws Throwable {
            this.close();
            super.finalize();
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.v(TAG, "upgrade db");
        }
    }

}
