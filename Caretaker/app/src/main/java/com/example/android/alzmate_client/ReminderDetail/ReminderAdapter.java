package com.example.android.alzmate_client.ReminderDetail;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.alzmate_client.R;

import java.util.ArrayList;

public class ReminderAdapter extends ArrayAdapter<ReminderHolder> {
    private Activity context;
    private ArrayList<ReminderHolder> allReminder;
    public ReminderAdapter(@NonNull Activity context, ArrayList<ReminderHolder> allReminder) {
        super(context, R.layout.event_list_layout,allReminder);
        this.context=context;
        this.allReminder=allReminder;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        LayoutInflater inflater=context.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.event_list_layout,null,true);

        //
        TextView title=(TextView)listItemView.findViewById(R.id.event_title_txt);
        TextView body=(TextView)listItemView.findViewById(R.id.event_body_text);
        TextView time=(TextView)listItemView.findViewById(R.id.event_tine);

        ReminderHolder currentReminder=allReminder.get(position);
        title.setText(currentReminder.getTitle());
        body.setText(currentReminder.getBody());
        time.setText(currentReminder.getTime());

        return listItemView;
    }
}
