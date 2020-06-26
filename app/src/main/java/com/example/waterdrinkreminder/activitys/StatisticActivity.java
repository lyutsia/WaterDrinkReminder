package com.example.waterdrinkreminder.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.waterdrinkreminder.R;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewData;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.waterdrinkreminder.data_base.DBHelper;

public class StatisticActivity extends AppCompatActivity {

    TextView middleValue, middlePercent, textReach;
    int volume=0,  count_reach=0;
    SQLiteDatabase database;
    DBHelper dBHelper;
    private static final int N = 7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        middleValue=(TextView) findViewById(R.id.value_ml_text_view);
        middlePercent =(TextView) findViewById(R.id.percent_value_text_view);
        textReach=(TextView) findViewById(R.id.average_achievement_text_view);
        textReach.setText("среднее\nдостижение");
        dBHelper = new DBHelper(this);
        database = dBHelper.getWritableDatabase();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        SharedPreferences mSettings = getSharedPreferences(
                MainActivity.APP_PREFERENCES, MODE_PRIVATE);
        count_reach=mSettings.getInt(MainActivity.APP_PREFERENCES_NAME_COUNT_REACH, 0);

       Cursor cursor = database.query(dBHelper.TABLE_STATISTIC, null, null, null, null, null, null);

        GraphViewData[]  graph= new GraphViewData[N];

        int[] week=new int[N];
        int volumeIndex = cursor.getColumnIndex(dBHelper.FIELD_VOLUME_DRINKING_WATER);
        int dayIndex = cursor.getColumnIndex(dBHelper.FIELD_DAY);
        boolean zero=true;
        int count=1;
        if( cursor.moveToFirst())
        {
            do {
                week[cursor.getInt(dayIndex) - 1] = cursor.getInt(volumeIndex);
                volume += cursor.getInt(volumeIndex);
            } while (cursor.moveToNext());
            cursor.moveToLast();
            count=cursor.getInt(dayIndex);
        }
            for(int i=0; i<N; i++) {
                graph[i] = new GraphViewData(i+1, week[i]);
                if(week[i]!=0)
                    zero=false;
            }

        cursor.close();


       GraphViewSeries exampleSeries = new GraphViewSeries(graph);
        GraphView graphView = new BarGraphView(this,
                "Статистика за неделю");
        graphView.addSeries(exampleSeries);
        //если база данных пуста
   if(zero)
        graphView.setVerticalLabels(new String[]{"0"});
//значения по горизонтали
        graphView.setHorizontalLabels(new String[]{"Пн","Вт","Ср","Чт","Пт","Сб","Вс"});
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_grafic);
        layout.addView(graphView);
//вычисляем среднее значение и процент достижения нормы за текущие дни
        middleValue.setText(volume/count+"");
        middlePercent.setText((count_reach*100)/count+"");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
