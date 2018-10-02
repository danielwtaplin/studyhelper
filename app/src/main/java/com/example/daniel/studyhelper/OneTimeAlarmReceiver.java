package com.example.daniel.studyhelper;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Daniel on 16/12/2016.
 */

public class OneTimeAlarmReceiver extends BroadcastReceiver {
    private static Class nextClass;
    private DatabaseManager dbHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        dbHelper = new DatabaseManager(context);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.custom_logo)
                        .setContentTitle( dbHelper.getClassName(nextClass.subjectFK) +" Soon")
                        .setContentText(nextClass.toString())
                        .setTicker("Class Soon")
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_VIBRATE);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE );
       manager.notify(1,mBuilder.build());
    }

    public static void setNextClass(Class c){
        nextClass = c;
    }

}
