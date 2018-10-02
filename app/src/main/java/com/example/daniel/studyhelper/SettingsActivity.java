package com.example.daniel.studyhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends BaseActivity {
    private TextView infoTxt, binTxt, timetableTxt, removeTxt, resultTxt;
    private ImageView infoIcon, deleteIcon, timetableIcon, removeIcon, resultIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        infoTxt = (TextView) findViewById(R.id.infoTxt);
        infoIcon = (ImageView) findViewById(R.id.infoIcon);
        timetableTxt = (TextView) findViewById(R.id.timetableTxt);
        timetableIcon = (ImageView) findViewById(R.id.timetableIcon);
        deleteIcon = (ImageView) findViewById(R.id.deleteIcon);
        binTxt = (TextView) findViewById(R.id.binTxt);
        removeTxt = (TextView) findViewById(R.id.removeTxt);
        removeIcon = (ImageView) findViewById(R.id.removeIcon);
        resultTxt = (TextView) findViewById(R.id.resultTxt);
        resultIcon = (ImageView) findViewById(R.id.resultIcon);
        timetableTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               handleTimeTableClick();
            }
        });
        timetableIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeTableClick();
            }
        });
        deleteIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAll();
            }
        });
        binTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAll();
            }
        });
        resultIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleResultClick();
            }
        });
        resultTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleResultClick();
            }
        });
        removeTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRemoveClick();
            }
        });
        removeIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRemoveClick();
            }
        });
        infoTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, MakeClassActivity.class));
            }
        });
        infoIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               try{
                   Intent openActivity= new Intent(SettingsActivity.this, MakeClassActivity.class);
                   openActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                   startActivity(openActivity);

               }
               catch(Exception e){
                   startActivity(new Intent(SettingsActivity.this, MakeClassActivity.class));
               }

            }
        });
    }

    public void deleteAll(){
        if(databaseManager.getTempData().isEmpty()){
            Toast.makeText(this, "Files have already been cleared", Toast.LENGTH_SHORT).show();
        }
        else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Are you sure you want to delete all?");
            String[] types = {"Yes","No"};
            dialog.setItems(types, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int selected) {
                    switch(selected){
                        case 0:
                            databaseManager.clearTempTable();
                            databaseManager.clearClassTable();
                            databaseManager.clearTodayClassTable();
                            databaseManager.clearResults();
                            databaseManager.clearNotes();
                            Toast.makeText(getApplicationContext(), "All files have been cleared", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            break;
                    }
                }
            });
            dialog.show();
        }
    }

    public void handleTimeTableClick(){
        if(databaseManager.getTempData().isEmpty()){
            Toast.makeText(SettingsActivity.this, "Fill in class info first ", Toast.LENGTH_SHORT).show();
        }
        else
            startActivity(new Intent(SettingsActivity.this, SetTimeTableActivity.class));
    }

    public void handleResultClick(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Results");
        String[] types = {"Marking Schedule", "Add/View Grades"};
        dialog.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int selected) {
                switch (selected){
                    case 0:
                        if(databaseManager.getTempData().isEmpty()){
                            Toast.makeText(SettingsActivity.this, "Fill in class info first ", Toast.LENGTH_SHORT).show();
                        }
                        else
                            startActivity(new Intent(SettingsActivity.this, MarkingSchedule.class));
                        break;
                    case 1:
                        if(databaseManager.getAllResults().size() == 0){
                            Toast.makeText(SettingsActivity.this, "Nothing added in the marking schedule yet", Toast.LENGTH_SHORT).show();
                        }
                        else
                            startActivity(new Intent(SettingsActivity.this, ViewGradeActivity.class));
                        break;
                }
            }
        });
        dialog.show();

    }

    public void handleRemoveClick(){
        if(!databaseManager.getTempData().isEmpty())
            startActivity(new Intent(SettingsActivity.this, DeleteActivity.class));
        else
            Toast.makeText(getApplicationContext(), "You have no classes to remove", Toast.LENGTH_SHORT).show();
    }
}
