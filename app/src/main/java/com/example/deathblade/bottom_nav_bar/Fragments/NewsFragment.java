package com.example.deathblade.bottom_nav_bar.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deathblade.bottom_nav_bar.Adaptersnextra.CustomLinearLayoutManager;
import com.example.deathblade.bottom_nav_bar.Adaptersnextra.News;
import com.example.deathblade.bottom_nav_bar.Adaptersnextra.NewsAdapter;
import com.example.deathblade.bottom_nav_bar.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapeter;
    private ArrayList<News> mNewsList;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mMessagesDBRef;
    private ChildEventListener mChildEventListener;
    private boolean cleared = false;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseDB = FirebaseDatabase.getInstance();
        mMessagesDBRef = mFirebaseDB.getReference().child("messages");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);


        mRecyclerView = view.findViewById(R.id.recycler_news);
        mNewsList = new ArrayList<>();
        mAdapeter = new NewsAdapter(mNewsList);
        mRecyclerView.setLayoutManager(new CustomLinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapeter);


        if (mNewsList.isEmpty())
            addPlaceholderNews();
        return view;
    }



    private void addPlaceholderNews() {
        for (int i = 0; i < 10; i++) {
            mNewsList.add(new News("Title" + i, "Message" + i));
        }
        String lorem = getString(R.string.placeholder_long);
        mNewsList.add(new News("TestTitle", lorem));

        mAdapeter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDBListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseListener();
    }

    private void attachDBListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    News news = dataSnapshot.getValue(News.class);
                    if (!cleared){
                        cleared = true;
                        mNewsList.clear();
                    }
                    mNewsList.add(news);
                    mAdapeter.notifyItemInserted(mAdapeter.getItemCount() - 1);
                }


                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };
            mMessagesDBRef.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseListener() {
        if (mChildEventListener != null) {
            mMessagesDBRef.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }


}
