package com.example.waterdrinkreminder.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import com.example.waterdrinkreminder.R;
import com.example.waterdrinkreminder.filters.NumberDecimalFilter;
import com.example.waterdrinkreminder.filters.NumberFilter;
import com.example.waterdrinkreminder.keys.SportKeyListener;
import com.example.waterdrinkreminder.keys.WaterVolumeKeyListener;
import com.example.waterdrinkreminder.keys.WeightKeyListener;
import com.example.waterdrinkreminder.model.User;

import static android.view.KeyEvent.KEYCODE_ENTER;

public class DailyGoalActivity extends AppCompatActivity {
    private User user;
    CheckBox manual_checkBox;

    CheckBox not_manual_checkBox;
    EditText volume1_text_view, sportHourView, weightView;
    TextView volume2_text_view;
    Button OkButton;
    int waterVolume=0, sportHour=0;
    double weight=0;
    boolean check=false;
    Drawable highlight;
    TextView manView, womanView, sportView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_goal);

        volume2_text_view=(TextView)findViewById(R.id.volume2_text_view);
        OkButton=(Button) findViewById(R.id.OkButton);
        manView=(TextView) findViewById(R.id.M_text_view);
        womanView=(TextView) findViewById(R.id.W_text_view);
        sportView=(TextView) findViewById(R.id.sport_text);
        sportView.setText("Физическая\nактивность");
        weightView=(EditText) findViewById(R.id.weight_text_view);

        weightView.setFilters(new InputFilter[]{new NumberDecimalFilter()});
        weightView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    View.OnKeyListener kl = new WeightKeyListener(weightView, v.getContext(), user);
                    KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KEYCODE_ENTER);
                    if (!kl.onKey(weightView, KEYCODE_ENTER, event))
                        weightView.setText(user.getWeight() + "");
                }


            }
        });
        sportHourView=(EditText) findViewById(R.id.sport_text_view);
        sportHourView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    View.OnKeyListener kl = new SportKeyListener(sportHourView, v.getContext(), user);
                    KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KEYCODE_ENTER);
                    if (!kl.onKey(sportHourView, KEYCODE_ENTER, event))
                        sportHourView.setText(user.getSportHour() + "");
                }


            }
        });
        sportHourView.setFilters(new InputFilter[]{new NumberFilter(2)});

        highlight = getResources().getDrawable( R.drawable.highlight1);

        Bundle intent = getIntent().getExtras();
        user=(User) intent.getSerializable("user");


            waterVolume = user.getWaterRate();
            weight=user.getWeight();
            sportHour=user.getSportHour();
            if(user.getSex().equals("man"))
                manView.setBackground(highlight);
            else
                womanView.setBackground(highlight);



        sportHourView.setCursorVisible(false);
        weightView.setCursorVisible(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        weightView.setOnKeyListener(new WeightKeyListener(weightView,this, user));


        manual_checkBox=(CheckBox)findViewById(R.id.manual_checkBox);
        not_manual_checkBox=(CheckBox)findViewById(R.id.not_manual_checkBox);
        volume1_text_view=findViewById(R.id.volume1_text_view);
        volume1_text_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    View.OnKeyListener kl = new WaterVolumeKeyListener(volume1_text_view, v.getContext(), user);
                    KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KEYCODE_ENTER);
                    if (!kl.onKey(volume1_text_view, KEYCODE_ENTER, event))
                        volume1_text_view.setText(user.getWaterRate() + "");
                }

            }
        });

        volume1_text_view.setOnKeyListener(new WaterVolumeKeyListener(volume1_text_view,this, user));
        volume1_text_view.setFilters(new InputFilter[]{new NumberFilter(4)});

        LoadPreferences();

        sportHourView.setOnKeyListener(new SportKeyListener(sportHourView,this, user));
        weightView.setText(weight+"");
        sportHourView.setText(sportHour+"");
    }


    public void onOKClick(View view) {
        //рассчитваем значение объема воды
        if (user.getSex().equals("man"))
            waterVolume =  (int)Math.round(((user.getWeight() * 0.04) + (user.getSportHour() * 0.6)) * 1000);
        else
            waterVolume = (int) Math.round(((user.getWeight() * 0.03) + (user.getSportHour() * 0.4)) * 1000);
        volume2_text_view.setText(waterVolume+"");
        user.setWaterRate(waterVolume);
        SharedPreferences mSettings = getSharedPreferences(MainActivity.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e=mSettings.edit();
        e.putInt("water", waterVolume);
        e.commit();
    }

    public void ManualCheckBoxChecked(View view) {
        not_manual_checkBox.setChecked(false);
        manual_checkBox.setChecked(true);
        volume1_text_view.setEnabled(true);
        volume2_text_view.setEnabled(false);
        volume1_text_view.setCursorVisible(false);
        volume2_text_view.setTextColor(Color.GRAY);
        volume1_text_view.setTextColor(Color.BLACK);
        SharedPreferences mSettings = getSharedPreferences(
                MainActivity.APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(MainActivity.APP_PREFERENCES_CHECK_BOX, manual_checkBox.isChecked());
        editor.commit();
        OkButton.setEnabled(false);

    }

    public void NotManualCheckBoxChecked(View view) {
        manual_checkBox.setChecked(false);
        not_manual_checkBox.setChecked(true);
        volume1_text_view.setTextColor(Color.GRAY);
        volume2_text_view.setTextColor(Color.BLACK);
        volume1_text_view.setEnabled(false);
        volume2_text_view.setEnabled(true);
        SharedPreferences mSettings = getSharedPreferences(
                MainActivity.APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(MainActivity.APP_PREFERENCES_CHECK_BOX, manual_checkBox.isChecked());
        editor.commit();
        OkButton.setEnabled(true);
    }
    private void LoadPreferences() {
        //устанавливаем значение выбранного checkBox
        SharedPreferences mSettings = getSharedPreferences(
                MainActivity.APP_PREFERENCES, MODE_PRIVATE);
            check=mSettings.getBoolean(MainActivity.APP_PREFERENCES_CHECK_BOX, false);
        if(check)
        {
            ManualCheckBoxChecked(volume1_text_view);
            volume1_text_view.setText(user.getWaterRate()+"");
        }
        else
        {
            NotManualCheckBoxChecked(volume2_text_view);
            volume2_text_view.setText(user.getWaterRate()+"");
        }
    }

    @Override
    public void onBackPressed() {

        backPressed();

    }

    @Override
    public boolean onSupportNavigateUp() {
        return backPressed();
    }
    public boolean backPressed()
    {

        boolean back=true;

       if(manual_checkBox.isChecked())
        {

            if(volume1_text_view.getText().toString().equals("") ) {
                Toast.makeText(getApplicationContext(),
                        "Введите значение",
                        Toast.LENGTH_SHORT).show();
                volume1_text_view.setCursorVisible(true);
                back=false;
            }
            else
            {
                user.setWaterRate(Integer.parseInt(volume1_text_view.getText().toString()));
            }

        }

        if(back) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }
        else
            return false;
    }

    public void weightEditTextClick(View view) {
        weightView.setCursorVisible(true);
        weightView.setText("");
    }

    public void sportEditTextClick(View view) {
        sportHourView.setCursorVisible(true);
        sportHourView.setText("");

    }

    public void onWomanClick(View view) {

        womanView.setBackground(highlight);
        manView.setBackground(null);
        SharedPreferences mSettings = getSharedPreferences(
                MainActivity.APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("sex", "woman");
        editor.commit();
        user.setSex("woman");
    }

    public void onManClick(View view)
    {
        manView.setBackground(highlight);
        womanView.setBackground(null);
        SharedPreferences mSettings = getSharedPreferences(
                MainActivity.APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("sex", "man");
        editor.commit();
        user.setSex("man");
    }

    public void onClearClick(View view) {
        //сброс данных
        SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e=sp.edit();
        e.putBoolean("hasVisited", false);
        e.commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    public void editTextClick(View view) {
        volume1_text_view.setCursorVisible(true);
    }
}
