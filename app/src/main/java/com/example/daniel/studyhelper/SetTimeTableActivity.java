package com.example.daniel.studyhelper;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.LocalTime;

import java.util.ArrayList;

public class SetTimeTableActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private ArrayAdapter adapter;
    private WeekDay dayOfTheWeek;
    private Switch switchTime;
    private TextView txtStartTime, txtFinTime, txtClass;
    private TimePicker timePicker, timePickerFin;
    private Button addBtn, nextBtn;
    private static int subjectIndex = 0;
    ArrayList<Subject> subjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time_table);
        switchTime = (Switch) findViewById(R.id.switchTime);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePickerFin = (TimePicker) findViewById(R.id.timePickerFin);
        txtStartTime = (TextView) findViewById(R.id.txtStartTime);
        txtFinTime = (TextView) findViewById(R.id.txtFinTime);
        txtStartTime.setTextColor(Color.parseColor("#FF69B4"));
        addBtn = (Button) findViewById(R.id.addBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.days_of_the_week, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        switchTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(timePicker.getVisibility() == View.VISIBLE){
                    timePicker.setVisibility(View.INVISIBLE);
                    timePickerFin.setVisibility(View.VISIBLE);
                    txtStartTime.setTextColor(Color.parseColor("#FFFFFF"));
                    txtFinTime.setTextColor(Color.parseColor("#FF69B4"));
                }
                else{
                    timePickerFin.setVisibility(View.INVISIBLE);
                    timePicker.setVisibility(View.VISIBLE);
                    txtStartTime.setTextColor(Color.parseColor("#FF69B4"));
                    txtFinTime.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subjectIndex < subjects.size() - 1){
                    subjectIndex++;
                    setClassName();
                }
                else{
                    subjectIndex = 0;
                   setClassName();
                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timePicker.getHour() == timePickerFin.getHour() && timePickerFin.getMinute() == timePicker.getMinute()){
                    Toast.makeText(SetTimeTableActivity.this, "Class start and finish times cannot be the same", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(timePicker.getHour() > 12 && timePickerFin.getHour() <= 12){
                    Toast.makeText(SetTimeTableActivity.this, "Class must start and finish on the same day", Toast.LENGTH_SHORT).show();
                    return;
                }
                LocalTime start = new LocalTime(timePicker.getHour(),timePicker.getMinute());
                LocalTime finish = new LocalTime(timePickerFin.getHour(), timePickerFin.getMinute());
                if(finish.isBefore(start)){
                    if(start.getHourOfDay() < 12 && finish.getHourOfDay() == 0)
                        Toast.makeText(SetTimeTableActivity.this, "Something doesn't look right, try adjusting the am/pm", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(SetTimeTableActivity.this, "Class finish time cannot be before start time", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(databaseManager.checkClassConflict(start, finish, dayOfTheWeek, subjects.get(subjectIndex).primaryKey)){
                    Toast.makeText(SetTimeTableActivity.this, "You have a class conflict with " + DatabaseManager.errorMsg, Toast.LENGTH_SHORT).show();
                    return;
                }
                new Class(start, finish, subjects.get(subjectIndex).primaryKey,dayOfTheWeek).fillClassTable(SetTimeTableActivity.this);
                Toast.makeText(SetTimeTableActivity.this, "Class time added", Toast.LENGTH_SHORT).show();
            }
        });
        txtClass = (TextView) findViewById(R.id.txtClass);
        setClassName();


    }

    @Override
    protected void onResume() {
        super.onResume();
        subjects = databaseManager.getTempData();
        subjects = presentFutureSubjects(subjects);
        setClassName();
    }

    private void setClassName(){
        try{
            txtClass.setText(subjects.get(subjectIndex).getName());
        }catch(Exception e){
            return;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView spinnerText = (TextView) view;
        dayOfTheWeek = WeekDay.valueOf(spinnerText.getText().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
