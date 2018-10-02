package com.example.daniel.studyhelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Daniel on 2/02/2017.
 */

public class GradeAdapter extends ArrayAdapter<String> {
    private ArrayList<Assessment> items;
    private DatabaseManager databaseManager;
    private Executable exe;
    public GradeAdapter(Context context, ArrayList<Assessment> items, Executable exe) {
        super(context, R.layout.ungraded_item, new String[items.size()]);
        this.items = items;
        databaseManager = new DatabaseManager(context);
        this.exe = exe;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Assessment assessment = items.get(position);
        if(!assessment.isGraded()) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView = inflater.inflate(R.layout.ungraded_item, parent, false);
            TextView subjectTxt = (TextView) customView.findViewById(R.id.subjectTxt);
            subjectTxt.setText(databaseManager.getClassName(assessment.subjectFk));
            final TextView nameTxt = (TextView) customView.findViewById(R.id.nameTxt);
            nameTxt.setText(assessment.getName());
            TextView weightTxt = (TextView) customView.findViewById(R.id.weightTxt);
            weightTxt.setText("Weight: " + assessment.weighting + "%");
            final EditText addResult = (EditText) customView.findViewById(R.id.addResult);
            Button gradeBtn = (Button) customView.findViewById(R.id.gradeBtn);
            gradeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String weightText = addResult.getText().toString();
                    double weight = Double.parseDouble(weightText);
                    if(weight <= 100){
                        databaseManager.addResult(assessment.primaryKey, weight);
                        exe.run(nameTxt);
                        Toast.makeText(getContext(), assessment.getName() + " graded", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(),"Cannot exceed 100%", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            return customView;
        }
        else{
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView = inflater.inflate(R.layout.graded_item, parent, false);
            TextView subjectTxt = (TextView) customView.findViewById(R.id.subjectTxt);
            subjectTxt.setText(databaseManager.getClassName(assessment.subjectFk));
            TextView nameTxt = (TextView) customView.findViewById(R.id.nameTxt);
            nameTxt.setText(assessment.getName());
            TextView weightTxt = (TextView) customView.findViewById(R.id.weightTxt);
            weightTxt.setText("Weight: " + assessment.weighting + "%");
            TextView gradedResult = (TextView) customView.findViewById(R.id.gradedResult);
            gradedResult.setText("Results: " + assessment.result + "%");
            return customView;
        }
    }
}
