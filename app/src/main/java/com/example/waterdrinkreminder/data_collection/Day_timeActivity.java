package com.example.waterdrinkreminder.data_collection;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterdrinkreminder.R;
import com.example.waterdrinkreminder.filters.TimeFilter;
import com.example.waterdrinkreminder.keys.TimeKeyListener;
import com.example.waterdrinkreminder.model.User;

import java.text.ParseException;

public class Day_timeActivity extends AppCompatActivity {
    User user;
    EditText dayTimeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_time);
        dayTimeText=findViewById(R.id.day_text_view);
        dayTimeText.setFilters(new InputFilter[]{new TimeFilter(dayTimeText)});
        Bundle intent = getIntent().getExtras();
        user = (User) intent.getSerializable("user");

        dayTimeText.setOnKeyListener(new TimeKeyListener(dayTimeText, this, user, true));

        dayTimeText.setCursorVisible(false);
    }

    public void onOKClick(View view) throws ParseException {
        if (dayTimeText.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Введите значение",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Night_timeActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

    }

    public void editTextClick(View view) {
        dayTimeText.setCursorVisible(true);
    }
}
