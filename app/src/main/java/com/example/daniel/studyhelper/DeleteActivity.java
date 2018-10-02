package com.example.daniel.studyhelper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Daniel on 16/01/2017.
 */

public class DeleteActivity extends BaseActivity {
    private TextView classesTxt, subjectsTxt, assessTxt;
    private ListView removeList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_activity);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        subjectsTxt = (TextView) findViewById(R.id.subjectsTxt);
        classesTxt = (TextView) findViewById(R.id.classesTxt);
        assessTxt = (TextView) findViewById(R.id.assessTxt);
        removeList = (ListView) findViewById(R.id.removeList);
        fillClass();
        subjectsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fillSubject();
            }
        });
        classesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             fillClass();
            }
        });
        assessTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillGrades();
            }
        });

    }

    public void fillClass(){
        classesTxt.setTextColor(Color.parseColor("#FF69B4"));
        subjectsTxt.setTextColor(Color.parseColor("#FFFFFF"));
        assessTxt.setTextColor(Color.parseColor("#FFFFFF"));
        ArrayList<Class> classes = databaseManager.getAllClasses();
        String[] strings = new String[classes.size()];
        for(int i = 0; i < classes.size(); i++){
            strings[i] =  databaseManager.getClassName(classes.get(i).subjectFK) +"\n" + classes.get(i).toString();
        }
        RemoveAdapter customAdapter = new RemoveAdapter(getApplicationContext(), strings, new Executable() {
            @Override
            public void run(TextView text) {
                Class temp = (Class) text.getTag();
                databaseManager.deleteClassByPrimaryKey(temp.primaryKey);
                databaseManager.deleteTodayClass(temp.startTime.getHourOfDay(), temp.startTime.getMinuteOfHour());
                fillClass();
                Toast.makeText(getApplicationContext(), "Class removed", Toast.LENGTH_SHORT).show();
            }
        }, classes);
        if(classes.size() == 0)
            Toast.makeText(getApplicationContext(), "No classes to remove", Toast.LENGTH_SHORT).show();
        removeList.setAdapter(customAdapter);
        removeList.setVisibility(View.VISIBLE);
    }

    public void fillSubject(){
        subjectsTxt.setTextColor(Color.parseColor("#FF69B4"));
        classesTxt.setTextColor(Color.parseColor("#FFFFFF"));
        assessTxt.setTextColor(Color.parseColor("#FFFFFF"));
        ArrayList<Subject> subjects = databaseManager.getTempData();
        String[] strings = new String[subjects.size()];
        for(int i = 0; i < subjects.size(); i++){
            strings[i] = subjects.get(i).toString();
        }
        RemoveAdapter customAdapter = new RemoveAdapter(getApplicationContext(), strings, new Executable() {
            @Override
            public void run(TextView text) {
                Subject subject = (Subject) text.getTag();
                databaseManager.deleteSubject(subject.primaryKey);
                databaseManager.deleteClassByForeignKey(subject.primaryKey);
                databaseManager.deleteResultByForeignKey(subject.primaryKey);
                databaseManager.deleteNoteByForeign(subject.primaryKey);
                fillSubject();
                Toast.makeText(getApplicationContext(), subject.getName() +" removed", Toast.LENGTH_SHORT).show();
            }
        }, subjects, true);
        if(subjects.size() == 0)
            Toast.makeText(getApplicationContext(), "No more subjects to remove", Toast.LENGTH_SHORT).show();
        removeList.setAdapter(customAdapter);
        removeList.setVisibility(View.VISIBLE);
    }

    public void fillGrades(){
        assessTxt.setTextColor(Color.parseColor("#FF69B4"));
        subjectsTxt.setTextColor(Color.parseColor("#FFFFFF"));
        classesTxt.setTextColor(Color.parseColor("#FFFFFF"));
        ArrayList<Assessment> results = databaseManager.getAllResults();
        String[] strings = new String[results.size()];
        for(int i = 0; i < results.size(); i++){
            strings[i] = databaseManager.getClassName(results.get(i).subjectFk) + "\n" + results.get(i).toString();
        }
        RemoveAdapter customAdapter = new RemoveAdapter(getApplicationContext(), strings, new Executable() {
            @Override
            public void run(TextView text) {
                Assessment result = (Assessment) text.getTag();
                databaseManager.deleteResultByPrimary(result.primaryKey);
                Toast.makeText(getApplicationContext(), result.getName() + " removed", Toast.LENGTH_SHORT).show();
                fillGrades();
            }
        }, results);
        if(results.size() == 0)
            Toast.makeText(getApplicationContext(), "No assessments to remove", Toast.LENGTH_SHORT).show();
        removeList.setAdapter(customAdapter);
        removeList.setVisibility(View.VISIBLE);
    }
}
