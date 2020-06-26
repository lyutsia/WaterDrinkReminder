package com.example.waterdrinkreminder.data_collection;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterdrinkreminder.R;
import com.example.waterdrinkreminder.filters.NumberFilter;
import com.example.waterdrinkreminder.keys.IntervalKeyListener;
import com.example.waterdrinkreminder.model.User;

public class IntervalActivity extends AppCompatActivity {
    User user;
    EditText intervalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval);

        Bundle intent = getIntent().getExtras();
        user = (User) intent.getSerializable("user");

        intervalText=findViewById(R.id.interval_text_view);
        intervalText.setOnKeyListener(new IntervalKeyListener(intervalText, this, user));
        intervalText.setFilters(new InputFilter[]{new NumberFilter(3)});
        intervalText.setCursorVisible(false);


    }

    public void onOKClick(View view) {
        if (intervalText.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Введите значение",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Day_timeActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }



    }

    public void editTextClick(View view) {
        intervalText.setCursorVisible(true);
    }
}