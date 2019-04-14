package com.example.android.alzmate_client.Model;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.alzmate_client.R;
import com.example.android.alzmate_client.ReminderDetail.ReminderAdapter;
import com.example.android.alzmate_client.ReminderDetail.ReminderHolder;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends android.support.v4.app.Fragment{
    private ProgressDialog progressDialog;
    private Dialog myDialog;
    private ReminderAdapter reminderAdapter;
    private ListView eventDisplayView;
    private DatabaseReference mReminderDatabaseReference;
    private ArrayList<ReminderHolder> informationEvent;
    private FirebaseAuth mAuth;
    private String time;
    private EditText titleV;
    private EditText bodyV;
    private EditText timeV;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.fragment_home, container, false);
        return retView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventDisplayView=(ListView) view.findViewById(R.id.event_list_view);
        informationEvent=new ArrayList<>();
        progressDialog=(ProgressDialog)new ProgressDialog(this.getContext());
        mAuth= FirebaseAuth.getInstance();
        myDialog = new Dialog(this.getContext());
        FirebaseUser user=mAuth.getCurrentUser();
        mReminderDatabaseReference=FirebaseDatabase.getInstance().getReference().child("PersonAlz").child(user.getUid()).child("events");
        Button addbtn=(Button)view.findViewById(R.id.bfab) ;
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowReminderPopUP();
            }
        });

    }

    private void ShowReminderPopUP() {
        myDialog.setContentView(R.layout.reminderdialog);

        titleV=(EditText) myDialog.findViewById(R.id.title_get_text);
        bodyV=(EditText) myDialog.findViewById(R.id.body_get_text);
        timeV=(EditText) myDialog.findViewById(R.id.time_get_text);
        Button confirmBtn=(Button)myDialog.findViewById(R.id.confirm_btn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String title=titleV.getText().toString().trim();
                String body=bodyV.getText().toString().trim();
                String time=timeV.getText().toString().trim();
               // String date;
                ReminderHolder reminderHolder=new ReminderHolder(title,body,time);
                mReminderDatabaseReference.child("14-April-2019").child(time).setValue(reminderHolder).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        myDialog.dismiss();
                    }
                });
            }
        });


        //Glide.with(getContext()).load(imageUrl).into(imageView);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog.setMessage(getString(R.string.progress_dialog));
        progressDialog.show();
        Date currentTime = Calendar.getInstance().getTime();
        String currentClockTime=currentTime.toString();
        time=currentClockTime.split(" ")[2]+"-April-2019";
        mReminderDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                informationEvent.clear();
                for(DataSnapshot eventSnapshot: dataSnapshot.getChildren())
                {
                    String currentUiD=eventSnapshot.getKey().toString();
                    if(currentUiD.equals(time)) {
                        for(DataSnapshot specficDateSnapshot:eventSnapshot.getChildren()) {
                            String title = specficDateSnapshot.child("title").getValue().toString();
                            String body = specficDateSnapshot.child("body").getValue().toString();
                            String event_time = specficDateSnapshot.child("time").getValue().toString();
                            ReminderHolder mTemporaryEvent = new ReminderHolder(title, body, event_time);
                            informationEvent.add(mTemporaryEvent);
                        }
                    }

                }
                progressDialog.dismiss();
                reminderAdapter =new ReminderAdapter(getActivity(),informationEvent);
                eventDisplayView.setAdapter(reminderAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
