package com.example.daniel.studyhelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Daniel on 29/01/2017.
 */

public class NoteAdapter extends ArrayAdapter<String> {
    private ArrayList<String> strings;
    private ArrayList<Note> items;
    private DatabaseManager dbHelper;
    private Executable delete;
    private Executable favourite;
    public NoteAdapter(Context context, ArrayList<Note> items, ArrayList<String> strings, Executable exe1, Executable exe2) {
        super(context, R.layout.note_item, strings);
        dbHelper = new DatabaseManager(context);
        this.items = items;
        this.strings = strings;
        this.delete = exe1;
        this.favourite = exe2;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.note_item, parent, false);
        TextView txtHeader = (TextView) customView.findViewById(R.id.txtHeader);
        txtHeader.setText(dbHelper.getClassName(items.get(position).subjectFk));
        final TextView txtItem = (TextView) customView.findViewById(R.id.txtItem);
        txtItem.setText(strings.get(position));
        txtItem.setTag(items.get(position));
        ImageView deleteIcon = (ImageView) customView.findViewById(R.id.deleteIcon);
        ImageView faveIcon = (ImageView) customView.findViewById(R.id.faveIcon);
        if(items.get(position).checkFave()){
            faveIcon.setImageResource(R.drawable.ic_star_border_white_gold);
        }
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete.run(txtItem);
            }
        });
        faveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favourite.run(txtItem);
            }
        });
        return customView;
    }
}
