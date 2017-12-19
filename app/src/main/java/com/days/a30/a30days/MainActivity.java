package com.days.a30.a30days;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
    AlertDialog.Builder mDeleteDialogBuilder;

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

        scheduleNotifIfNecessary();
        // Need to pass in pointer to activity
        mDeleteDialogBuilder = new AlertDialog.Builder(this);
    }

    private void scheduleNotifIfNecessary() {
        if (mAdapter.getItemCount() == 0) return;

        Notification notif = new Notification.Builder(this)
                .setContentTitle(getString(R.string.notif_title))
                .setContentText(String.format(getString(R.string.notif_content), mAdapter.getItemCount()))
                .setVibrate(new long[]{200, 200, 200})
                .setLights(255, 300, 2000)
                // TODO: replace with new icon
                .setSmallIcon(R.drawable.met_ic_clear)
                .build();

        NotificationManager notifManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.notify(123, notif);
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
        mAdapter = new RVAdapter(data, new RVAdapter.ChallengeListener() {
            @Override
            public void onChallengeClicked(Challenge challenge) {
                // change challenge objs timestamp
                challenge.mLastCheckTimestamp = System.currentTimeMillis();
                // persist into SP
                SharedPrefs.editChallenge(getApplicationContext(), challenge);
            }

            @Override
            public void onChallengeLongClick(final Challenge challenge) {
                mDeleteDialogBuilder
                        .setTitle(R.string.delete_challenge)
                        .setMessage(R.string.confirm_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // delete from SP
                                SharedPrefs.deleteChallenge(getApplicationContext(), challenge);
                                // remove from mAdapter
                                mAdapter.removeChallenge(challenge);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // no-op
                            }
                        })
                        .show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

}
