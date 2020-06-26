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

public class WeightKeyListener implements View.OnKeyListener {
    private EditText editText;
    private Context context;
    private User user;
    public WeightKeyListener(EditText editText, Context context, User user) {
        this.editText = editText;
        this.context = context;
        this.user=user;
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {

        boolean consumed = false;
        if (keyCode == KEYCODE_ENTER) {


            if (editText.getText().toString().equals("")) {
                Toast.makeText(context,
                        "Введите значение",
                        Toast.LENGTH_SHORT).show();
            } else {
                double weight = Double.parseDouble(editText.getText().toString());
                String weightString = editText.getText().toString();
                if (weight < 10 || weight > 300 || weightString.charAt(weightString.length() - 1) == '.') {
                    Toast.makeText(context,
                            "Неверное значение",
                            Toast.LENGTH_SHORT).show();

                } else {

                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    editText.setCursorVisible(false);
                    user.setWeight(weight);
                    SharedPreferences mSettings = context.getSharedPreferences(MainActivity.APP_PREFERENCES,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor e=mSettings.edit();
                    e.putString("weight", String.valueOf(weight));
                    e.commit();
                    consumed = true;
                }
            }


        }
        return consumed;
    }
}