package com.example.daniel.studyhelper;

/**
 * Created by Daniel on 27/01/2017.
 */

public class Note {
    private boolean fave;
    private String text;
    public int subjectFk;
    public int primaryKey;

    public Note(String text, int fk){
        this.text = text;
        this.subjectFk = fk;
    }

    public Note(String text, int fk, boolean fave){
        this.text = text;
        this.subjectFk = fk;
        this.fave = fave;
    }
    public Note(String text, int fk, boolean fave, int pk){
        this.text = text;
        this.subjectFk = fk;
        this.fave = fave;
        this.primaryKey = pk;
    }

    public String getText(){return this.text;}

    public boolean checkFave(){
        if(fave)
            return true;
        else
            return false;
    }

    public String toString(){
        return "Note: " + this.text + " Fk: " + this.subjectFk;
    }
}
