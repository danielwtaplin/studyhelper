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
 * Created by Daniel on 22/01/2017.
 */

public class RemoveAdapter extends ArrayAdapter<String> {
    private String[] strings;
    private Executable exe;
    private boolean subject = false;
    private ArrayList items;
    public RemoveAdapter(Context context, String[] stringArgs, Executable exe, ArrayList items) {
        super(context, R.layout.text_remove, stringArgs);
        strings = stringArgs;
        this.exe = exe;
        this.items = items;
    }
    public RemoveAdapter(Context context, String[] stringArgs, Executable exe, ArrayList items, boolean subject) {
        super(context, R.layout.text_remove, stringArgs);
        strings = stringArgs;
        this.exe = exe;
        this.items = items;
        this.subject = subject;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.text_remove, parent, false);
        final TextView txtItem = (TextView) customView.findViewById(R.id.txtItem);
        ImageView removeIcon = (ImageView) customView.findViewById(R.id.removeIcon);
        txtItem.setText(strings[position]);
        txtItem.setTag(items.get(position));
        TextView endedTxt = (TextView) customView.findViewById(R.id.endedTxt);
        if(this.subject == true){
            Subject s = (Subject) items.get(position);
            if(s.checkIfEnded())
                endedTxt.setVisibility(View.VISIBLE);
        }

        removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exe.run(txtItem);
            }
        });
        return customView;
    }
}
