package com.example.waterdrinkreminder.data_base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="statisticDataBase";
    public static final String TABLE_STATISTIC="statistics";

    public static final String FIELD_ID = "id";
    public static final String FIELD_VOLUME_DRINKING_WATER = "drinking_water";
    public static final String FIELD_DAY = "day";
    //баа данных состоит из из трех полей: id, количество выпитой воды, номер дня недели
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_STATISTIC + "(" +
                FIELD_ID + " integer primary key," +
                FIELD_VOLUME_DRINKING_WATER+ " integer," +

                FIELD_DAY + " integer" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_STATISTIC);
        onCreate(db);

    }
}
