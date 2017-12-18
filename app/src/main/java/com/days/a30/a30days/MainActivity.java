package com.days.a30.a30days;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RVAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        setUpRecyclerView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void setUpRecyclerView() {
        // TODO: grab data here
        ArrayList<Challenge> data = new ArrayList<>();
        data.add(new Challenge(5,"asdf","asdf",false));
        data.add(new Challenge(5,"asdf","asdf",false));
        data.add(new Challenge(5,"asdf","asdf",false));
        data.add(new Challenge(5,"asdf","asdf",false));
        data.add(new Challenge(5,"asdf","asdf",false));
        data.add(new Challenge(5,"asdf","asdf",false));
        data.add(new Challenge(5,"asdf","asdf",false));
        mAdapter = new RVAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    // TODO: handle the mem writes here
    // serialize challenge key based on its data -- make funciton for that
        // should use all the data -- or else duplicates will overwrite.
        // this should be easy, maybe don't include changing data like day or succ/fail
        // during creation of new challenges, check shared pref for the existence.
        // disallow duplicates here
            // raises question -- do we allow editng of the names/desc?
            // since desc might change we might want not to base the key on that ...


    // --> OK, serialize key based on just the name. then disallow editing the name
    //          and allow editing of the description.

    // potentially serialize based on the day count ? no, we could still dupe that data

    // TODO: where to do save? how to save? deets
    // static class with handlers
    // don't think we need to deal with consistency .. or do we?
        // creation at the same time as saving data -- semaphore?
        // this is handled by the editor object, no worries!
}
