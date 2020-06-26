package com.example.waterdrinkreminder.activitys;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.waterdrinkreminder.R;
import com.example.waterdrinkreminder.data_base.DBHelper;
import com.example.waterdrinkreminder.data_collection.StartActivity;
import com.example.waterdrinkreminder.model.User;
import com.example.waterdrinkreminder.service.EndDayAlarmService;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.app.AlarmManager.INTERVAL_DAY;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME_VIBRATE = "vibrate";
    public static final String APP_PREFERENCES_NAME_REMIND = "remind";
    public static final String APP_PREFERENCES_NAME_SOUND = "sound";
    public static final String APP_PREFERENCES_NAME_NOTIFICATIONS = "constant_notifications";
    public static final String APP_PREFERENCES_CHECK_BOX = "check";
    public static final String APP_PREFERENCES_NAME_COUNT_REACH = "reach";
    public static final String APP_PREFERENCES_NAME_NOT_REMIND="not_remind";
    public static final String APP_PREFERENCES_NAME_DRINKING_WATER="drinking_water";
    public static final String APP_PREFERENCES_NAME_STOP = "stop";

    TextView balance_target_view;
    int volumeDrinkingWater, volumeWater;
   static public TextView nextReminderTimeView;
    int add=0;
    private User user;

    SQLiteDatabase database;
    DBHelper dBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences mSettings = getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);

        // проверяем, первый ли раз открывается программа
        boolean hasVisited = mSettings.getBoolean("hasVisited", false);

        if (!hasVisited) {
            // выводим нужную активность
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
            SharedPreferences.Editor e = mSettings.edit();
            e.putBoolean("hasVisited", true);
            e.commit();
            setStartValues();
            dBHelper = new DBHelper(this);
            database = dBHelper.getWritableDatabase();
            database.delete(dBHelper.TABLE_STATISTIC, null, null);
            Intent intent1 = new Intent(getApplicationContext(), EndDayAlarmService.class);
            //установка alarmManager на полночь, для записи в бд данных за неделю
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, 0);
            Calendar now = Calendar.getInstance();
            Calendar day = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)+1, 0, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, day.getTimeInMillis(), INTERVAL_DAY, pendingIntent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        balance_target_view=(TextView)findViewById(R.id.balance_target_view);
        nextReminderTimeView=(TextView)findViewById(R.id.next_reminder_time_view);
        volumeDrinkingWater=mSettings.getInt(APP_PREFERENCES_NAME_DRINKING_WATER, 0);

        Bundle intent = getIntent().getExtras();
        if(intent!=null) {
            add = intent.getInt("add");
            volumeDrinkingWater += add;
            SharedPreferences.Editor e=mSettings.edit();
            e.putInt(APP_PREFERENCES_NAME_DRINKING_WATER, volumeDrinkingWater);
            e.commit();
        }

        user = new User();
        LoadPreferences();

        volumeWater = user.getWaterRate();

    }
    private void setStartValues() {
        //при первом запуске или сбросе данных обнуляем данные
        SharedPreferences mSettings = getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor e=mSettings.edit();
        e.putBoolean(APP_PREFERENCES_NAME_NOTIFICATIONS,false);
        e.putBoolean(APP_PREFERENCES_NAME_SOUND, false);
        e.putBoolean(APP_PREFERENCES_NAME_VIBRATE, false);
        e.putBoolean(APP_PREFERENCES_NAME_NOT_REMIND, false);
        e.putBoolean(APP_PREFERENCES_NAME_REMIND, false);
        e.putBoolean(APP_PREFERENCES_NAME_STOP, false);
        e.putBoolean(APP_PREFERENCES_CHECK_BOX,false);
        e.putInt(APP_PREFERENCES_NAME_DRINKING_WATER, 0);
        e.putInt(APP_PREFERENCES_NAME_COUNT_REACH, 0);
        e.putString("nextRemind","");
        e.commit();

    }

    private void LoadPreferences() {
        //при открытии приложения
        SharedPreferences mSettings = getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        nextReminderTimeView=(TextView)findViewById(R.id.next_reminder_time_view);
            user.setSportHour(mSettings.getInt("sport", 0));
            user.setInterval(mSettings.getInt("interval", 10));
            user.setSex(mSettings.getString("sex", "man"));
            user.setWeight(Double.parseDouble(mSettings.getString("weight", "60")));
            user.setWaterRate(mSettings.getInt("water", 2000));

            user.setDayTime(mSettings.getString("dayTime","07:00"));
            user.setNightTime(mSettings.getString("nightTime","23:00"));

        volumeDrinkingWater=mSettings.getInt(APP_PREFERENCES_NAME_DRINKING_WATER, 0);


    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
//выбор элемента из шторки
        int id = item.getItemId();

        if (id == R.id.statistics) {
            {
                Intent intent = new Intent(getApplicationContext(), StatisticActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.reminder) {
            Intent intent = new Intent(getApplicationContext(), ReminderActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);

        } else if (id == R.id.daily_goal) {

            Intent intent = new Intent(getApplicationContext(), DailyGoalActivity.class);

            intent.putExtra("user", user);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }


    public void addWater(View view) {
       switch (view.getId())
        {
            case R.id.water200:
            {
                volumeDrinkingWater+=200;
                break;
            }
            case R.id.water300:
            {
                volumeDrinkingWater+=300;
                break;
            }
            case R.id.water400:
            {
                volumeDrinkingWater+=400;
                break;
            }
            case R.id.water500:
            {
                volumeDrinkingWater+=500;
                break;

            }

        }
        balance_target_view.setText(volumeDrinkingWater+"/"+volumeWater);
        SharedPreferences sp = getSharedPreferences(APP_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e=sp.edit();
        e.putInt(APP_PREFERENCES_NAME_DRINKING_WATER, volumeDrinkingWater);
        e.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    //если окно снова становится активным
        SharedPreferences mSettings = getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        balance_target_view.setText(volumeDrinkingWater + "/" + volumeWater);
        nextReminderTimeView.setText(mSettings.getString("nextRemind", ""));
        SharedPreferences.Editor e=mSettings.edit();
        e.putBoolean(APP_PREFERENCES_NAME_STOP, true);
        e.commit();

    }
    @Override
    protected void onStop() {
        super.onStop();
        //если окно становится неактивным
        SharedPreferences mSettings = getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor e=mSettings.edit();
        e.putBoolean(APP_PREFERENCES_NAME_STOP, false);
        e.commit();

    }
}
