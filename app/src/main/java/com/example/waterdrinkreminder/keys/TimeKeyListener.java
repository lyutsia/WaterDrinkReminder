package com.example.waterdrinkreminder.keys;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.waterdrinkreminder.activitys.MainActivity;
import com.example.waterdrinkreminder.activitys.ReminderActivity;
import com.example.waterdrinkreminder.model.User;
import com.example.waterdrinkreminder.service.StartAlarmService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.KeyEvent.KEYCODE_ENTER;

public class TimeKeyListener implements View.OnKeyListener {
    private EditText editText;
    private Context context;
    private User user;
    private Boolean day;

    public TimeKeyListener(EditText editText, Context context, User user, Boolean day) {
        this.editText =editText;
        this.context=context;
        this.user=user;
        this.day=day;
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {

        boolean consumed = false;
        if (keyCode == KEYCODE_ENTER) {


            if (editText.getText().toString().equals("")) {
                Toast.makeText(context,
                        "Введите значение",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                if (editText.getText().toString().length() != 5) {
                    Toast.makeText(context,
                            "Неверное значение",
                            Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        Date date = new SimpleDateFormat("HH:mm").parse(editText.getText().toString());
                        SharedPreferences mSettings = context.getSharedPreferences(MainActivity.APP_PREFERENCES,
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor e = mSettings.edit();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        editText.setCursorVisible(false);
                        if (day) {
                            user.setDayTime(editText.getText().toString());
                            e.putString("dayTime", editText.getText().toString());
                            e.commit();
                        } else {

                            e.putString("nightTime", editText.getText().toString());
                            e.commit();
                            user.setNightTime(editText.getText().toString());
                        }
                        //если уведомления включены и меняем промежуток времени, то устанавливаем уведомления заново
                        if (mSettings.getBoolean(MainActivity.APP_PREFERENCES_NAME_REMIND, false)) {
                            Intent intent = new Intent(context.getApplicationContext(), StartAlarmService.class);
                            intent.putExtra("start", true);
                            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, 0);
                            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context.getApplicationContext(), 1, intent, 0);
                            intent.putExtra("start", false);
                            PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context.getApplicationContext(), 2, intent, 0);
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            ReminderActivity.setAlarmManager(context, pendingIntent1, pendingIntent2, pendingIntent3, alarmManager);
                        }
                        consumed = true;

                    } catch (ParseException e) {

                        Toast.makeText(context,
                                "Неверное значение",
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }

        }
        return consumed;
    }
}
