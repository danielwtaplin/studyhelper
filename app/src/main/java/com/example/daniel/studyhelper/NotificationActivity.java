package com.example.daniel.studyhelper;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;

public class NotificationActivity extends BaseActivity {
    private static ArrayList<Class> classesOn;
    private static String[] classString;
    private static Class[] classes;
    private TextView todayTxt;
    private String weekDay;
    private ListView todayListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        todayListView = (ListView) findViewById(R.id.todayListView);
        todayTxt = (TextView) findViewById(R.id.todayTxt);
        weekDay = new DateTime().dayOfWeek().getAsText();
        todayTxt.setText("Classes " + weekDay);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(weekDay != null && weekDay !=new DateTime().dayOfWeek().getAsText()){
            weekDay = new DateTime().dayOfWeek().getAsText();
            todayTxt.setText("Classes " + weekDay);
        }
        classesOn = databaseManager.getTodayClassList();
        if(classesOn.size() == 0){
            Toast.makeText(getApplicationContext(), "No classes today", Toast.LENGTH_SHORT).show();
            return;
        }
        removePastClasses();
        checkIfHandled();
        if(classesOn.size() == 0){
            Toast.makeText(getApplicationContext(), "No more upcoming classes", Toast.LENGTH_SHORT).show();
        }
        if(!databaseManager.checkTodayClassRemaining())
            databaseManager.clearTodayClassTable();
        classString = new String[ classesOn.size()];
        classes = new Class[ classesOn.size()];
        for(int i = 0; i < classesOn.size(); i ++){
            classString[i] = classesOn.get(i).toString();
            classes[i] = classesOn.get(i);
        }
        NotifyAdapter customAdapter = new NotifyAdapter(getApplicationContext(), classString, classes);
        todayListView.setAdapter(customAdapter);
    }

    public static void removeClass(Class classToRemove){
        classesOn.remove(classToRemove);
    }

    public void checkIfHandled(){
        if(databaseManager.checkTodayEmpty()) {
            return;
        }
        ArrayList<Class> temp = new ArrayList<>();
        for(Class c: classesOn){
            if(!databaseManager.checkTodayForeignKey(c.startTime)){
                temp.add(c);
            }
        }
        classesOn = temp;
    }

    public void removePastClasses(){
        ArrayList<Class> newClassList = new ArrayList<>();
        for(Class c: classesOn){
            if(c.startTime.isAfter(new LocalTime()))
                newClassList.add(c);
        }
        classesOn = newClassList;
    }


}
