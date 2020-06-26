package com.example.waterdrinkreminder.data_collection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterdrinkreminder.R;
import com.example.waterdrinkreminder.model.User;

public class StartActivity extends AppCompatActivity {
    private  User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        user = new User();
    }

    public void onOKClick(View view) {
        Intent intent = new Intent(getApplicationContext(), SexActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
    }
}
