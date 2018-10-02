package com.example.daniel.studyhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;



public class MainActivity extends BaseActivity{

    private static int imageNum = 0;
    private ImageView imgMon, imgTue, imgWed, imgThu, imgFri;
    private ListView todayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        todayList = (ListView) findViewById(R.id.todayList);
        imgMon = (ImageView) findViewById(R.id.imgMon);
        imgMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadDayImgs();
                imgMon.setImageResource(R.drawable.monday_ro);
                showDaysClasses(1);
            }
        });
        imgTue = (ImageView) findViewById(R.id.imgTue);
        imgTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadDayImgs();
                imgTue.setImageResource(R.drawable.tuesday_ro);
                showDaysClasses(2);
            }
        });
        imgWed = (ImageView) findViewById(R.id.imgWed);
        imgWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadDayImgs();
                imgWed.setImageResource(R.drawable.wednesday_ro);
                showDaysClasses(3);
            }
        });
        imgThu = (ImageView) findViewById(R.id.imgThu);
        imgThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadDayImgs();
                imgThu.setImageResource(R.drawable.thursday_ro);
                showDaysClasses(4);
            }
        });
        imgFri = (ImageView) findViewById(R.id.imgFri);
        imgFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reloadDayImgs();
                imgFri.setImageResource(R.drawable.friday_ro);
                showDaysClasses(5);
            }
        });
    }

    public static File getFile(){
        File folder = new File("sdcard/study_helper");
        if(!folder.exists()){
            folder.mkdir();
        }
        imageNum++;
        File image = new File(folder, "new_image" + imageNum + ".jpg");
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void reloadDayImgs(){
        imgMon.setImageResource(R.drawable.monday);
        imgTue.setImageResource(R.drawable.tuesday);
        imgWed.setImageResource(R.drawable.wednesday);
        imgThu.setImageResource(R.drawable.thursday);
        imgFri.setImageResource(R.drawable.friday);

    }

    private void showDaysClasses(int day){
        ArrayList<String> classArray = databaseManager.getClassSubjectString(day);
        if(classArray.size() > 0){
            String[] classDetails = new String[classArray.size()];
            for(int i =0; i < classArray.size(); i++){
               classDetails[i] = classArray.get(i);
           }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.search_list_item, classDetails);
            todayList.setAdapter(adapter);

            if(todayList.getVisibility() == View.INVISIBLE)
                todayList.setVisibility(View.VISIBLE);
        }
        else {
            todayList.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "No classes on this day", Toast.LENGTH_SHORT).show();
        }
    }
}
