package com.example.waterdrinkreminder.data_collection;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterdrinkreminder.R;
import com.example.waterdrinkreminder.filters.NumberFilter;
import com.example.waterdrinkreminder.keys.SportKeyListener;
import com.example.waterdrinkreminder.model.User;

public class SportActivity extends AppCompatActivity {
    User user;
    EditText sportHourText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        Bundle intent = getIntent().getExtras();
        user = (User) intent.getSerializable("user");

        sportHourText=findViewById(R.id.sport_text_view);
        sportHourText.setOnKeyListener(new SportKeyListener(sportHourText, this, user));
        sportHourText.setFilters(new InputFilter[]{new NumberFilter(2)});
        sportHourText.setCursorVisible(false);


    }

    public void onOKClick(View view) {

            Intent intent = new Intent(getApplicationContext(), IntervalActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);


    }

    public void editTextClick(View view) {
        sportHourText.setCursorVisible(true);
    }
}
