package com.days.a30.a30days;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by MacAir on 2018-01-22.
 */
public class NotificationPublisher extends BroadcastReceiver {

    public static final String NOTIFICATION = "NOTIFICATION";
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 1);
        notifManager.notify(id, notif);
    }

}
