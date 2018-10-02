package com.example.daniel.studyhelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Daniel on 2/02/2017.
 */

public class ViewGradeActivity extends BaseActivity {
    private ListView gradeList;
    private GradeAdapter adapter;
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
       setAdapter();
    }

    public void setAdapter(){
        gradeList = (ListView) findViewById(R.id.gradeList);
        adapter = new GradeAdapter(this, databaseManager.getAllResults(), new Executable() {
            @Override
            public void run(TextView text) {
                setAdapter();
            }
        });
        gradeList.setAdapter(adapter);
    }
}
