package com.example.deathblade.bottom_nav_bar.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deathblade.bottom_nav_bar.Adaptersnextra.Event;
import com.example.deathblade.bottom_nav_bar.Adaptersnextra.EventsAdapter;
import com.example.deathblade.bottom_nav_bar.MainActivity;
import com.example.deathblade.bottom_nav_bar.R;
import com.google.firebase.FirebaseApp;
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
    ArrayList<Event> mEventsLists;
    EventsAdapter mEventAdapters = new EventsAdapter();

    public Profile() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_events, container, false);

        mRecyclerView = view.findViewById(R.id.profile_events);

        mEvents = new ArrayList<>();

        mAdapter = new EventsAdapter(mEvents);

            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);

//        mEvents.addAll(placeHolderEvents());
//        mAdapter.setData(mEvents);
//        mAdapter.notifyDataSetChanged();
        prepareProfile();
        return view;
    }

    private ArrayList<Event> placeHolderEvents() {
        ArrayList<Event> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String x = "Caption_" + i;
            list.add(new Event("Event Name", x));
        }
        return list;
    }


    public void prepareProfile(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference profile_event = firebaseDatabase.getReference().child("users").child(uid).child("events");
        profile_event.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    mEvents.add(new Event(event.getKey()
                            , (String) event.getValue()));
//                        Log.e("Event:", event.getKey() );
                    mAdapter.setData(mEvents);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        });
        mEventAdapters.notifyDataSetChanged();
    }
}
