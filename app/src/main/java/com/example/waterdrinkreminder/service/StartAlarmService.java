package com.example.waterdrinkreminder.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.waterdrinkreminder.activitys.MainActivity;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class StartAlarmService extends BroadcastReceiver {

    public static final String APP_PREFERENCES_NAME_INTERVAL= "interval";

    int interval=0;
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSettings = context.getSharedPreferences(
                MainActivity.APP_PREFERENCES, MODE_PRIVATE);

        interval=mSettings.getInt(APP_PREFERENCES_NAME_INTERVAL, 0);
        Bundle bundle= intent.getExtras();
        boolean start=bundle.getBoolean("start");

        intent = new Intent(context, AlarmService.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar now = Calendar.getInstance();
//устанавливаем уведомления с заданным интервалом, если start=true
        //отключаем уведомления, если start=false
        if(start)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(),  interval*60 * 1000, pendingIntent);
        else
            alarmManager.cancel(pendingIntent);
    }
}
