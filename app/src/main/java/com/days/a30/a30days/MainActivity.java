package com.days.a30.a30days;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_NEW_CHALLENGE_SUCCESS = 1;
    public static final String EXTRAS_KEY_CHALLENGE_JSON = "EXTRAS_KEY_CHALLENGE_JSON";

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
                Intent intent = new Intent(view.getContext(), NewChallengeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEW_CHALLENGE_SUCCESS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_NEW_CHALLENGE_SUCCESS) {
            // get challenge from data
            String json = data.getExtras().getString(EXTRAS_KEY_CHALLENGE_JSON);
            Gson gson = new Gson();
            Challenge challenge = gson.fromJson(json, Challenge.class);

            mAdapter.addChallenge(challenge);
        }
    }

    public void setUpRecyclerView() {
        ArrayList<Challenge> data = SharedPrefs.getAllChallenges(this);
        mAdapter = new RVAdapter(data, new RVAdapter.ChallengeButtonListener() {
            @Override
            public void onChallengeClicked(Challenge challenge) {
                // change challenge objs timestamp
                challenge.mLastCheckTimestamp = System.currentTimeMillis();
                // persist into SP
                SharedPrefs.editChallenge(getApplicationContext(), challenge);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

}
