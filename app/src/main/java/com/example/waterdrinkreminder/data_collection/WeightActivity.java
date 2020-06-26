package com.example.waterdrinkreminder.data_collection;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterdrinkreminder.R;
import com.example.waterdrinkreminder.filters.NumberDecimalFilter;
import com.example.waterdrinkreminder.keys.WeightKeyListener;
import com.example.waterdrinkreminder.model.User;

public class WeightActivity extends AppCompatActivity {
    User user;
    EditText weightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        Bundle intent = getIntent().getExtras();
        user = (User) intent.getSerializable("user");

        weightText=findViewById(R.id.weight_text_view);
        weightText.setOnKeyListener(new WeightKeyListener(weightText,this,user));
        weightText.setCursorVisible(false);
        weightText.setFilters(new InputFilter[]{new NumberDecimalFilter()});

    }

    public void onOKClick(View view) {
        if (weightText.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Введите значение",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), SportActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

    }

    public void editTextClick(View view) {
        weightText.setCursorVisible(true);
    }
}
