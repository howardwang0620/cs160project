package com.example.guest.askSJSU;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class QuestionOptionAdapter extends ArrayAdapter<QuestionOption> {

    // View lookup cache
    private static class ViewHolder {
        TextView optionName;
        TextView voteCount;
    }

    QuestionOptionAdapter(Context context, ArrayList<QuestionOption> questionList) {
        super(context, R.layout.layout_question_options, questionList);
    }

    @Override @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final QuestionOption option = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_question_options, parent, false);
            viewHolder.optionName = (TextView) convertView.findViewById(R.id.optionNameTextView);
            viewHolder.voteCount = (TextView) convertView.findViewById(R.id.optionVoteCountTextView);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.optionName.setText(option.getOptionName());
        viewHolder.voteCount.setText(getContext().getResources().getString(R.string.option_vote_count, option.getVoteCount()));

        /*
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option.vote();
                notifyDataSetChanged();

                LinkedHashMap<String, String> params = new LinkedHashMap<>();
                params.put("optionid", String.valueOf(option.getOptionId()));
                PerformNetworkRequest vote = new PerformNetworkRequest(Api.URL_UPDATE_QUESTION_OPTION_VOTE, params, Api.CODE_POST_REQUEST);
                vote.execute();
            }
        });
        */
        // Return the completed view to render on screen
        return convertView;
    }
}
