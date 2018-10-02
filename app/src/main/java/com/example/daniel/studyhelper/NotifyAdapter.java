package com.example.daniel.studyhelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.DateTime;

/**
 * Created by Daniel on 11/01/2017.
 */

public class NotifyAdapter extends ArrayAdapter<String> {
    private Class[] classArray;
    public NotifyAdapter(Context context, String[] stringArgs, Class[] classArgs) {
        super(context,R.layout.text_action, stringArgs);
        classArray = classArgs;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DatabaseManager dbHelper = new DatabaseManager(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.text_action, parent, false);
        Button notifyBtn = (Button) customView.findViewById(R.id.notifyBtn);
        final TextView txtClass = (TextView) customView.findViewById(R.id.txtClass);
        txtClass.setTag(classArray[position]);
        Class temp = (Class) txtClass.getTag();
        txtClass.setText(dbHelper.getClassName(temp.subjectFK) + "\n" + txtClass.getTag().toString());
        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class temp = (Class) txtClass.getTag();
                OneTimeAlarmReceiver.setNextClass(temp);
                DateTime nowDate = new DateTime();
                DateTime alarmTime = new DateTime(nowDate.getYear(), nowDate.getMonthOfYear(), nowDate.getDayOfMonth(), temp.startTime.getHourOfDay(), temp.startTime.getMinuteOfHour());
                setAlarm(alarmTime.getMillis());
                NotificationActivity.removeClass(temp);
                dbHelper.insertIntoTodayClass(temp.startTime.getHourOfDay(), temp.startTime.getMinuteOfHour());
                Intent openActivity= new Intent(getContext(), NotificationActivity.class);
                openActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(openActivity);
            }
        });
        return customView;
    }

    public void setAlarm(long triggerAtMillis){
        Intent intent = new Intent(getContext(), OneTimeAlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis , pIntent);



    }
}
