package com.example.guest.askSJSU;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class QuestionAdapter extends ArrayAdapter<Question> {

    // View lookup cache
    private static class ViewHolder {
        TextView questionID;
        TextView questionBody;
        TextView dateCreated;
        TextView expirationDate;
        TextView usefulCount;
    }

    QuestionAdapter(Context context, ArrayList<Question> questionList) {
        super(context, R.layout.layout_questions, questionList);
    }

    @Override @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final Question question = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_questions, parent, false);
            viewHolder.questionID = (TextView) convertView.findViewById(R.id.questionIDTextView);
            viewHolder.questionBody = (TextView) convertView.findViewById(R.id.questionBodyTextView);
            viewHolder.dateCreated = (TextView) convertView.findViewById(R.id.dateCreatedTextView);
            viewHolder.expirationDate = (TextView) convertView.findViewById(R.id.expirationDateTextView);
            viewHolder.usefulCount = (TextView) convertView.findViewById(R.id.usefulCountTextView);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.questionID.setText(getContext().getResources().getString(R.string.question_id, question.getQuestionId()));
        viewHolder.questionBody.setText(question.getQuestionBody());
        viewHolder.dateCreated.setText(getContext().getResources().getString(R.string.date_created, question.getDateCreated().toString()));
        viewHolder.expirationDate.setText(getContext().getResources().getString(R.string.expiration_date, question.getExpirationDate().toString()));
        viewHolder.usefulCount.setText(getContext().getResources().getString(R.string.useful_count, question.getUsefulCount()));

        // Return the completed view to render on screen
        return convertView;
    }
}
