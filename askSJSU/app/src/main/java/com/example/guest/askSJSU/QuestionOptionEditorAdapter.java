package com.example.guest.askSJSU;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class QuestionOptionEditorAdapter extends ArrayAdapter<String> {

    // View lookup cache
    private static class ViewHolder {
        TextView optionName;
        TextView delete;
    }

    QuestionOptionEditorAdapter(Context context, ArrayList<String> optionList) {
        super(context, R.layout.layout_question_option_editor, optionList);
    }

    @Override @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final String questionOption = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_question_option_editor, parent, false);
            viewHolder.optionName = (TextView) convertView.findViewById(R.id.optionNameEditorTextView);
            viewHolder.delete = (TextView) convertView.findViewById(R.id.optionDeleteTextView);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.optionName.setText(questionOption);

        // Add listener to delete button
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(questionOption);
                notifyDataSetChanged();
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
