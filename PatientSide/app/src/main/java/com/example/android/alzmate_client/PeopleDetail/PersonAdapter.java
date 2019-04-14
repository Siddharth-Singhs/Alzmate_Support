package com.example.android.alzmate_client.PeopleDetail;


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
import com.example.android.alzmate_client.Holder.PersonHolder;
import com.example.android.alzmate_client.R;

import java.util.ArrayList;

public class PersonAdapter extends ArrayAdapter<PersonHolder> {

    private Activity context;
    private ArrayList<PersonHolder> allPerson;
    public PersonAdapter(@NonNull Activity context, ArrayList<PersonHolder> allPerson) {
        super(context, R.layout.person_list_layout,allPerson);
        this.context=context;
        this.allPerson=allPerson;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        LayoutInflater inflater=context.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.person_list_layout,null,true);

        //
        TextView name=(TextView)listItemView.findViewById(R.id.name_text);
        TextView relationship=(TextView)listItemView.findViewById(R.id.relationship_text);
        TextView bio=(TextView)listItemView.findViewById(R.id.bio_text);
        ImageView imageView=(ImageView)listItemView.findViewById(R.id.user_image);

        PersonHolder currentPerson=allPerson.get(position);
        name.setText(currentPerson.getName());
        relationship.setText(currentPerson.getRelation());
        bio.setText(currentPerson.getBio());
        String imageUrl=currentPerson.getImageLocation();
        Glide.with(getContext()).load(imageUrl).into(imageView);

        return listItemView;
    }
}
