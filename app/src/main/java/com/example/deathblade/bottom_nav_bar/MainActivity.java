package com.example.deathblade.bottom_nav_bar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.example.deathblade.bottom_nav_bar.LoginFlow.LoginActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public BottomNavigationView navigation;
    private int currentID = R.id.events;
    private static final String LOG_TAG = "MainActivity";
    private static final String MY_PREFS_NAME = "pref";
    private static final String UID_KEY = "uid";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 1;
    private boolean isNavHidden = false;
    private boolean anyFragmentAttached = false;

    @Override
    public void onAttachFragment(Fragment fragment) {
        anyFragmentAttached = true;
        if (fragment instanceof EventDetailsFragment) {
            EventDetailsFragment headlinesFragment = (EventDetailsFragment) fragment;
            if (navigation != null)
                navigation.animate().translationY(navigation.getHeight()).setDuration(300).start();
            isNavHidden = true;
        } else {
            if (isNavHidden) {
                isNavHidden = false;
                if (navigation != null)
                    navigation.animate().translationY(0).setDuration(300).start();
            }
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() != currentID) {
                currentID = item.getItemId();
                switch (item.getItemId()) {
                    case R.id.events:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ListFragment()).commit();
                        return true;
                    case R.id.qr:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new QR_fragment()).commit();
                        return true;
                    case R.id.notification:
//                    mTextMessage.setText("Live feed");
                        return true;
                    case R.id.profile:
//                    mTextMessage.setText("Profile");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new Profile()).commit();
                        return true;
                }

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
        mAuth = FirebaseAuth.getInstance();

        /**
         * Checks if the user is logged in. If not, sends them to the login screen.
         */
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {
                    Intent signInIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            }
        };

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        String s = getSupportFragmentManager().isStateSaved() ? "stateSaved" : "notSaved";
        Log.e(LOG_TAG, s);
        if (!anyFragmentAttached)
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ListFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_CANCELED)
                finish();
            if (resultCode == RESULT_OK) {
                String uid = mAuth.getUid();
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString(UID_KEY, uid);
                editor.apply();
                Log.e(LOG_TAG, "UID: " + uid);
            }
        }

    }
}
