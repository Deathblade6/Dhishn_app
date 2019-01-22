package com.example.deathblade.bottom_nav_bar.Fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deathblade.bottom_nav_bar.Adaptersnextra.Coordinator;
import com.example.deathblade.bottom_nav_bar.Adaptersnextra.CustomLinearLayoutManager;
import com.example.deathblade.bottom_nav_bar.Adaptersnextra.Event;
import com.example.deathblade.bottom_nav_bar.Adaptersnextra.EventsAdapter;
import com.example.deathblade.bottom_nav_bar.Adaptersnextra.FlagshipAdapter;
import com.example.deathblade.bottom_nav_bar.Listeners.RecyclerTouchListener;
import com.example.deathblade.bottom_nav_bar.MainActivity;
import com.example.deathblade.bottom_nav_bar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    RecyclerView mFlagshipRecyclerView;
    EventsAdapter[] mEventAdapters;
    RecyclerView[] mEventRecyclers;
    ArrayList<Event>[] mEventsLists;
    TypedArray mRecyclerIDs;
    FlagshipAdapter mFlagshipAdapter;
    ArrayList<Event> flagshipEvents;
    Handler mHandler;
    Runnable mRunnable;
    Boolean mIsScrolling = false;
    Timer mTimer;
    private boolean isConnected;
    int no_of_dept = 8;
    String LOG_TAG = "ListFragment";
    private View mContentView;
    private AnimatedVectorDrawableCompat avd;
    private ImageView mProgressView;
    private TextView mProgressTextView;
    private View mNoConnectionView;
    private Toolbar mToolbar;

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
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mContentView = view.findViewById(R.id.content);
        mProgressView = view.findViewById(R.id.login_progress);
        mProgressTextView = view.findViewById(R.id.text_progress);
        mNoConnectionView = view.findViewById(R.id.profile_no_connection);
        mToolbar = view.findViewById(R.id.toolbar);

        if (isAdded()){
            MainActivity activity = (MainActivity) getActivity();
            activity.setSupportActionBar(mToolbar);

        }

        setHasOptionsMenu(true);


        isConnected = isNetworkConnected();


        if (isAdded())
            showProgress(true);

        flagshipEvents = new ArrayList<>();
        mFlagshipAdapter = new FlagshipAdapter(flagshipEvents);
        mFlagshipRecyclerView = view.findViewById(R.id.flagship_recycler_view);
        mFlagshipRecyclerView.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mFlagshipRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFlagshipRecyclerView.setAdapter(mFlagshipAdapter);
        mFlagshipRecyclerView.setNestedScrollingEnabled(false);
        mFlagshipRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mFlagshipRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Event clickedEvent = mFlagshipAdapter.getEventAtIndex(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("event", clickedEvent);
                animateToDetails(view, bundle);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mFlagshipRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e(LOG_TAG, "" + newState);
                if (newState == SCROLL_STATE_DRAGGING) {
                    mTimer.cancel();
                    mIsScrolling = false;
                    Log.e(LOG_TAG, "removing");
                }
                if (newState == SCROLL_STATE_IDLE)
                    if (!mIsScrolling)
                        setUpAutoScroll(mFlagshipAdapter.getCurrentPos());
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        mEventRecyclers = new RecyclerView[no_of_dept];
        mEventsLists = new ArrayList[no_of_dept];
        mEventAdapters = new EventsAdapter[no_of_dept];
        if (isAdded())
            mRecyclerIDs = getResources().obtainTypedArray(R.array.departments_recycler_views);
        for (int i = 0; i < no_of_dept; i++) {
            mEventsLists[i] = new ArrayList<>();
            int id = mRecyclerIDs.getResourceId(i, 0);
            mEventRecyclers[i] = (RecyclerView) view.findViewById(id);
            mEventAdapters[i] = new EventsAdapter(mEventsLists[i], i);
            mEventRecyclers[i].setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            mEventRecyclers[i].setItemAnimator(new DefaultItemAnimator());
            mEventRecyclers[i].setNestedScrollingEnabled(true);
            mEventRecyclers[i].setAdapter(mEventAdapters[i]);
            final int finalI = i;
            mEventRecyclers[i].addOnItemTouchListener(new RecyclerTouchListener(getContext(), mEventRecyclers[i], new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Event clickedEvent = mEventAdapters[finalI].getEventAtIndex(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("event", clickedEvent);
                    animateToDetails(view, bundle);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

        }

        if (isConnected) {
            prepareFlagshipEvents();
            setUpAutoScroll(0);
            prepareDepartments();
        } else
            showNoConnectionView(true);


        return view;
    }

    /**
     * private void prepareFlagshipEventsWithPlaceHolders() {
     * flagshipEvents.addAll(placeHolderEvents());
     * }
     * <p>
     * private void prepareDepartmentsWithPlaceHolders() {
     * for (int i = 0; i<no_of_dept; i++){
     * mEventsLists[i].addAll(placeHolderEvents());
     * mEventAdapters[i].notifyDataSetChanged();
     * }
     * }
     * <p>
     * private void removeFlagshipPlaceholders() {
     * flagshipEvents.clear();
     * mFlagshipAdapter.notifyDataSetChanged();
     * }
     * <p>
     * private void removeEventPlaceHolders() {
     * for (int i=0; i<no_of_dept; i++){
     * mEventsLists[i].clear();
     * }
     * }
     */


    private void animateToDetails(View view, Bundle bundle) {
//        TODO: Build version check.

        setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.shared));
        setExitTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.no_transition));

        EventDetailsFragment nextPage = new EventDetailsFragment();

// IMPORTANT ERROR(Ignored to add firebase capabilities)-
        nextPage.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.default_trans));
        nextPage.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.no_transition));
        nextPage.setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.no_transition));
        nextPage.setExitTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.no_transition));
        nextPage.setArguments(bundle);
        openDetails(nextPage, view);

    }

    private void openDetails(EventDetailsFragment nextPage, View view) {
        String transName = getString(R.string.transition_string);
        getFragmentManager().beginTransaction()
                .addSharedElement(view, transName)
                .replace(R.id.frame_layout, nextPage)
                .commit();
    }

    private void setUpAutoScroll(final int pos) {
        mTimer = new Timer();
        TimerTask task = new TimerTask() {
            int count = pos;
            boolean flag = true;

            @Override
            public void run() {
                if (count < mFlagshipAdapter.getItemCount()) {
                    if (count == mFlagshipAdapter.getItemCount() - 1) {
                        flag = false;
                    } else if (count == 0) {
                        flag = true;
                    }
                    if (flag) count++;
                    else count--;

                    mFlagshipRecyclerView.smoothScrollToPosition(count);
                }
            }
        };
        mIsScrolling = true;
        mTimer.scheduleAtFixedRate(task, 3000, 3200);
    }


    private void prepareFlagshipEvents() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference event_ref = database.getReference().child("events").child("co");
        event_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    ArrayList<Coordinator> coordinators = new ArrayList<>();
                    for (DataSnapshot coordinator : event.child("coordinators").getChildren()) {
                        coordinators.add(coordinator.getValue(Coordinator.class));
                    }

                    flagshipEvents.add(new Event(event.getKey()
                            , (String) event.child("caption").getValue()
                            , (String) event.child("description").getValue()
                            , (String) event.child("rules").getValue()
                            , (String) event.child("prize1").getValue()
                            , (String) event.child("prize2").getValue()
                            , (String) event.child("prize3").getValue()
                            , (String) event.child("fee").getValue()
                            , (String) event.child("registration").getValue()
                            , (String) event.child("insta").getValue()
                            , coordinators.get(0)
                            , coordinators.get(1)));
//                        Log.e("Event:", event.getKey() );
                    mFlagshipAdapter.notifyDataSetChanged();
                    //hi

                }
                if (isAdded())
                    showProgress(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        });
        mFlagshipAdapter.notifyDataSetChanged();


    }


    private void prepareDepartments() {
        for (int i = 0; i < no_of_dept; i++) {
            final int cur = i;
            List<String> events = Arrays.asList("ws", "ee", "ec", "ce", "cs", "it", "me", "se");
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference event_ref = database.getReference().child("events").child(events.get(i));
            event_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot event : dataSnapshot.getChildren()) {
                        ArrayList<Coordinator> coordinators = new ArrayList<>();
                        for (DataSnapshot coordinator : event.child("coordinators").getChildren()) {
                            coordinators.add(coordinator.getValue(Coordinator.class));
                        }

                        mEventsLists[cur].add(new Event(event.getKey()
                                , (String) event.child("caption").getValue()
                                , (String) event.child("description").getValue()
                                , (String) event.child("rules").getValue()
                                , (String) event.child("prize1").getValue()
                                , (String) event.child("prize2").getValue()
                                , (String) event.child("prize3").getValue()
                                , (String) event.child("fee").getValue()
                                , (String) event.child("registration").getValue()
                                , (String) event.child("insta").getValue()
                                , coordinators.get(0)
                                , coordinators.get(1)));
//                        Log.e("Event:", event.getKey() );
                        mEventAdapters[cur].notifyDataSetChanged();
                        //hi

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }


            });
            mEventAdapters[i].notifyDataSetChanged();
        }

    }


    private void showNoConnectionView(boolean show) {
        mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressTextView.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressView.setVisibility(show ? View.GONE : View.VISIBLE);
        mNoConnectionView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mContentView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    /**
     * private ArrayList<Event> placeHolderEvents() {
     * ArrayList<Event> list = new ArrayList<>();
     * for (int i = 0; i < 10; i++) {
     * String x = "Caption_" + i;
     * list.add(new Event("Event Name", x));
     * }
     * return list;
     * }
     */

    @Override
    public void onPause() {
        super.onPause();
        if (isConnected)
            mTimer.cancel();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
