package com.example.daniel.studyhelper;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Daniel on 16/12/2016.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    public static final String databaseName = "Course.db";
    public static String errorMsg;
    public SQLiteDatabase sqLiteDatabase;

    public DatabaseManager(Context context) {
        super(context, databaseName, null, 1);
        sqLiteDatabase = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL("CREATE TABLE TEMP_CLASS(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " NAME TEXT, TUTOR TEXT, START_DATE_YEAR INTEGER, START_DATE_MONTH INTEGER, START_DATE_DAY INTEGER," +
                " FINISH_DATE_YEAR INTEGER, FINISH_DATE_MONTH INTEGER, FINISH_DATE_DAY INTEGER )");
        sqLiteDatabase.execSQL("CREATE TABLE CLASS(ID INTEGER PRIMARY KEY AUTOINCREMENT, SUBJECT_FK INTEGER, ROOM TEXT, " +
                "START_HOUR INTEGER, START_MINUTE INTEGER, FINISH_HOUR INTEGER, FINISH_MINUTE INTEGER, " +
                "DAY TEXT, FOREIGN KEY(SUBJECT_FK) REFERENCES TEMP_CLASS(ID))");
        sqLiteDatabase.execSQL("CREATE TABLE TODAY_CLASS(ID INTEGER PRIMARY KEY AUTOINCREMENT, CLASS_HOUR_FK INTEGER, CLASS_MIN_FK INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE RESULTS(ID INTEGER PRIMARY KEY AUTOINCREMENT, TYPE TEXT, SUBJECT_FK INTEGER, WEIGHT INTEGER, RESULT REAL, NAME TEXT, " +
                "FOREIGN KEY(SUBJECT_FK) REFERENCES TEMP_CLASS(ID))");
        sqLiteDatabase.execSQL("CREATE TABLE NOTES(ID INTEGER PRIMARY KEY AUTOINCREMENT, NOTE TEXT, SUBJECT_FK INTEGER, FAVE INTEGER, FOREIGN KEY(SUBJECT_FK) REFERENCES TEMP_CLASS(ID))");
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();

    }

    public int getResultCount(AssessmentType type, int classFk){
        String query = "SELECT ID FROM RESULTS WHERE TYPE = ? AND SUBJECT_FK = ?";
        sqLiteDatabase.beginTransaction();
        Cursor cursor = (sqLiteDatabase).rawQuery(query, new String[]{type.toString(), String.valueOf(classFk)});
        ArrayList<Integer> resultList = new ArrayList<>();
        if(!cursor.moveToNext()) {
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return 0;
        }
        else
            while(cursor.moveToNext()){
                resultList.add(cursor.getInt(0));
            }
        cursor.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return resultList.size();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TEMP_CLASS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS CLASS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TODAY_CLASS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS RESULTS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NOTES");
        onCreate(sqLiteDatabase);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void insertIntoTodayClass(int fk, int fk1){
        ContentValues contentValues = new ContentValues();
        contentValues.put("CLASS_HOUR_FK", fk);
        contentValues.put("CLASS_MIN_FK", fk1);
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.insert("TODAY_CLASS", null, contentValues);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();

    }

    public void insertIntoNotes(Note note){
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOTE", note.getText());
        contentValues.put("SUBJECT_FK", note.subjectFk);
        contentValues.put("FAVE", 0);
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.insert("NOTES", null, contentValues);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();

    }

    public boolean checkTodayForeignKey(LocalTime startTime){
        String query = "SELECT ID FROM TODAY_CLASS WHERE CLASS_HOUR_FK = ? AND CLASS_MIN_FK = ?";
        sqLiteDatabase.beginTransaction();
        Cursor cursor = (sqLiteDatabase).rawQuery(query, new String[]{String.valueOf(startTime.getHourOfDay()), String.valueOf(startTime.getMinuteOfHour())});
        if(cursor.moveToNext()) {
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return true;
        }
        else {
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return false;
        }
    }


    public boolean checkTodayEmpty(){
        String query = "SELECT ID FROM TODAY_CLASS";
        sqLiteDatabase.beginTransaction();
        Cursor cursor = (sqLiteDatabase).rawQuery(query, null);
        if(cursor.moveToNext()) {
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return false;
        }
        else {
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return true;
        }
    }

    public boolean canAddAssessment(int pk, int value){
        String query = "SELECT WEIGHT FROM RESULTS WHERE SUBJECT_FK = ?";
        sqLiteDatabase.beginTransaction();
        Cursor cursor = (sqLiteDatabase).rawQuery(query, new String[]{String.valueOf(pk)});
        int total = 0;
        while(cursor.moveToNext()){
            total += cursor.getInt(0);
        }
        if(total + value > 100) {
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return false;
        }
        else {
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return true;
        }

    }


    public void insertIntoTempClass(String name, String tutor, int startYear, int startMonth, int startDay, int finishYear, int finishMonth, int finishDay){
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME",name);
        contentValues.put("TUTOR", tutor);
        contentValues.put("START_DATE_YEAR", startYear);
        contentValues.put("START_DATE_MONTH", startMonth);
        contentValues.put("START_DATE_DAY", startDay);
        contentValues.put("FINISH_DATE_YEAR", finishYear);
        contentValues.put("FINISH_DATE_MONTH", finishMonth);
        contentValues.put("FINISH_DATE_DAY", finishDay);
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.insert("TEMP_CLASS", null, contentValues);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void insertIntoClass(int subjectFK, String room, LocalTime startTime, LocalTime finishTime, WeekDay day){
        ContentValues contentValues = new ContentValues();
        contentValues.put("SUBJECT_FK", subjectFK);
        contentValues.put("ROOM", room);
        contentValues.put("START_HOUR", startTime.getHourOfDay());
        contentValues.put("START_MINUTE", startTime.getMinuteOfHour());
        contentValues.put("FINISH_HOUR", finishTime.getHourOfDay());
        contentValues.put("FINISH_MINUTE", finishTime.getMinuteOfHour() );
        contentValues.put("DAY", day.toString());
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.insert("CLASS", null, contentValues);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void insertResultUngraded(Assessment arg){
        ContentValues contentValues = new ContentValues();
        contentValues.put("TYPE", arg.type.toString());
        contentValues.put("SUBJECT_FK", arg.subjectFk);
        contentValues.put("WEIGHT", arg.weighting);
        contentValues.put("NAME", arg.getName());
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.insert("RESULTS", null, contentValues);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void addResult(int pk, double result){
        ContentValues contentValues = new ContentValues();
        contentValues.put("RESULT", result);
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.update("RESULTS", contentValues, "ID = ?", new String[]{String.valueOf(pk)} );
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();

    }

    public int getClassPrimaryKey(String name){
        sqLiteDatabase.beginTransaction();
        Cursor primaryKey = (sqLiteDatabase).rawQuery("SELECT C.ID FROM CLASS AS C INNER JOIN TEMP_CLASS AS S ON C.SUBJECT_FK = S.ID WHERE S.NAME = ?", new String[]{name});
        while(primaryKey.moveToNext()){
            int pk = primaryKey.getInt(0);
            primaryKey.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return pk;
        }
        primaryKey.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return -1;
    }

    public ArrayList<Subject> getTempData(){
        String query = "SELECT * FROM TEMP_CLASS";
        ArrayList<Subject> subjects = new ArrayList<>();
        sqLiteDatabase.beginTransaction();
        Cursor cursor = (sqLiteDatabase).rawQuery(query,null);
        while(cursor.moveToNext()){
            int pK = cursor.getInt(0);
            String name = cursor.getString(1);
            String tutor = cursor.getString(2);
            int startTimeYear = cursor.getInt(3);
            int startTimeMonth = cursor.getInt(4);
            int startTimeDay = cursor.getInt(5);
            int finTimeYear = cursor.getInt(6);
            int finTimeMonth = cursor.getInt(7);
            int finTimeDay = cursor.getInt(8);
            LocalDate startDate = new LocalDate(startTimeYear, startTimeMonth, startTimeDay);
            LocalDate finishDate = new LocalDate(finTimeYear, finTimeMonth, finTimeDay);
            subjects.add(new Subject(pK, name, tutor, startDate, finishDate));
        }
        Collections.sort(subjects);
        cursor.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return subjects;
    }

    public Cursor getTempCursor(){
        String query = "SELECT * FROM TEMP_CLASS";
        sqLiteDatabase.beginTransaction();
        Cursor cursor = (sqLiteDatabase).rawQuery(query, null);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return cursor;
    }

    public void deleteSubject(int pk){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete("TEMP_CLASS", "ID = ?", new String[]{String.valueOf(pk)});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void deleteClassByPrimaryKey(int pk){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete("CLASS", "ID = ?", new String[]{String.valueOf(pk)});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void deleteResultByPrimary(int pk){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete("RESULTS", "ID = ?", new String[]{String.valueOf(pk)});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void deleteResultByForeignKey(int fk){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete("RESULTS", "SUBJECT_FK = ?", new String[]{String.valueOf(fk)});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void deleteClassByForeignKey(int fk){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete("CLASS", "SUBJECT_FK = ?", new String[]{String.valueOf(fk)});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void deleteTodayClass(int hour, int min){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete("TODAY_CLASS", "CLASS_HOUR_FK = ? AND CLASS_MIN_FK = ?", new String[]{String.valueOf(hour), String.valueOf(min)});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void deleteNoteByPrimary(int pk){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete("NOTES", "ID = ?", new String[]{String.valueOf(pk)});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void deleteNoteByForeign(int fk){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete("NOTES", "SUBJECT_FK = ?", new String[]{String.valueOf(fk)});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void clearTempTable(){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete("TEMP_CLASS", null, null);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void clearClassTable(){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete("CLASS", null, null);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void clearTodayClassTable(){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete("TODAY_CLASS", null, null);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void clearResults(){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete("RESULTS", null, null);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void clearNotes(){
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete("NOTES", null, null);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public Cursor getTodayClasses(){
        DateTime date = new DateTime();
        int year = date.getYear();
        int month = date.getMonthOfYear();
        int day = date.getDayOfMonth();
        String today = date.dayOfWeek().getAsText();
        String query = "SELECT C.SUBJECT_FK, C.ROOM, C.START_HOUR, C.START_MINUTE, " +
                "C.FINISH_HOUR, C.FINISH_MINUTE, C.DAY " +
                "FROM CLASS AS C INNER JOIN TEMP_CLASS AS S " +
                "ON C.SUBJECT_FK = S.ID " +
                "WHERE S.ID IN" +
                " (SELECT S.ID FROM TEMP_CLASS AS S WHERE S.START_DATE_YEAR < "  + year +
                    " OR S.START_DATE_YEAR = " + year + " AND START_DATE_MONTH < " + month +
                    " OR S.START_DATE_YEAR = " + year + " AND START_DATE_MONTH = " + month +
                    " AND S.START_DATE_DAY <= " + day + ")" +
                "AND S.ID IN (SELECT S.ID FROM TEMP_CLASS AS S WHERE S.FINISH_DATE_YEAR > " + year +
                    " OR S.FINISH_DATE_YEAR = " + year + " AND S.FINISH_DATE_MONTH > " + month +
                    " OR S.FINISH_DATE_YEAR = " + year + " AND S.FINISH_DATE_MONTH = " + month +
                    " AND S.FINISH_DATE_DAY >= " + day +") " +
                "AND C.DAY = ?  ORDER BY C.START_HOUR";
        sqLiteDatabase.beginTransaction();
        Cursor cursor = (sqLiteDatabase).rawQuery(query, new String[]{today});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return cursor;
    }

    public boolean checkTodayClassRemaining(){
        Cursor cursor = getTodayClasses();
        LocalTime now = new LocalTime();
        while(cursor.moveToNext()){
            int startHour = cursor.getInt(2);
            int startMinute = cursor.getInt(3);
            LocalTime startTime = new LocalTime(startHour, startMinute);
            if(startTime.isAfter(now)) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public void favouriteNote(Note note){
        ContentValues contentValues = new ContentValues();
        int value;
        if(note.checkFave())
            value = 0;
        else
         value = 1;
        contentValues.put("FAVE", value);
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.update("NOTES", contentValues, "ID = ?", new String[]{String.valueOf(note.primaryKey)});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }


    public ArrayList<Class> getTodayClassList(){
        Cursor temp = getTodayClasses();
        ArrayList<Class> todayClasses = new ArrayList<>();
        while(temp.moveToNext()){
            int fk = temp.getInt(0);
            String room = temp.getString(1);
            int startHour = temp.getInt(2);
            int startMinute = temp.getInt(3);
            LocalTime startTime = new LocalTime(startHour, startMinute);
            int finishHour = temp.getInt(4);
            int finishMinute = temp.getInt(5);
            LocalTime finishTime = new LocalTime(finishHour, finishMinute);
            WeekDay day = WeekDay.valueOf(temp.getString(6));
            todayClasses.add(new Class(startTime, finishTime, fk, day));
        }
        temp.close();
        return todayClasses;
    }

    public ArrayList<Class> getAllClasses(){
        String query = "SELECT * FROM CLASS";
        ArrayList<Class> allClasses = new ArrayList<>();
        sqLiteDatabase.beginTransaction();
        Cursor cursor = (sqLiteDatabase).rawQuery(query, null);
        while(cursor.moveToNext()){
            int pk = cursor.getInt(0);
            int fk = cursor.getInt(1);
            String room = cursor.getString(2);
            int startHour = cursor.getInt(3);
            int startMinute = cursor.getInt(4);
            LocalTime startTime = new LocalTime(startHour, startMinute);
            int finishHour = cursor.getInt(5);
            int finishMinute = cursor.getInt(6);
            LocalTime finishTime = new LocalTime(finishHour, finishMinute);
            WeekDay day = WeekDay.valueOf(cursor.getString(7));
            allClasses.add(new Class(startTime, finishTime, fk, day, pk));
        }
        cursor.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return allClasses;
    }

    public ArrayList<Assessment> getAllResults(){
        String query = "SELECT * FROM RESULTS";
        ArrayList<Assessment> resultList = new ArrayList<>();
        sqLiteDatabase.beginTransaction();
        Cursor results = (sqLiteDatabase).rawQuery(query, null);
        while(results.moveToNext()){
            int pk = results.getInt(0);
            AssessmentType type = AssessmentType.valueOf(results.getString(1));
            int fk = results.getInt(2);
            int weight = results.getInt(3);
            double result = results.getDouble(4);
            String name = results.getString(5);
            Assessment assess;
            if(!results.isNull(4))
                assess = new Assessment(type, weight, fk, result ,name, pk );
            else
                assess = new Assessment(type, weight, fk,name, pk );
            results.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            resultList.add(assess);
        }
        results.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return resultList;
    }

    public ArrayList<Note> getAllNotes(){
        String query = "SELECT * FROM NOTES";
        ArrayList<Note> noteList = new ArrayList<>();
        sqLiteDatabase.beginTransaction();
        Cursor notes = (sqLiteDatabase).rawQuery(query, null);
        while(notes.moveToNext()){
            int pk = notes.getInt(0);
            String text = notes.getString(1);
            boolean fave;
            if(notes.getInt(3) == 1)
                fave = true;
            else
                fave = false;
            int fk = notes.getInt(2);
            Note note = new Note(text, fk, fave, pk);
            noteList.add(note);
        }
        notes.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return noteList;
    }

    public ArrayList<Subject> getRunningSubjects(){
        ArrayList<Subject> subjects = getTempData();
        ArrayList<Subject> running = new ArrayList<>();
        for(Subject s : subjects){
            if(s.isRunning())
                running.add(s);
        }
        return running;
    }

    public ArrayList<Note> getFaveNotes(){
        String query = "SELECT * FROM NOTES WHERE FAVE = 1";
        sqLiteDatabase.beginTransaction();
        Cursor notes = (sqLiteDatabase).rawQuery(query, null);
        ArrayList<Note> noteList = new ArrayList<>();
        while(notes.moveToNext()){
            int pk = notes.getInt(0);
            String text = notes.getString(1);
            int fk = notes.getInt(2);
            boolean fave = true;
            Note note = new Note(text,fk, fave, pk);
            noteList.add(note);
        }
        notes.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return noteList;
    }

    public ArrayList<Note> getNotesForSubject(int pk){
        String query = "SELECT TEXT, FK FROM NOTES WHERE FK = ?";
        ArrayList<Note> noteList = new ArrayList<>();
        sqLiteDatabase.beginTransaction();
        Cursor notes = (sqLiteDatabase).rawQuery(query, new String[]{String.valueOf(pk)});
        while(notes.moveToNext()){
            String text = notes.getString(0);
            int fk = notes.getInt(1);
            Note note  = new Note(text, fk);
            noteList.add(note);
        }
        notes.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return noteList;
    }

    public ArrayList<Class> classCursorToArrayList(Cursor cursor){
        ArrayList<Class> classes = new ArrayList<>();
        while(cursor.moveToNext()){
            int fk = cursor.getInt(1);
            String room = cursor.getString(2);
            int startHour = cursor.getInt(3);
            int startMinute = cursor.getInt(4);
            LocalTime startTime = new LocalTime(startHour, startMinute);
            int finishHour = cursor.getInt(5);
            int finishMinute = cursor.getInt(6);
            LocalTime finishTime = new LocalTime(finishHour, finishMinute);
            WeekDay day = WeekDay.valueOf(cursor.getString(7));
            classes.add(new Class(startTime, finishTime, fk, day));
        }
        cursor.close();
        return classes;
    }

    public Cursor getClassByForeignKey(int fK){
        String query = "SELECT * FROM CLASS WHERE SUBJECT_FK = ?";
        sqLiteDatabase.beginTransaction();
        Cursor cursor = (sqLiteDatabase).rawQuery(query, new String[]{String.valueOf(fK)});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return  cursor;
    }

    public Cursor getClassInfo(String request){
        String query = "SELECT * FROM TEMP_CLASS WHERE UPPER(NAME) = ?";
        sqLiteDatabase.beginTransaction();
        Cursor cursor = (sqLiteDatabase).rawQuery(query, new String[]{request});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return cursor;
    }

    public boolean checkName(String name){
        String query = "SELECT ID FROM TEMP_CLASS WHERE NAME = ?";
        sqLiteDatabase.beginTransaction();
        Cursor cursor = (sqLiteDatabase).rawQuery(query, new String[]{name});
        if(cursor.moveToNext()) {
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return true;
        }
        else{
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return false;
        }
    }

    public boolean classIsRunning(int primaryKey){
        DateTime date = new DateTime();
        int year = date.getYear();
        int month = date.getMonthOfYear();
        int day = date.getDayOfMonth();
        String query = "SELECT NAME FROM TEMP_CLASS WHERE ID = ? AND " +
                "ID IN (SELECT ID FROM TEMP_CLASS WHERE START_DATE_YEAR < " + year +
                    " OR START_DATE_YEAR = " + year + " AND START_DATE_MONTH < " + month +
                    " OR START_DATE_YEAR = " + year + " AND START_DATE_MONTH = " + month +
                    " AND START_DATE_DAY <= " + day + ") " +
                "AND ID IN (SELECT ID FROM TEMP_CLASS WHERE FINISH_DATE_YEAR > " + year +
                    " OR FINISH_DATE_YEAR = " + year + " AND FINISH_DATE_MONTH > " + month +
                    " OR FINISH_DATE_YEAR = " + year + " AND FINISH_DATE_MONTH = " + month +
                    " AND FINISH_DATE_DAY >= " + day + ")";
        sqLiteDatabase.beginTransaction();
        Cursor running = (sqLiteDatabase).rawQuery(query, new String[]{String.valueOf(primaryKey)});
        if(running.getCount() == 0) {
            running.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return false;
        }
        else {
            running.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return true;
        }
    }


    public boolean checkClassConflict(LocalTime start, LocalTime finish, WeekDay day, int pk){
        String query = "SELECT SUBJECT_FK, START_HOUR, START_MINUTE, FINISH_HOUR, FINISH_MINUTE" +
                " FROM CLASS WHERE DAY = ?";
        sqLiteDatabase.beginTransaction();
        Cursor conflict = (sqLiteDatabase).rawQuery(query, new String[]{day.toString()});
        while(conflict.moveToNext()){
            int fk = conflict.getInt(0);
            int startHour = conflict.getInt(1);
            int startMin = conflict.getInt(2);
            int finHour = conflict.getInt(3);
            int finMin = conflict.getInt(4);
            LocalTime startTime = new LocalTime(startHour, startMin);
            LocalTime finTime = new LocalTime(finHour, finMin);
            if(start.isBefore(finTime) && finish.isAfter(finTime) || start.isEqual(startTime) && finish.isEqual(finTime) ||
                    start.isAfter(startTime) && start.isBefore(finTime)){
                if(runningTogether(fk, pk)){
                    conflict.close();
                    sqLiteDatabase.setTransactionSuccessful();
                    sqLiteDatabase.endTransaction();
                    return true;
                }
            }
        }
        conflict.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return false;
    }

    public LocalDate getStartDate(int primaryKey){
        String query = "SELECT START_DATE_YEAR, START_DATE_MONTH, START_DATE_DAY FROM TEMP_CLASS WHERE ID = ?";
        sqLiteDatabase.beginTransaction();
        Cursor dates = (sqLiteDatabase).rawQuery(query, new String[]{String.valueOf(primaryKey)});
        LocalDate date;
        while (dates.moveToNext()){
            int year = dates.getInt(0);
            int month = dates.getInt(1);
            int day = dates.getInt(2);
            date = new LocalDate(year, month, day);
            dates.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return date;
        }
        dates.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return new LocalDate();
    }

    public LocalDate getFinishDate(int primaryKey){
        String query = "SELECT FINISH_DATE_YEAR, FINISH_DATE_MONTH, FINISH_DATE_DAY FROM TEMP_CLASS WHERE ID = ?";
        sqLiteDatabase.beginTransaction();
        Cursor dates = (sqLiteDatabase).rawQuery(query, new String[]{String.valueOf(primaryKey)});
        LocalDate date;
        while (dates.moveToNext()){
            int year = dates.getInt(0);
            int month = dates.getInt(1);
            int day = dates.getInt(2);
            date = new LocalDate(year, month, day);
            dates.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return date;
        }
        dates.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return new LocalDate();
    }

    public String getClassName(int pk){
        String query = "SELECT NAME FROM TEMP_CLASS WHERE ID = ?";
        String result;
        sqLiteDatabase.beginTransaction();
        Cursor cursor = (sqLiteDatabase).rawQuery(query, new String[]{String.valueOf(pk)});
        while (cursor.moveToNext()){
            result = cursor.getString(0);
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return result;
        }
        cursor.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return new String();
    }

    public int getClassId(String name){
        String query = "SELECT ID FROM TEMP_CLASS WHERE NAME = ?";
        int id;
        sqLiteDatabase.beginTransaction();
        Cursor className = (sqLiteDatabase).rawQuery(query, new String[]{name});
        while(className.moveToNext()){
            id = className.getInt(0);
            className.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return id;
        }
        className.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return -1;

    }

    public boolean runningTogether(int pk, int fk){
        String query = "SELECT START_DATE_YEAR, START_DATE_MONTH, START_DATE_DAY, FINISH_DATE_YEAR, FINISH_DATE_MONTH, FINISH_DATE_DAY" +
                " FROM TEMP_CLASS WHERE ID = ?";
        sqLiteDatabase.beginTransaction();
        Cursor first = (sqLiteDatabase).rawQuery(query, new String[]{String.valueOf(fk)});
        Cursor second = (sqLiteDatabase).rawQuery(query, new String[]{String.valueOf(pk)});

        while( first.moveToNext()){
            LocalDate firstStartDate = new LocalDate(first.getInt(0), first.getInt(1), first.getInt(2));
            LocalDate firstEndDate = new LocalDate(first.getInt(3), first.getInt(4), first.getInt(5));

            while( second.moveToNext()){
                LocalDate secondStartDate = new LocalDate(second.getInt(0), second.getInt(1), second.getInt(2));
                LocalDate secondEndDate = new LocalDate(second.getInt(3), second.getInt(4), second.getInt(5));

                if(firstStartDate.isEqual(secondStartDate) && firstEndDate.isEqual(secondEndDate) || inBetween(firstStartDate, secondStartDate, secondEndDate) ||
                        inBetween(firstEndDate, secondStartDate, secondEndDate) ){
                    errorMsg = getClassName(pk);
                    first.close();
                    second.close();
                    sqLiteDatabase.setTransactionSuccessful();
                    sqLiteDatabase.endTransaction();
                    return true;
                }
            }
        }
        first.close();
        second.close();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return false;
    }

    public boolean inBetween(LocalDate arg, LocalDate start, LocalDate end){
        if(arg.isAfter(start) && arg.isBefore(end))
            return true;
        else
            return false;
    }

    public Cursor getWeeksClassesByDay(int requestedDay){
        LocalDate requestedDate = new LocalDate().withDayOfWeek(requestedDay);
        WeekDay day = getWeekDay(requestedDay);
        String query = "SELECT S.NAME, C.START_HOUR, C.START_MINUTE, C.FINISH_HOUR, C.FINISH_MINUTE, S.TUTOR, C.ID" +
                " FROM TEMP_CLASS AS S INNER JOIN CLASS AS C ON S.ID = C.SUBJECT_FK " +
                "WHERE S.ID IN" +
            " (SELECT S.ID FROM TEMP_CLASS AS S WHERE S.START_DATE_YEAR < "  + requestedDate.getYear() +
                    " OR S.START_DATE_YEAR = " + requestedDate.getYear() + " AND START_DATE_MONTH < " + requestedDate.getMonthOfYear() +
                    " OR S.START_DATE_YEAR = " + requestedDate.getYear() + " AND START_DATE_MONTH = " + requestedDate.getMonthOfYear() +
                    " AND S.START_DATE_DAY <= " + requestedDate.getDayOfMonth() + ")" +
            " AND S.ID IN (SELECT S.ID FROM TEMP_CLASS AS S WHERE S.FINISH_DATE_YEAR > " + requestedDate.getYear() +
                    " OR S.FINISH_DATE_YEAR = " + requestedDate.getYear() + " AND S.FINISH_DATE_MONTH > " + requestedDate.getMonthOfYear() +
                    " OR S.FINISH_DATE_YEAR = " + requestedDate.getYear() + " AND S.FINISH_DATE_MONTH = " + requestedDate.getMonthOfYear() +
                    " AND S.FINISH_DATE_DAY >= " + requestedDate.getDayOfMonth() +")" +
            " AND C.DAY = ? ORDER BY C.START_HOUR";
        sqLiteDatabase.beginTransaction();
        Cursor daysClasses = (sqLiteDatabase).rawQuery(query, new String[]{day.toString()});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return daysClasses;
    }

    public HashMap<Subject, Class> getClassSubjectHashMap(int requestedDay){
      HashMap<Subject, Class> classMap = new HashMap<>();
      Cursor daysClasses = getWeeksClassesByDay(requestedDay);
        WeekDay day = getWeekDay(requestedDay);
      while(daysClasses.moveToNext()){
          String name = daysClasses.getString(0);
          int startHour = daysClasses.getInt(1);
          int startMin = daysClasses.getInt(2);
          int finishHour = daysClasses.getInt(3);
          int finishMin = daysClasses.getInt(4);
          String tutor =  daysClasses.getString(5);
          int classFk = daysClasses.getInt(6);
          Subject subject = new Subject(name, tutor);
          LocalTime startTime = new LocalTime(startHour, startMin);
          LocalTime finishTime = new LocalTime(finishHour, finishMin);
          Class temp = new Class(startTime, finishTime, classFk, day );
          classMap.put(subject, temp);
      }
        return classMap;
    }

    public ArrayList<String> getClassSubjectString(int requestedDay){
        ArrayList<String> classList = new ArrayList<>();
        Cursor daysClasses = getWeeksClassesByDay(requestedDay);
        WeekDay day = getWeekDay(requestedDay);
        while(daysClasses.moveToNext()){
            String name = daysClasses.getString(0);
            int startHour = daysClasses.getInt(1);
            int startMin = daysClasses.getInt(2);
            int finishHour = daysClasses.getInt(3);
            int finishMin = daysClasses.getInt(4);
            String tutor =  daysClasses.getString(5);
            int classFk = daysClasses.getInt(6);
            Subject subject = new Subject(name, tutor);
            LocalTime startTime = new LocalTime(startHour, startMin);
            LocalTime finishTime = new LocalTime(finishHour, finishMin);
            Class temp = new Class(startTime, finishTime, classFk, day );
            classList.add(new String(subject.toString() + temp.toString() +"\n"));
        }
        return classList;
    }

    public WeekDay getWeekDay(int requestedDay){
        WeekDay day;
        switch (requestedDay) {
            case 1:
                day = WeekDay.Monday;
                break;
            case 2:
                day = WeekDay.Tuesday;
                break;
            case 3:
                day = WeekDay.Wednesday;
                break;
            case 4:
                day = WeekDay.Thursday;
                break;
            case 5:
                day = WeekDay.Friday;
                break;
            default:
                return null;
        }
        return day;
    }

    public Subject getSubject(Class arg){
        Subject subject;
        String query = "SELECT * FROM TEMP_CLASS WHERE ID = ?";
        sqLiteDatabase.beginTransaction();
        Cursor cursor = (sqLiteDatabase).rawQuery(query, new String[]{String.valueOf(arg.subjectFK)});
        while(cursor.moveToNext()){
            int pK = cursor.getInt(0);
            String name = cursor.getString(1);
            String tutor = cursor.getString(2);
            int startTimeYear = cursor.getInt(3);
            int startTimeMonth = cursor.getInt(4);
            int startTimeDay = cursor.getInt(5);
            int finTimeYear = cursor.getInt(6);
            int finTimeMonth = cursor.getInt(7);
            int finTimeDay = cursor.getInt(8);
            LocalDate startDate = new LocalDate(startTimeYear, startTimeMonth, startTimeDay);
            LocalDate finishDate = new LocalDate(finTimeYear, finTimeMonth, finTimeDay);
            subject = new Subject(pK, name, tutor, startDate, finishDate);
            cursor.close();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return subject;
        }
        return new Subject(null, null);

    }


}
