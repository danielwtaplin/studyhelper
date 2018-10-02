package com.example.daniel.studyhelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Daniel on 16/01/2017.
 */

public class MarkingSchedule extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private TextView txtClass;
    private ArrayList<Subject> subjects;
    private int subjectIndex;
    private Spinner spinner;
    private ArrayAdapter adapter;
    private AssessmentType assessmentType;
    private Button btnSubmit, btnNext;
    private EditText editName, editWeight;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marking_schedule);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        txtClass = (TextView) findViewById(R.id.txtClass);
        subjects = databaseManager.getTempData();
        subjects = presentFutureSubjects(subjects);
        subjectIndex =0;
        txtClass.setText(subjects.get(subjectIndex).getName());
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.assign_type, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        editName = (EditText) findViewById(R.id.editName);
        editWeight = (EditText) findViewById(R.id.editWeight);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subjectIndex < subjects.size() - 1)
                    subjectIndex++;
                else
                    subjectIndex = 0;
                txtClass.setText(subjects.get(subjectIndex).getName());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView spinnerText = (TextView) view;
        assessmentType = AssessmentType.valueOf(spinnerText.getText().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        subjects = databaseManager.getTempData();
        subjects = presentFutureSubjects(subjects);
    }

    public void handleSubmit(){
        if(editWeight.getText().toString().trim().length() == 0){
            Toast.makeText(getApplicationContext(), "Assessment weighting must be entered", Toast.LENGTH_SHORT).show();
        }
        else{
            String weightText = editWeight.getText().toString();
            int weight = Integer.parseInt(weightText);
            if(databaseManager.canAddAssessment(subjects.get(subjectIndex).primaryKey, weight)){
                Assessment assessment;
                String name;
                if(editName.getText().toString().trim().length() == 0){
                    assessment = new Assessment(assessmentType, weight, subjects.get(subjectIndex).primaryKey, getApplicationContext() );
                }
                else{
                    name = editName.getText().toString();
                    assessment = new Assessment(assessmentType, weight, subjects.get(subjectIndex).primaryKey, name);
                    editName.setText("");
                }
                editWeight.setText("");
                databaseManager.insertResultUngraded(assessment);
            }
            else{
                Toast.makeText(getApplicationContext(), "Total weighting cannot exceed 100%", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
