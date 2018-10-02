package com.example.daniel.studyhelper;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    private TextView txtResults;
    private ImageView searchButton;
    private EditText searchText;
    private ListView listResults;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        txtResults = (TextView) findViewById(R.id.txtResults);
        searchButton = (ImageView) findViewById(R.id.searchButton);
        searchText = (EditText) findViewById(R.id.searchText);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        listResults = (ListView) findViewById(R.id.listResults);
        setSupportActionBar(app_bar);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchText.getText().toString().trim().length() != 0){
                    String searchQuery = searchText.getText().toString();
                     Cursor classInfo = databaseManager.getClassInfo(searchQuery.toUpperCase());
                    if(!classInfo.moveToNext()){
                        Toast.makeText(getApplicationContext(),"No results", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int foreignKey = classInfo.getInt(0);
                        ArrayList<Class> searchClasses = databaseManager.classCursorToArrayList(databaseManager.getClassByForeignKey(foreignKey));
                        ArrayList<String> classes = new ArrayList<>();
                        for(Class c: searchClasses){
                            classes.add(c.toString());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchActivity.this, R.layout.search_list_item, classes);
                        listResults.setAdapter(adapter);
                        txtResults.setVisibility(View.VISIBLE);
                        listResults.setVisibility(View.VISIBLE);
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"No query entered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
