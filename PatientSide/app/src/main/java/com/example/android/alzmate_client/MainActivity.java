package com.example.android.alzmate_client;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.android.alzmate_client.Model.CameraFragment;
import com.example.android.alzmate_client.Model.ChatFragment;
import com.example.android.alzmate_client.Model.HomeFragment;
import com.example.android.alzmate_client.Model.LogFragment;
import com.example.android.alzmate_client.Model.PeopleFragment;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mNavView;
    private FrameLayout mMainFrame;
    private HomeFragment homeFragment;
    private PeopleFragment peopleFragment;
    private CameraFragment cameraFragment;
    private ChatFragment chatFragment;
    private LogFragment logFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavView=(BottomNavigationView)findViewById(R.id.main_nav);
        disableShiftMode((mNavView));
        mMainFrame=(FrameLayout)findViewById(R.id.main_frame);
        homeFragment=new HomeFragment();
        peopleFragment=new PeopleFragment();
        cameraFragment=new CameraFragment();
        chatFragment=new ChatFragment();
        logFragment=new LogFragment();
        setFragement(homeFragment);
        mNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        setFragement(homeFragment);
                        return true;
                    case R.id.action_people:
                        setFragement(peopleFragment);
                        return true;
                    case R.id.action_camera:
                        setFragement(cameraFragment);
                        return true;
                    case R.id.action_chat:
                        setFragement(chatFragment);
                        return true;
                    case R.id.action_log:
                        setFragement(logFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }
    private void setFragement(Fragment fragement) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragement);
        fragmentTransaction.commit();

    }
    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            if(menuView.getChildCount()<6)
            {
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShifting(false);
                    //item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }
}
