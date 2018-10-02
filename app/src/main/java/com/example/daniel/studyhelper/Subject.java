package com.example.daniel.studyhelper;

import android.content.Context;
import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by Daniel on 15/12/2016.
 */

public class Subject implements Comparable{
    private String name;
    private String tutorName;
    private LocalDate startDate;
    private LocalDate finishDate;
    public int primaryKey;

    public Subject(String name, String tutorName){
        this.name = name;
        this.tutorName = tutorName;
    }

    public Subject(String name, String tutorName, LocalDate startDate){
        this.name = name;
        this.tutorName = tutorName;
        this.startDate = startDate;
    }

    public Subject( String name, String tutorName, LocalDate startDate, LocalDate finishDate){

        this.name = name;
        this.tutorName = tutorName;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public Subject( int primaryKey, String name, String tutorName, LocalDate startDate, LocalDate finishDate){
        this.primaryKey = primaryKey;
        this.name = name;
        this.tutorName = tutorName;
        this.startDate = startDate;
        this.finishDate = finishDate;

    }

    public boolean compareForeignKey( Class fk){
        if(this.primaryKey == fk.subjectFK)
            return true;
        else return false;
    }

    public String getName(){
        return this.name;
    }

    public void fillTempSubject(Context context){
        DatabaseManager databaseManager = new DatabaseManager(context);
        databaseManager.insertIntoTempClass(this.name, this.tutorName, this.startDate.getYear(), this.startDate.getMonthOfYear(),
                this.startDate.getDayOfMonth(), this.finishDate.getYear(), this.finishDate.getMonthOfYear(), this.finishDate.getDayOfMonth());
    }


    public String formatDate(LocalDate date){
        return date.getDayOfMonth() + "/" + date.getMonthOfYear() + "/" + date.getYear();
    }
    public String toString(){
        if(this.startDate!= null)
            return "Subject: " + this.name + "\nTutor: " + this.tutorName + "\nStart date: " + formatDate(this.startDate) + "\nFinish date: " + formatDate(this.finishDate);
        else
            return "Subject: " + this.name + "\nTutor: " + this.tutorName + "\n";
    }


    @Override
    public int compareTo(Object o) {
        Subject other = (Subject) o;
        if(this.startDate.isAfter(other.startDate))
            return 1;
        else if(this.startDate.isBefore(other.startDate))
            return -1;
        else
            return 0;
    }

    public boolean checkIfEnded(){
        if(this.finishDate.isBefore(new LocalDate()))
            return true;
        else return false;
    }

    public boolean isRunning(){
        LocalDate today = new LocalDate();
        if((startDate.isBefore(today) || startDate == today) && (finishDate.isAfter(today) || finishDate == today))
            return true;
        else
            return false;
    }
}
