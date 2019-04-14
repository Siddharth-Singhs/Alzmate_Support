package com.example.android.alzmate_client.Model;


import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.alzmate_client.Holder.PersonHolder;
import com.example.android.alzmate_client.LogDetail.LogAdapter;
import com.example.android.alzmate_client.LogDetail.LogHolder;
import com.example.android.alzmate_client.PeopleDetail.PersonAdapter;
import com.example.android.alzmate_client.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

public class LogFragment extends android.support.v4.app.Fragment {
    Dialog myDialog;
    private ProgressDialog progressDialog;
    private LogAdapter logAdapter;
    private ListView logDisplayView;
    private DatabaseReference mUnKnownDatabase;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private ArrayList<LogHolder> informationLog;
    private NotificationCompat.Builder builder;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.fragment_log, container, false);
        return retView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myDialog = new Dialog(this.getContext());
        builder = new NotificationCompat.Builder(getContext());
        logDisplayView=(ListView) view.findViewById(R.id.unknown_person_result_list);
        informationLog=new ArrayList<>();
        progressDialog=(ProgressDialog)new ProgressDialog(this.getContext());
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        mUnKnownDatabase= FirebaseDatabase.getInstance().getReference().child("PersonAlz").child(user.getUid()).child("unknown-people");
        logDisplayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                LogHolder mCurrentLog=(LogHolder) adapterView.getItemAtPosition(position);
                String currentLatitude=mCurrentLog.getLatitude();
                String currentLongitude=mCurrentLog.getLongitude();
                String uri = "http://maps.google.com/maps?saddr=" +"28.589383"+ "," + "77.314630" + "&daddr=" + currentLatitude + "," + currentLongitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }

        });



    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog.show();
        mUnKnownDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                informationLog.clear();
                for (DataSnapshot eachLog:dataSnapshot.getChildren())
                {
                    String imgURL=eachLog.child("imageLocation").getValue().toString();
                    String date=eachLog.child("date").getValue().toString();
                    String time=eachLog.child("time").getValue().toString();
                    LogHolder logHolder=new LogHolder(imgURL,date,time);
                    informationLog.add(logHolder);

                }
                progressDialog.dismiss();
                logAdapter =new LogAdapter(getActivity(),informationLog);
                logDisplayView.setAdapter(logAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
