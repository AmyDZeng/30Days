package com.days.a30.a30days;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

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

        Intent notifIntent = new Intent(this, NotificationPublisher.class);
        notifIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 123);
        notifIntent.putExtra(NotificationPublisher.NOTIFICATION, getNotification());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        /*
          set to 12pm every day TODO: can make this user set

          How are we going to do this?
          Our purpose is to remind the user on a daily basis,
          if they've already looked at it today theres no need to remind them.
          Do we want this to be the case? Where we'll need to track when the app is opened

          if they open the app we want to schedule for the next day, what about scheduling for today?
          do we need to schedule for today? if we've opened the app yesterday we would've already scheduled for today
          if we haven't opened the app yesterday we've already broken the chain, all our challenges are failed.
          No point in notifying -- or we make that a separate notif when you fail challenges. We can handle fail in NotificationPublisher

          In conclusion just schedule for tomorrow since the app has to be opened to start a challenge, and opened once a day to be successful.
         */
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long futureMillis = calendar.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureMillis, pendingIntent);
    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(getString(R.string.notif_title))
                .setContentText(String.format(getString(R.string.notif_content), mAdapter.getItemCount()))
                .setVibrate(new long[]{200, 200, 200})
                .setLights(255, 300, 2000)
                        // TODO: replace with new icon
                .setSmallIcon(R.drawable.met_ic_clear);
        return builder.build();
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
