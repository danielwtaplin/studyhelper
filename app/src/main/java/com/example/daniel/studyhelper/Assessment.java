package com.example.daniel.studyhelper;

import android.content.Context;

/**
 * Created by Daniel on 17/01/2017.
 */

public class Assessment {
    public AssessmentType type;
    private String name;
    public int weighting;
    public final int subjectFk;
    public double result;
    private DatabaseManager dbHelper;
    public int primaryKey;
    private boolean graded = false;

    public Assessment(AssessmentType type, int weighting, int fk, Context context){
        this.type = type;
        this.weighting = weighting;
        this.subjectFk = fk;
        dbHelper = new DatabaseManager(context);
        this.name = type.toString() + " " + (dbHelper.getResultCount(type, fk) + 1);
    }

    public Assessment(AssessmentType type, int weighting, int fk, String name){
        this.type = type;
        this.weighting = weighting;
        this.subjectFk = fk;
        this.name = name;
    }

    public Assessment(AssessmentType type, int weighting, int fk, double result, String name){
        this.type = type;
        this.weighting = weighting;
        this.subjectFk = fk;
        this.result = result;
        this.name = name;
        this.graded = true;
    }

    public Assessment(AssessmentType type, int weighting, int fk, String name, int primaryKey){
        this.type = type;
        this.weighting = weighting;
        this.subjectFk = fk;
        this.name = name;
        this.primaryKey = primaryKey;
    }


    public Assessment(AssessmentType type, int weighting, int fk, double result, String name, int primaryKey){
        this.type = type;
        this.weighting = weighting;
        this.subjectFk = fk;
        this.result = result;
        this.name = name;
        this.primaryKey = primaryKey;
        this.graded = true;
    }

    public boolean isGraded(){
        if(this.graded)
            return true;
        else
            return false;
    }

    public String toString(){
        if(this.graded)
            return this.name + " \nType:" + this.type.toString() + " \nWeight: " + this.weighting + "%\nResults: " + this.result + "%";
        else
            return this.name + "\nType:" + this.type.toString() + "\nWeight: " + this.weighting + "%\nNot yet graded";
    }

    public String getName(){
        return this.name;
    }
}
