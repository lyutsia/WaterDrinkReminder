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

import com.example.waterdrinkreminder.activitys.ReminderActivity;
import com.example.waterdrinkreminder.model.User;
import com.example.waterdrinkreminder.service.StartAlarmService;

import static android.view.KeyEvent.KEYCODE_ENTER;

public class IntervalKeyListener implements View.OnKeyListener {
    private EditText editText;
    private Context context;
    private User user;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME_REMIND = "remind";
    public IntervalKeyListener(EditText editText, Context context, User user) {
        this.editText = editText;
        this.context = context;
        this.user = user;
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {

        boolean consumed = false;
        if (keyCode == KEYCODE_ENTER) {


            if (editText.getText().toString().equals("")) {
                Toast.makeText(context,
                        "Введите значение",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                int interval = Integer.parseInt(editText.getText().toString());
                if(interval<10)
                {   Toast.makeText(context,
                        "Минимальный интервал 10 мин",
                        Toast.LENGTH_SHORT).show();

                }
                else
                {
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    editText.setCursorVisible(false);
                    user.setInterval(interval);
                    SharedPreferences mSettings = context.getSharedPreferences(APP_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor e=mSettings.edit();
                    e.putInt("interval", interval);
                    e.commit();

                    consumed = true;
                    //если меняем интервал и уведомления включены, то устанавливаем уведомления заново
                    if(mSettings.getBoolean(APP_PREFERENCES_NAME_REMIND,false)) {
                        Intent intent = new Intent(context.getApplicationContext(), StartAlarmService.class);
                        intent.putExtra("start", true);
                        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, 0);
                        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context.getApplicationContext(), 1, intent, 0);
                        intent.putExtra("start", false);
                        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context.getApplicationContext(), 2, intent, 0);
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        ReminderActivity.setAlarmManager(context, pendingIntent1, pendingIntent2, pendingIntent3, alarmManager);
                    }

                }
            }


        }
        return consumed;
    }
}
