package com.example.daniel.studyhelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Daniel on 13/01/2017.
 */

public class MyService extends Service {
    private Thread thread;
    public static long trigger;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public MyService(){

    }

    public MyService(long trigger_){
        trigger = trigger_;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.thread = new Thread() {

            @Override
            public void run() {
                OneTimeAlarmReceiver receiver = new OneTimeAlarmReceiver();
                Intent intent = new Intent(getBaseContext(), OneTimeAlarmReceiver.class);
                PendingIntent pIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger , pIntent);
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.thread.start();
        return super.onStartCommand(intent, flags, startId);

    }
}
