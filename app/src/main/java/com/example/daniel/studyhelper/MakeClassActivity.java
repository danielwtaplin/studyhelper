package com.example.daniel.studyhelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import java.util.ArrayList;



public class MakeClassActivity extends BaseActivity {
    private EditText editTextName, editTextTutor;
    private DatePicker datePicker, datePickerFin;
    private Button submitBtn, doneBtn;
    private Switch startFinSwitch;
    private TextView txtView, txtViewFin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_class);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextTutor = (EditText) findViewById(R.id.editTextTutor);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        doneBtn = (Button) findViewById(R.id.doneBtn);
        txtView = (TextView) findViewById(R.id.txtView);
        txtView.setTextColor(Color.parseColor("#FF69B4"));
        txtViewFin = (TextView) findViewById(R.id.txtViewFin);
        datePickerFin = (DatePicker) findViewById(R.id.datePickerFin);
        startFinSwitch = (Switch) findViewById(R.id.startFinSwitch);
        startFinSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(datePicker.getVisibility() == View.VISIBLE){
                    datePicker.setVisibility(View.INVISIBLE);
                    datePickerFin.setVisibility(View.VISIBLE);
                    txtViewFin.setTextColor(Color.parseColor("#FF69B4"));
                    txtView.setTextColor(Color.parseColor("#FFFFFF"));
                }
                else{
                    datePicker.setVisibility(View.VISIBLE);
                    datePickerFin.setVisibility(View.INVISIBLE);
                    txtViewFin.setTextColor(Color.parseColor("#FFFFFF"));
                    txtView.setTextColor(Color.parseColor("#FF69B4"));
                }
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextName.getText().toString().trim().length() == 0 || editTextTutor.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "Not enough information provided", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(databaseManager.checkName(editTextName.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Class already added", Toast.LENGTH_SHORT).show();
                    return;
                }
                LocalDate start = getDateFromPicker(datePicker);
                LocalDate finish = getDateFromPicker(datePickerFin);

                if(datePicker.getYear() == datePickerFin.getYear() &&
                        datePicker.getMonth() == datePickerFin.getMonth() &&
                        datePicker.getDayOfMonth() == datePickerFin.getDayOfMonth()){
                    Toast.makeText(getApplicationContext(), "Start and finish date cannot be the same", Toast.LENGTH_SHORT).show();
                    return;
                }
                DateTime now = new DateTime();
                if(datePickerFin.getYear() < now.getYear() || datePickerFin.getYear() == now.getYear() && datePickerFin.getMonth() + 1 < now.getMonthOfYear() ||
                        datePickerFin.getYear() == now.getYear() && datePickerFin.getMonth() + 1 == now.getMonthOfYear() && datePickerFin.getDayOfMonth() <= now.getDayOfMonth()){
                    Toast.makeText(getApplicationContext(), "Finish date cannot be in the past", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(finish.isBefore(start)){
                    Toast.makeText(getApplicationContext(), "Finish date cannot be before start date", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Subject(editTextName.getText().toString(),editTextTutor.getText().toString(), start, finish).fillTempSubject(MakeClassActivity.this);
                editTextName.setText("");
                editTextTutor.setText("");
                Toast.makeText(getApplicationContext(), "Class saved to list", Toast.LENGTH_SHORT).show();
            }
        });
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Subject> subjectList = databaseManager.getTempData();
                if(subjectList.size() > 0)
                    startActivity(new Intent(MakeClassActivity.this, SetTimeTableActivity.class));
                else
                    Toast.makeText(getApplicationContext(), "No classes added", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static LocalDate getDateFromPicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year =  datePicker.getYear();
        LocalDate dateToReturn = new LocalDate(year, month, day);
        return dateToReturn;
    }

}
