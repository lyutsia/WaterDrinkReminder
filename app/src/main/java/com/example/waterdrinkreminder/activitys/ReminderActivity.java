package com.example.waterdrinkreminder.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.waterdrinkreminder.R;
import com.example.waterdrinkreminder.filters.NumberFilter;
import com.example.waterdrinkreminder.filters.TimeFilter;
import com.example.waterdrinkreminder.keys.IntervalKeyListener;
import com.example.waterdrinkreminder.keys.TimeKeyListener;
import com.example.waterdrinkreminder.model.User;
import com.example.waterdrinkreminder.service.StartAlarmService;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.view.KeyEvent.KEYCODE_ENTER;


public class ReminderActivity extends AppCompatActivity {
    EditText intervalTime;
    EditText dayTimeText, nightTimeText;
    TextView notRemindText;

    int interval = 0;
    String dayTime, nightTime;
    SwitchCompat remindSwitch, soundSwitch, vibrateSwitch, constantRemindSwitch, notReminderSwitch;
    boolean vibrate=false, remind=false, sound=false, constant_notifications=false, not_reminder=false;
static User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        LoadPreferences();
        Bundle intent = getIntent().getExtras();
        notRemindText=(TextView) findViewById(R.id.not_reminder_text_view);
        notRemindText.setText("отключить уведомления\nпосле достижения цели");
        intervalTime = (EditText) findViewById(R.id.time_text_view);
        dayTimeText=(EditText) findViewById(R.id.day_text_view);
        nightTimeText=(EditText) findViewById(R.id.night_text_view);

        user = (User) intent.getSerializable("user");
                interval = user.getInterval();
                dayTime = user.getDayTime();
                nightTime = user.getNightTime();


        intervalTime.setText(interval+"");
        intervalTime.setCursorVisible(false);
        intervalTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    View.OnKeyListener kl = new IntervalKeyListener(intervalTime, v.getContext(), user);
                    KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KEYCODE_ENTER);
                    if (!kl.onKey(intervalTime, KEYCODE_ENTER, event))
                        intervalTime.setText(user.getInterval() + "");
                }
            }
        });
        dayTimeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    View.OnKeyListener kl = new TimeKeyListener(dayTimeText, v.getContext(), user, true);
                    KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KEYCODE_ENTER);
                    if (!kl.onKey(dayTimeText, KEYCODE_ENTER, event))
                        dayTimeText.setText(user.getDayTime());
                }
            }
        });
        nightTimeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    View.OnKeyListener kl = new TimeKeyListener(nightTimeText, v.getContext(), user, false);
                    KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KEYCODE_ENTER);
                    if (!kl.onKey(nightTimeText, KEYCODE_ENTER, event))
                        nightTimeText.setText(user.getNightTime());
                }
            }
        });
        dayTimeText.setText(dayTime);
        nightTimeText.setText(nightTime);
        nightTimeText.setCursorVisible(false);
        dayTimeText.setCursorVisible(false);

        remindSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences mSettings = getSharedPreferences(MainActivity.APP_PREFERENCES,
                    Context.MODE_PRIVATE);
                SharedPreferences.Editor e=mSettings.edit();
                Intent intent = new Intent(getApplicationContext(), StartAlarmService.class);
                intent.putExtra("start", true);
                PendingIntent pendingIntent1=PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
               PendingIntent pendingIntent2= PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
                intent.putExtra("start", false);
                PendingIntent pendingIntent3= PendingIntent.getBroadcast(getApplicationContext(), 2, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                if (isChecked) {
                    //устанавливаем уведомления
                    setAlarmManager(getApplicationContext(),pendingIntent1,pendingIntent2,pendingIntent3, alarmManager);
                } else {
                    //отключаем уведомления
                    e.remove("nextRemind");
                    e.commit();
                    alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), pendingIntent3);
                    alarmManager.cancel(pendingIntent2);
                    alarmManager.cancel(pendingIntent3);

                }
                remind=isChecked;
                e.putBoolean(MainActivity.APP_PREFERENCES_NAME_REMIND,remind);
                e.commit();
            }
        });

        vibrateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vibrate=isChecked;
                SharedPreferences mSettings = getSharedPreferences(MainActivity.APP_PREFERENCES,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor e=mSettings.edit();
                e.putBoolean(MainActivity.APP_PREFERENCES_NAME_VIBRATE, vibrate);
                e.commit();
            }
        });


        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 sound=isChecked;
                SharedPreferences mSettings = getSharedPreferences(MainActivity.APP_PREFERENCES,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor e=mSettings.edit();
                e.putBoolean(MainActivity.APP_PREFERENCES_NAME_SOUND, sound);
                e.commit();
            }
        });


       constantRemindSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
       {

           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               constant_notifications=isChecked;
               SharedPreferences mSettings = getSharedPreferences(MainActivity.APP_PREFERENCES,
                       Context.MODE_PRIVATE);
               SharedPreferences.Editor e=mSettings.edit();
               e.putBoolean(MainActivity.APP_PREFERENCES_NAME_NOTIFICATIONS, constant_notifications);
               e.commit();
           }
       });
        notReminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                not_reminder=isChecked;
                SharedPreferences mSettings = getSharedPreferences(MainActivity.APP_PREFERENCES,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor e=mSettings.edit();
                e.putBoolean(MainActivity.APP_PREFERENCES_NAME_NOT_REMIND, not_reminder);
                e.commit();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        intervalTime.setFilters(new InputFilter[]{new NumberFilter(3)});
        intervalTime.setOnKeyListener(new IntervalKeyListener(intervalTime,this, user));

        dayTimeText.setOnKeyListener(new TimeKeyListener(dayTimeText, this, user, true));
        nightTimeText.setOnKeyListener(new TimeKeyListener(nightTimeText, this, user, false));

        dayTimeText.setFilters(new InputFilter[]{new TimeFilter(dayTimeText)});
        nightTimeText.setFilters(new InputFilter[]{new TimeFilter(nightTimeText)});


    }
    private void LoadPreferences() {
        //установка значений switchCompat
        SharedPreferences mSettings = getSharedPreferences(MainActivity.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e=mSettings.edit();
        remindSwitch=(SwitchCompat)findViewById(R.id.switch_compat_remind);
        vibrateSwitch=(SwitchCompat)findViewById(R.id.switch_compat_vibration);
        soundSwitch=(SwitchCompat)findViewById(R.id.switch_compat_melody);
        constantRemindSwitch=(SwitchCompat)findViewById(R.id.switch_compat_constant_remind);
        notReminderSwitch=(SwitchCompat)findViewById(R.id.switch_compat_not_reminder);

            vibrate=mSettings.getBoolean(MainActivity.APP_PREFERENCES_NAME_VIBRATE, false);
            vibrateSwitch.setChecked(vibrate);

            sound=mSettings.getBoolean(MainActivity.APP_PREFERENCES_NAME_SOUND, false);
            soundSwitch.setChecked(sound);

            constant_notifications=mSettings.getBoolean(MainActivity.APP_PREFERENCES_NAME_NOTIFICATIONS, false);
            constantRemindSwitch.setChecked(constant_notifications);

            remind=mSettings.getBoolean(MainActivity.APP_PREFERENCES_NAME_REMIND, false);
            remindSwitch.setChecked(remind);

            not_reminder=mSettings.getBoolean(MainActivity.APP_PREFERENCES_NAME_NOT_REMIND, false);
            notReminderSwitch.setChecked(not_reminder);
    }

static  public void setAlarmManager(Context context, PendingIntent pendingIntent1,PendingIntent pendingIntent2, PendingIntent pendingIntent3, AlarmManager alarmManager)
{
    Calendar day, night;
    Calendar now= Calendar.getInstance();
    String[] dayStr=user.getDayTime().split(":");
    String[] nightStr=user.getNightTime().split(":");
    int hourD= Integer.parseInt(dayStr[0]);
    int minutD= Integer.parseInt(dayStr[1]);
    int hourN= Integer.parseInt(nightStr[0]);
    int minutN= Integer.parseInt(nightStr[1]);
    day=new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), Integer.parseInt(dayStr[0]), Integer.parseInt(dayStr[1]));
    night=new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), Integer.parseInt(nightStr[0]), Integer.parseInt(nightStr[1]));

//если уведомление включено, то первое уведопление будет сейчас
    if((now.getTimeInMillis()>day.getTimeInMillis()&&now.getTimeInMillis()<night.getTimeInMillis()) ||
            (day.getTimeInMillis()>night.getTimeInMillis()&&now.getTimeInMillis()<night.getTimeInMillis()&& now.getTimeInMillis()<day.getTimeInMillis())
                    ||(day.getTimeInMillis()>night.getTimeInMillis()&&now.getTimeInMillis()>night.getTimeInMillis()&&now.getTimeInMillis()>day.getTimeInMillis())){
        alarmManager.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(),  pendingIntent1);
    }
    else
    {
        SharedPreferences mSettings = context.getSharedPreferences(MainActivity.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e=mSettings.edit();
        e.putString("nextRemind",getTime(day.getTime()));
        e.commit();
        alarmManager.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), pendingIntent3);
    }
    //если этим днем заданное время уже прошло, то устанавливаем на следующий
    if(now.get(Calendar.HOUR_OF_DAY)>hourD||(now.get(Calendar.HOUR_OF_DAY)==hourD&&now.get(Calendar.MINUTE)>minutD))
        day.add(Calendar.DAY_OF_MONTH,1);
    if(now.get(Calendar.HOUR_OF_DAY)>hourN||(now.get(Calendar.HOUR_OF_DAY)==hourN&&now.get(Calendar.MINUTE)>minutN))
        night.add(Calendar.DAY_OF_MONTH,1);

    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, day.getTimeInMillis(), INTERVAL_DAY, pendingIntent2);

    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, night.getTimeInMillis(), INTERVAL_DAY, pendingIntent3);
}

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void editTextClick(View view) {
        intervalTime.setCursorVisible(true);
        intervalTime.setText("");
    }

    public void dayTimeEditTextClick(View view) {
        dayTimeText.setCursorVisible(true);
        dayTimeText.setText("");
    }

    public void nightTimeEditTextClick(View view) {
        nightTimeText.setCursorVisible(true);
        nightTimeText.setText("");
    }

    private static String getTime(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
    }

}


