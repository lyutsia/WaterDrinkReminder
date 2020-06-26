package com.example.waterdrinkreminder.model;

import java.io.Serializable;

public class User implements Serializable {
    private double weight;
    private int sportHour;
    private String sex;
    private String dayTime;
    private String nightTime;
    private int waterRate;
    private int interval;
    public User()
    {

    }
    public User(double weight,int sportHour,String sex, String dayTime,String nightTime, int waterRate, int interval)
    {
        this.weight=weight;
        this.sportHour=sportHour;
        this.sex=sex;
        this.dayTime=dayTime;
        this.nightTime=nightTime;
        this.waterRate=waterRate;
        this.interval=interval;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getSportHour(){return sportHour;}
    public void setSportHour(int sportHour){this.sportHour=sportHour;}

    public String getSex(){return sex;}
    public void setSex(String sex){this.sex=sex;}

    public String getDayTime(){return dayTime;}
    public void setDayTime(String dayTime){this.dayTime=dayTime;}

    public String getNightTime(){return nightTime;}
    public void setNightTime(String nightTime){this.nightTime=nightTime;}

    public int getWaterRate() {
        return waterRate;
    }
    public void setWaterRate(int waterRate) {
        this.waterRate = waterRate;
    }

    public int getInterval(){return interval;}
    public void setInterval(int interval){this.interval=interval;}
}
