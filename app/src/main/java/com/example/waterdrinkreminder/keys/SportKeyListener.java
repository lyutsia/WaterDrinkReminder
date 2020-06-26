package com.example.waterdrinkreminder.keys;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.waterdrinkreminder.activitys.MainActivity;
import com.example.waterdrinkreminder.model.User;

import static android.view.KeyEvent.KEYCODE_ENTER;

public class SportKeyListener implements View.OnKeyListener {
    private EditText editText;
    private Context context;
    private User user;
    public SportKeyListener(EditText editText, Context context, User user) {
        this.editText = editText;
        this.context = context;
        this.user=user;
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {

        boolean consumed = false;
        if (keyCode == KEYCODE_ENTER) {
            if(!editText.getText().toString().equals("")) {
                int sportHour = Integer.parseInt(editText.getText().toString());
                if (sportHour > 23) {
                    Toast.makeText(context,
                            "Неверное значение",
                            Toast.LENGTH_SHORT).show();

                } else {
                    user.setSportHour(sportHour);
                    SharedPreferences mSettings = context.getSharedPreferences(MainActivity.APP_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor e=mSettings.edit();
                    e.putInt("sport", sportHour);
                    e.commit();
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    editText.setCursorVisible(false);
                    consumed = true;
                }

            }
            else
            {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                SharedPreferences mSettings = context.getSharedPreferences(MainActivity.APP_PREFERENCES,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor e=mSettings.edit();
                e.putInt("sport", 0);
                e.commit();
                user.setSportHour(0);
                editText.setCursorVisible(false);
                consumed = true;
            }

        }
        return consumed;
    }
}

