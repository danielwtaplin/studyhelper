package com.example.daniel.studyhelper;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static android.support.v4.content.FileProvider.getUriForFile;


/**
 * Created by Daniel on 15/12/2016.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static int MEDIA_TYPE_IMAGE = 1;
    private static int MEDIA_TYPE_VIDEO = 2;
    protected Boolean notificationStatus;
    protected Boolean locationStatus;
    protected DatabaseManager databaseManager;
    protected NotificationManager notifyManager;
    private final int CAMERA_REQUEST_CODE = 1;
    private String fileLocation;
    private final String albumName = "StudyApp";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseManager = new DatabaseManager(this);
    }

    public void setNotificationStatus(boolean state){
        notificationStatus = state;
    }

    public void setLocationStatus(boolean state){
        locationStatus = state;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings:
                try{
                    Intent openActivity= new Intent(this, SettingsActivity.class);
                    openActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(openActivity);
                }
                catch (Exception e){
                    startActivity(new Intent(this, SettingsActivity.class));
                }
                return true;
            case R.id.search:
                try{
                    Intent openActivity= new Intent(this, SearchActivity.class);
                    openActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(openActivity);
                }
                catch (Exception e){
                    startActivity(new Intent(this, SearchActivity.class));
                }
                return true;
            case R.id.alarm:
                try{
                    Intent openActivity= new Intent(this, NotificationActivity.class);
                    openActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(openActivity);
                }
                catch (Exception e){
                    startActivity(new Intent(this, SearchActivity.class));
                }
                return true;
            case R.id.camera:
                return cameraIntent();
            case R.id.note:
                try{
                    Intent openActivity= new Intent(this, NotesActivity.class);
                    openActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(openActivity);
                }
                catch (Exception e){
                    startActivity(new Intent(this, NotesActivity.class));}
                return true;
            case R.id.home:
                try{
                    Intent openActivity= new Intent(this, MainActivity.class);
                    openActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(openActivity);
                }
                catch (Exception e){
                    startActivity(new Intent(this, MainActivity.class));}
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public Boolean cameraIntent(){
        if(isExternalStorageWritable()){
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = null;
            try{
                file = createImageFile();
            }catch(Exception e){
                e.printStackTrace();
                return true;
            }
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            camera.setFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(camera,CAMERA_REQUEST_CODE);
            return true;
        }
        System.out.println("Directory not writable");
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void setAlarm(long triggerAtMillis){
        Intent intent = new Intent(this, OneTimeAlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis , pIntent);
    }

    protected ArrayList presentFutureSubjects(ArrayList<Subject> subjectList){
        ArrayList notEnded = new ArrayList();
        for(Subject s : subjectList){
            if(!s.checkIfEnded())
                notEnded.add(s);
        }
        return notEnded;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
        }
    }

    private File createImageFile() throws IOException {
        String name = "IMG_" + formatDateTime(new DateTime());
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + albumName;
        String path2 = getFilesDir() + File.separator + albumName;
        File directory = new File(path);
        if(!directory.exists()){
            directory.mkdirs();
            System.out.println("No such directory");
        }
        File file = new File(path2 + File.separator + name);
        fileLocation = file.getAbsolutePath();
        System.out.println(path2);
        return file;

    }

    private File getLocalFile(){
        String name = "Capture" + formatDateTime(new DateTime());
        File file = new File(getApplicationContext().getFilesDir(), name);
        return file;
    }

    public String formatDateTime(DateTime dateTime){
        String date = String.valueOf(dateTime.getSecondOfMinute()) +  String.valueOf(dateTime.getMinuteOfHour()) +
                String.valueOf(dateTime.getHourOfDay()) +  String.valueOf(dateTime.getDayOfMonth()) +  String.valueOf(dateTime.getMonthOfYear()) +
                String.valueOf(dateTime.getYear());
        return date;
    }


    private  Uri getOutputMediaFileUri(int type) throws IOException {
        return Uri.fromFile(createImageFile());
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }






}
