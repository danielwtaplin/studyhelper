package com.example.daniel.studyhelper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

public class NotesActivity extends BaseActivity {
    private EditText noteEditText;
    private TextView txtSubject, noteLink, txtHeading, txtAll, txtFave;
    private ImageView penIcon, nxtIcon, noteBtn;
    private ArrayList<Subject> subjects;
    private int index;
    private Subject subject;
    private ListView noteListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        txtHeading = (TextView) findViewById(R.id.txtHeading);
        index = 0;
        subjects = databaseManager.getRunningSubjects();
        noteEditText = (EditText) findViewById(R.id.noteEditText);
        txtSubject = (TextView) findViewById(R.id.txtSubject);
        nxtIcon = (ImageView) findViewById(R.id.nxtIcon);
        penIcon = (ImageView) findViewById(R.id.penIcon);
        txtAll = (TextView) findViewById(R.id.txtAll);
        txtFave = (TextView) findViewById(R.id.txtFave);
        noteListView = (ListView) findViewById(R.id.noteListView);
        checkClassRunning();
        penIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(databaseManager.getTempData().size() == 0){
                    Toast.makeText(getApplicationContext(), "add class information before making notes", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(subject == null && subjects.size() == 0){
                    Toast.makeText(getApplicationContext(), "no classes currently running", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(noteEditText.getText().toString().trim().length() > 0){
                    String text = noteEditText.getText().toString();
                    int fk;
                    if(subject == null)
                        fk = subjects.get(index).primaryKey;
                    else
                        fk = subject.primaryKey;
                    System.out.println(fk);
                    Note note = new Note(text, fk);
                    databaseManager.insertIntoNotes(note);
                    noteEditText.setText("");
                    Toast.makeText(getApplicationContext(), "note saved", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "No text entered", Toast.LENGTH_SHORT).show();
            }
        });
        nxtIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < subjects.size() -1)
                    index++;
                else
                    index = 0;
                txtSubject.setText(subjects.get(index).getName());
            }
        });
        noteLink = (TextView) findViewById(R.id.noteLink);
        noteBtn = (ImageView) findViewById(R.id.noteBtn);
        noteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeVisibilities();
            }
        });
        noteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeVisibilities();
            }
        });
        txtAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               viewAll();
            }
        });
        txtFave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              viewFaves();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        subjects = databaseManager.getRunningSubjects();
        if(noteEditText.getVisibility() == View.VISIBLE)
            checkClassRunning();
        else
            viewAll();

    }

    public void checkClassRunning() {
        final LocalTime timeNow = new LocalTime();
        if(new LocalDate().getDayOfWeek() == 6 || new LocalDate().getDayOfWeek() == 7){
            noClassesOn();
            return;
        }
        final WeekDay day = WeekDay.valueOf(new LocalDate().dayOfWeek().getAsText());
        final ArrayList classes = databaseManager.getAllClasses();
        Optional current = classes.stream().findFirst().filter(new java.util.function.Predicate() {
            @Override
            public boolean test(Object o) {
                Class arg = (Class) o;
                if (arg.day == day && arg.startTime.isBefore(timeNow) && arg.finishTime.isAfter(timeNow)
                        && !databaseManager.getSubject(arg).checkIfEnded())
                    return true;
                else
                    return false;
            }
        });
        Class currentClass;
        try{
            currentClass = (Class) current.get();
        }
        catch(Exception e){
            currentClass = null;
        }
        if(currentClass != null){
            subject = databaseManager.getSubject(currentClass);
            Toast.makeText(getApplicationContext(), "take notes for " + subject.getName(), Toast.LENGTH_SHORT).show();
        }
        else{
            if(databaseManager.getTempData().size() == 0){
                Toast.makeText(getApplicationContext(), "no class information present yet", Toast.LENGTH_SHORT).show();
                return;
            }
            if(subjects.size() == 0){
                Toast.makeText(getApplicationContext(), "No classes currently running", Toast.LENGTH_SHORT).show();
                return;
            }
            noClassesOn();
        }
    }

    public void noClassesOn(){
        if(index < subjects.size() && subjects.size() > 0){
            txtSubject.setText(subjects.get(index).getName());
            txtSubject.setVisibility(View.VISIBLE);
            nxtIcon.setVisibility(View.VISIBLE);

        }
        else{
            txtSubject.setVisibility(View.INVISIBLE);
            nxtIcon.setVisibility(View.INVISIBLE);

        }
    }

    public void changeVisibilities(){
        if(noteEditText.getVisibility() == View.VISIBLE){
            if(databaseManager.getAllNotes().size() == 0){
                Toast.makeText(getApplicationContext(), "no notes to view", Toast.LENGTH_SHORT).show();
                return;
            }
            noteEditText.setVisibility(View.INVISIBLE);
            txtSubject.setVisibility(View.INVISIBLE);
            nxtIcon.setVisibility(View.INVISIBLE);
            penIcon.setVisibility(View.INVISIBLE);
            txtHeading.setText("View Notes");
            noteLink.setText("Make note");
            txtAll.setVisibility(View.VISIBLE);
            txtFave.setVisibility(View.VISIBLE);
            noteListView.setVisibility(View.VISIBLE);
            viewAll();
        }
        else{
            noteEditText.setVisibility(View.VISIBLE);
            penIcon.setVisibility(View.VISIBLE);
            checkClassRunning();
            txtHeading.setText("Make a note");
            noteLink.setText("View notes");
            txtAll.setVisibility(View.INVISIBLE);
            txtFave.setVisibility(View.INVISIBLE);
            noteListView.setVisibility(View.INVISIBLE);
        }

    }

    public void viewAll(){
        txtAll.setTextColor(Color.parseColor("#FF69B4"));
        txtFave.setTextColor(Color.parseColor("#FFFFFF"));
        fillList(true);
    }

    public void viewFaves(){
        txtFave.setTextColor(Color.parseColor("#FF69B4"));
        txtAll.setTextColor(Color.parseColor("#FFFFFF"));
        fillList(false);
    }

    public void fillList(Boolean all){
        final boolean state = all;
        ArrayList<Note> notes;
        if(all)
            notes = databaseManager.getAllNotes();
        else
        notes = databaseManager.getFaveNotes();
        ArrayList strings = new ArrayList();
        for(Note n: notes){
            strings.add(n.getText());
        }
        NoteAdapter adapter = new NoteAdapter(getApplicationContext(), notes, strings, new Executable() {
            @Override
            public void run(TextView text) {
                Note note = (Note) text.getTag();
                databaseManager.deleteNoteByPrimary(note.primaryKey);
                fillList(state);
            }
        }, new Executable() {
            @Override
            public void run(TextView text) {
                Note note = (Note) text.getTag();
                databaseManager.favouriteNote(note);
                if(!note.checkFave())
                    Toast.makeText(getApplicationContext(), "note added to favourites", Toast.LENGTH_SHORT).show();
                fillList(state);
            }
        });
        noteListView.setAdapter(adapter);
    }



}
