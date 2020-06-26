package com.example.waterdrinkreminder.service;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.example.waterdrinkreminder.activitys.MainActivity;
import com.example.waterdrinkreminder.data_base.DBHelper;


import java.util.Calendar;

public class EndDayAlarmService  extends BroadcastReceiver {
    DBHelper dBHelper;
    SQLiteDatabase database;
    public static final String APP_PREFERENCES_NAME_WATER_VOLUME = "water";
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSettings = context.getSharedPreferences(MainActivity.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        dBHelper = new DBHelper(context);
        database = dBHelper.getWritableDatabase();
        int count_reach = mSettings.getInt(MainActivity.APP_PREFERENCES_NAME_COUNT_REACH, 0);
        int drinkingWater = mSettings.getInt(MainActivity.APP_PREFERENCES_NAME_DRINKING_WATER, 0);
        //обнуляем количество выпитой воды за день
        int water = mSettings.getInt(APP_PREFERENCES_NAME_WATER_VOLUME, 0);
        SharedPreferences.Editor e = mSettings.edit();
        e.putInt(MainActivity.APP_PREFERENCES_NAME_DRINKING_WATER, 0);
        //если пользователь выполнил норму
        if (drinkingWater >= water)
            e.putInt(MainActivity.APP_PREFERENCES_NAME_COUNT_REACH, count_reach + 1);
// записываем в базу данных день недели и количество выпитой воды
        int day =Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2;
        if(day==-1)
            day=6;
        if(day==0)
            day=7;
        //если неделя прошла, то удаляем данные из бд
        if(day==1) {
            e.putInt(MainActivity.APP_PREFERENCES_NAME_COUNT_REACH, 0);
            database.delete(dBHelper.TABLE_STATISTIC, null, null);
        }
        e.commit();


        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.FIELD_VOLUME_DRINKING_WATER, drinkingWater);
        contentValues.put(DBHelper.FIELD_DAY, day);
         database.insert(dBHelper.TABLE_STATISTIC, null, contentValues);

    }
}
