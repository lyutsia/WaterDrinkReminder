package com.example.waterdrinkreminder.data_collection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waterdrinkreminder.activitys.MainActivity;
import com.example.waterdrinkreminder.R;
import com.example.waterdrinkreminder.model.User;

public class SexActivity extends AppCompatActivity {
    User user;
    ImageView manImageView;
    ImageView womanImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex);
       manImageView = findViewById(R.id.man);
        womanImageView = findViewById(R.id.woman);
        Bundle intent = getIntent().getExtras();
        user = (User) intent.getSerializable("user");
        Drawable highlight = getResources().getDrawable( R.drawable.highlight);
        manImageView.setBackground(highlight);
        user.setSex("man");
    }


    public void onOKClick(View view) {
        Intent intent = new Intent(getApplicationContext(), WeightActivity.class);
        SharedPreferences mSettings = getSharedPreferences(MainActivity.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e=mSettings.edit();
        e.putString("sex", user.getSex());
        e.commit();
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void onImageManClick(View view) {
        Drawable highlight = getResources().getDrawable( R.drawable.highlight);
        manImageView.setBackground(highlight);
        womanImageView.setBackground(null);
        user.setSex("man");
    }

    public void onImageWomanClick(View view) {
        Drawable highlight = getResources().getDrawable( R.drawable.highlight);
        womanImageView.setBackground(highlight);
        manImageView.setBackground(null);
        user.setSex("woman");
    }
}

