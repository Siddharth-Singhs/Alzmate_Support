package com.example.android.alzmate_client.LogDetail;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.android.alzmate_client.R;

import java.util.ArrayList;

public class LogAdapter extends ArrayAdapter<LogHolder>{
    private Activity context;
    private ArrayList<LogHolder> allLog;
    public LogAdapter(@NonNull Activity context, ArrayList<LogHolder> allLog) {
        super(context, R.layout.unknown_detail_list,allLog);
        this.context=context;
        this.allLog=allLog;

    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        LayoutInflater inflater=context.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.unknown_detail_list,null,true);

        //
        TextView date=(TextView)listItemView.findViewById(R.id.date_text);
        TextView time=(TextView)listItemView.findViewById(R.id.time_text);
        ImageView imageView=(ImageView)listItemView.findViewById(R.id.user_image);

        LogHolder currentLog=allLog.get(position);
        date.setText(currentLog.getDate());
        time.setText(currentLog.getTime());
        String imageUrl=currentLog.getImageLocation();
        Glide.with(getContext()).load(imageUrl).into(imageView);

        return listItemView;
    }
}

