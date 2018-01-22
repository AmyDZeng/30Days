package com.days.a30.a30days;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Malzberry on 12/16/2017.
 * Object to hold our challenge data
 */

public class Challenge implements Serializable {
    Long mStartTimestamp;
    String mName;
    String mDesc;
    Long mLastCheckTimestamp;

    public Challenge(String name, String desc) {
        mStartTimestamp = System.currentTimeMillis();
        mName = name;
        mDesc = desc;
        // set timestamp to exactly at midnight
        mLastCheckTimestamp = getLastMidnightCalendar().getTimeInMillis();
    }

    public boolean completeForToday() {
        Date lastMidnightDate = getLastMidnightCalendar().getTime();
        Date lastTimestampDate = new Date(mLastCheckTimestamp);

        return lastTimestampDate.after(lastMidnightDate);
    }

    public boolean failedChallenge() {
        // get last midnight date and go back one day
        Calendar calendar = getLastMidnightCalendar();
        calendar.roll(Calendar.DAY_OF_MONTH, -1);

        Date twoMidnightsAgoDate = calendar.getTime();
        Date lastTimestampDate = new Date(mLastCheckTimestamp);

        return lastTimestampDate.before(twoMidnightsAgoDate);
    }

    private Calendar getLastMidnightCalendar() {
        Calendar calendar = new GregorianCalendar(); // has current date
        // reset to midnight
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    public int getDayCount() {
        // convert start timestamp to its midnight
        Calendar startDayCalendar = new GregorianCalendar();
        startDayCalendar.setTime(new Date(mStartTimestamp));
        startDayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startDayCalendar.set(Calendar.MINUTE, 0);
        startDayCalendar.set(Calendar.SECOND, 0);
        startDayCalendar.set(Calendar.MILLISECOND, 0);
        // compare with most recent midnight
        long mostRecentMidnightMillis = getLastMidnightCalendar().getTimeInMillis();

        long timeDiffMillis = mostRecentMidnightMillis - startDayCalendar.getTimeInMillis();
        return (int) (timeDiffMillis / TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }
}
