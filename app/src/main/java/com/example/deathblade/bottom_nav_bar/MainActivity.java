package com.example.deathblade.bottom_nav_bar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deathblade.bottom_nav_bar.Fragments.EventDetailsFragment;
import com.example.deathblade.bottom_nav_bar.Fragments.Events;
import com.example.deathblade.bottom_nav_bar.Fragments.ListFragment;
import com.example.deathblade.bottom_nav_bar.Fragments.Profile;
import com.example.deathblade.bottom_nav_bar.Fragments.QR_fragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements EventDetailsFragment.OnOpened {

    private TextView mTextMessage;
    public BottomNavigationView navigation;
    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof EventDetailsFragment) {
            EventDetailsFragment headlinesFragment = (EventDetailsFragment) fragment;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.events:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new ListFragment()).commit();
                    return true;
                case R.id.qr:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new QR_fragment()).commit();
                    return true;
                case R.id.notification:
//                    mTextMessage.setText("Live feed");
                    return true;
                case R.id.profile:
//                    mTextMessage.setText("Profile");
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new Profile()).commit();
                    return true;
            }
            return false;
        }
    };


    //Hey there

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        FirebaseApp.initializeApp(this);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new ListFragment()).commit();
    }

    @Override
    public void setclose() {
        navigation.clearAnimation();
        Log.e("LOOKKIIEI","HEERERERE");
        Toast.makeText(this,"Working here",Toast.LENGTH_SHORT).show();
        navigation.animate().translationY(navigation.getHeight()).setDuration(300);
    }
}
