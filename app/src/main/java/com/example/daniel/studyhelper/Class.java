package com.example.daniel.studyhelper;

import android.content.Context;

import org.joda.time.LocalTime;


/**
 * Created by Daniel on 19/12/2016.
 */

public class Class {
    public String room;
    public LocalTime startTime;
    public LocalTime finishTime;
    public WeekDay day;
    public int primaryKey;
    private DatabaseManager dbHelper;
    public enum Type{
        Tutorial, Lab, Lecture
    }
    public Type type;
    public final int subjectFK;

    public Class(LocalTime startTime, LocalTime finishTime, int fK, WeekDay day){
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.subjectFK = fK;
        this.day = day;
    }

    public Class(LocalTime startTime, LocalTime finishTime, int fK, WeekDay day, int pk){
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.subjectFK = fK;
        this.day = day;
        this.primaryKey = pk;
    }

    public Class(String room,LocalTime startTime, LocalTime finishTime, int fK, WeekDay day){
        this.room = room;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.subjectFK = fK;
        this.day = day;
    }

    public Class(String room, LocalTime startTime, LocalTime finishTime, Type type, int fK, WeekDay day){
        this.room = room;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.type = type;
        this.subjectFK = fK;
        this.day = day;
    }


    public boolean compareClass(Class other){
        if(this.startTime.isBefore(other.startTime))
            return true;
        else return false;
    }

    public void fillClassTable(Context context){
        dbHelper = new DatabaseManager(context);
        dbHelper.insertIntoClass(this.subjectFK, this.room, this.startTime, this.finishTime, this.day);
    }

    public String localTimeToString(LocalTime time){
        String timeString;
        String temp;
        if(time.getMinuteOfHour() < 10)
            temp = ":0";
        else
            temp = ":";
        timeString = String.valueOf(time.getHourOfDay()) + temp + String.valueOf(time.getMinuteOfHour());
        if(time.getHourOfDay() >= 12) {
            if(time.getHourOfDay() > 12)
                timeString = String.valueOf(time.getHourOfDay() - 12) + temp + String.valueOf(time.getMinuteOfHour());
            timeString += "pm";
        }
        else{
            if(time.getHourOfDay() == 0)
                timeString = String.valueOf(12) + temp + String.valueOf(time.getMinuteOfHour());
            timeString += "am";
        }
        return timeString;

    }

    public String toString(){
        return "Day: " + this.day.toString() + "\nStart time: " + localTimeToString(this.startTime) + "\nFinish time: " + localTimeToString(this.finishTime);
    }

}
