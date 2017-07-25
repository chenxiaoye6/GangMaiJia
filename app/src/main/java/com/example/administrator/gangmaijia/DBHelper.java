package com.example.administrator.gangmaijia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.administrator.gangmaijia.Util.CitySqliteDatabase;

import java.io.IOException;

/**
 * Created by Administrator on 2016/10/12.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper mInstance = null;

    private String CREATE_TABLE_USER = "create table User(UserId varchar(255)," +
            "UserType varchar(1),UserPhone varchar(11),isLogin varchar(1),UserBell varchar(255));";
    //
    private String CREATE_TABLE_NEWS = "create table IF NOT EXISTS News_table(ms int,time varchar(255),news varchar(255))";
    //

    public DBHelper(Context context) {
        this(context, "GangMaiJia.db", null, 1);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public synchronized static DBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHelper(context);
        }
        return mInstance;
    }

    ;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_NEWS);
        Log.e("789", "creat table");
        try {
            String mzk_db = new CitySqliteDatabase().CopySqliteFileFromRawToDatabases("mzk_db");
            System.out.println("--------mzk_db--------" + mzk_db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
