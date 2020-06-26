package com.example.waterdrinkreminder.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.waterdrinkreminder.activitys.MainActivity;
import com.example.waterdrinkreminder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.content.Context.MODE_PRIVATE;


public class AlarmService extends BroadcastReceiver {



    public static final String APP_PREFERENCES_NAME_INTERVAL = "interval";
    public static final String APP_PREFERENCES_NAME_WATER_VOLUME = "water";

    // Идентификатор канала
    private static String CHANNEL_ID = "Channel";
    private boolean sound = false, vibrate = false, constant_notifications = false, not_reminder=false;
    int interval = 0, water = 0, drinkingWater = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSettings = context.getSharedPreferences(
                MainActivity.APP_PREFERENCES, MODE_PRIVATE);

            vibrate = mSettings.getBoolean(MainActivity.APP_PREFERENCES_NAME_VIBRATE, false);
            sound = mSettings.getBoolean(MainActivity.APP_PREFERENCES_NAME_SOUND, false);
            constant_notifications = mSettings.getBoolean(MainActivity.APP_PREFERENCES_NAME_NOTIFICATIONS, false);
            interval = mSettings.getInt(APP_PREFERENCES_NAME_INTERVAL, 0);
            water = mSettings.getInt(APP_PREFERENCES_NAME_WATER_VOLUME, 0);
            drinkingWater = mSettings.getInt(MainActivity.APP_PREFERENCES_NAME_DRINKING_WATER, 0);
            not_reminder = mSettings.getBoolean(MainActivity.APP_PREFERENCES_NAME_NOT_REMIND, false);
        Bundle bundle = intent.getExtras();
        String dayString = mSettings.getString("dayTime", "07:00");
        //если пользователь отметил, чтобы при достижении нормы уведомления больше не присылались
        if (not_reminder && water <= drinkingWater) {
            SharedPreferences.Editor e = mSettings.edit();
            e.putString("nextRemind", dayString);
            e.commit();
        } else {
            //устанавливаем уведомления
            Intent notificationIntent = new Intent(context, MainActivity.class);


            PendingIntent contentIntent = PendingIntent.getActivity(context,
                    1, notificationIntent,
                    0);
            PendingIntent contentIntent1 = PendingIntent.getActivity(context,
                    2, notificationIntent.putExtra("add", 200),
                    0);
            PendingIntent contentIntent2 = PendingIntent.getActivity(context,
                    3, notificationIntent.putExtra("add", 300),
                    0);
            PendingIntent contentIntent3 = PendingIntent.getActivity(context,
                    4, notificationIntent.putExtra("add", 400),
                    0);
            long[] vibration = new long[]{1000, 1000, 1000, 1000, 1000};
            Uri ringURI =
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.icon_water)
                            .setContentTitle("Время пить воду!")
                            .setContentText("")
                            .setContentIntent(contentIntent)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_water))
                            .setColor(Color.GRAY)
                            .addAction(R.drawable.icon_water, "200 мл", contentIntent1)
                            .addAction(R.drawable.icon_water, "300 мл", contentIntent2)
                            .addAction(R.drawable.icon_water, "400 мл", contentIntent3)
                            .setAutoCancel(true);
            //если отмесено, чтобы был звук
            if (sound)
                builder.setSound(ringURI);
            //если отмесено, чтобы была вибрация
            if (vibrate)
                builder.setVibrate(vibration);
            //если отмесено, чтобы были постоянные уведомления
            if (constant_notifications)
                builder.setOngoing(true);
            Notification notification = builder.build();
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);
            notificationManager.notify(0, notification);

            Calendar now = Calendar.getInstance();
            Calendar nowNext = Calendar.getInstance();
            nowNext.add(Calendar.MINUTE, interval);
            String nightString = mSettings.getString("nightTime", "23:00");

            String[] nightStr = nightString.split(":");

            Calendar night = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), Integer.parseInt(nightStr[0]), Integer.parseInt(nightStr[1]));
            SharedPreferences.Editor e = mSettings.edit();
            if (now.get(Calendar.HOUR_OF_DAY) > nowNext.get(Calendar.HOUR_OF_DAY) && Integer.parseInt(nightStr[0])< now.get(Calendar.HOUR_OF_DAY)) {
                night = new GregorianCalendar(nowNext.get(Calendar.YEAR), nowNext.get(Calendar.MONTH), nowNext.get(Calendar.DAY_OF_MONTH), Integer.parseInt(nightStr[0]), Integer.parseInt(nightStr[1]));
            }
            //если время следущего уведомления не входит в заданный промежуток времени, то время следующего уведомления будет утром
            if (now.getTimeInMillis() < night.getTimeInMillis() && nowNext.getTimeInMillis() > night.getTimeInMillis()) {
                e.putString("nextRemind", dayString);
                e.commit();
            } else {

                e.putString("nextRemind", getTime(nowNext.getTime()));
                e.commit();
            }
            //если окно MainActivity сейчас активно
            if(mSettings.getBoolean(MainActivity.APP_PREFERENCES_NAME_STOP,false))
                MainActivity.nextReminderTimeView.setText(mSettings.getString("nextRemind",""));
        }
    }

    private static String getTime(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
    }

}
