package com.example.waterdrinkreminder.data_collection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterdrinkreminder.activitys.MainActivity;
import com.example.waterdrinkreminder.R;
import com.example.waterdrinkreminder.filters.TimeFilter;
import com.example.waterdrinkreminder.keys.TimeKeyListener;
import com.example.waterdrinkreminder.model.User;

public class Night_timeActivity extends AppCompatActivity {
    User user;
    EditText nightTimeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night_time);

        Bundle intent = getIntent().getExtras();
        user = (User) intent.getSerializable("user");

        nightTimeText=findViewById(R.id.night_text_view);
        nightTimeText.setFilters(new InputFilter[]{new TimeFilter(nightTimeText)});
        nightTimeText.setOnKeyListener(new TimeKeyListener(nightTimeText, this, user, false));
        nightTimeText.setCursorVisible(false);
    }

    public void onOKClick(View view) {
        if (nightTimeText.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Введите значение",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            int waterVolume;
            if (user.getSex().equals("man"))
                waterVolume =  (int)Math.round(((user.getWeight() * 0.04) + (user.getSportHour() * 0.6)) * 1000);
            else
                waterVolume = (int) Math.round(((user.getWeight() * 0.03) + (user.getSportHour() * 0.4)) * 1000);
            user.setWaterRate(waterVolume);
            SharedPreferences mSettings = getSharedPreferences(MainActivity.APP_PREFERENCES,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor e=mSettings.edit();
            e.putInt("water", waterVolume);
            e.commit();
            startActivity(intent);
        }

    }

    public void editTextClick(View view) {
        nightTimeText.setCursorVisible(true);
    }
}
