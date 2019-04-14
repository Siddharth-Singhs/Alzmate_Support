package com.example.android.alzmate_client.BackGroundService;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.alzmate_client.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UnKnownListener extends Service {
    private DatabaseReference unknownReference;
    private NotificationCompat.Builder builder;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Adding a childevent listener to firebase
        builder = new NotificationCompat.Builder(this);
       unknownReference= FirebaseDatabase.getInstance().getReference().child("PersonAlz").child("Uw5U5QJVHDMqaA1RYilZsz76u2l1");
        unknownReference.child("unknown-people").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle("Firebase Push Notification");
                builder.setContentText("Hello this is a test Firebase notification, a new database child has been added");
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return START_STICKY;
        }

        }