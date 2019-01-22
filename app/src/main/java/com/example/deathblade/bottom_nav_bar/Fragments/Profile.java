package com.example.deathblade.bottom_nav_bar.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deathblade.bottom_nav_bar.Adaptersnextra.Event;
import com.example.deathblade.bottom_nav_bar.Adaptersnextra.EventsAdapter;
import com.example.deathblade.bottom_nav_bar.MainActivity;
import com.example.deathblade.bottom_nav_bar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    RecyclerView mRecyclerView;
    ArrayList<Event> mEvents;
    EventsAdapter mAdapter;
    String uid = "He2diL0fwtZ7mqm70o2CXNLeJke2";
    CollapsingToolbarLayout toolbarLayout;
    EventsAdapter mEventAdapters = new EventsAdapter();
    private static final String MY_PREFS_NAME = "pref";
    private static final String UID_KEY = "uid";
    private ImageView mProgressView;
    private TextView mProgressTextView;
    private View mEmptyView;
    private View mNoConnectionView;
    private AnimatedVectorDrawableCompat avd;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sign_out)
            FirebaseAuth.getInstance().signOut();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        uid = prefs.getString(UID_KEY, uid);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("User Profile");
        toolbarLayout = view.findViewById(R.id.collapsing);
        setupActionBar(toolbar);


        mRecyclerView = view.findViewById(R.id.profile_events);
        mProgressView = view.findViewById(R.id.login_progress);
        mProgressTextView = view.findViewById(R.id.text_progress);
        mNoConnectionView = view.findViewById(R.id.profile_no_connection);
        mEmptyView = view.findViewById(R.id.profile_empty_view);

        if (isAdded())
            showProgress(true);


        mEvents = new ArrayList<>();
        mAdapter = new EventsAdapter(mEvents, true);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        if (!isNetworkConnected())
            showNoConnectionView(true);
        else
            prepareProfile();
        return view;
    }


    private void setupActionBar(Toolbar toolbar) {
        MainActivity activity = (MainActivity) getActivity();

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(false);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        setHasOptionsMenu(true);
    }


    public void prepareProfile() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference profile_name = firebaseDatabase.getReference().child("users").child(uid);

        profile_name.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s = " ";
                s += (String) dataSnapshot.child("name").getValue();
                if (s == null)
                    Log.e("ASDASD", s);
                toolbarLayout.setTitle(s);
                if (isAdded())
                    showProgress(false);
                for (DataSnapshot event : dataSnapshot.child("events").getChildren()) {
                    mEvents.add(new Event(event.getKey()
                            , (String) event.getValue()));
//                        Log.e("Event:", event.getKey() );
                    mAdapter.setData(mEvents);
                    mAdapter.notifyDataSetChanged();
                }

                mEmptyView.setVisibility(mEvents.isEmpty() ? View.VISIBLE : View.GONE);
                mRecyclerView.setVisibility(mEvents.isEmpty() ? View.GONE : View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        });
        mEventAdapters.notifyDataSetChanged();
    }


    private void showNoConnectionView(boolean show) {
        mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressTextView.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressView.setVisibility(show ? View.GONE : View.VISIBLE);
        mNoConnectionView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mRecyclerView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressTextView.setVisibility(show ? View.VISIBLE : View.GONE);

        avd = AnimatedVectorDrawableCompat.create(mProgressTextView.getContext(), R.drawable.logo_loading_vector_white);
        mProgressView.setImageDrawable(avd);

        final Animatable animatable = (Animatable) mProgressView.getDrawable();
        if (show) {
            animatable.start();
            avd.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    if (show)
                        animatable.start();
                    else
                        animatable.stop();
                }
            });
        } else {
            animatable.stop();
            avd.clearAnimationCallbacks();
            avd = AnimatedVectorDrawableCompat.create(mProgressTextView.getContext(), R.drawable.logo_loading_vector_white);
            mProgressView.setImageDrawable(avd);
        }


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
