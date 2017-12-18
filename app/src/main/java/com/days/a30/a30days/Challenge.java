package com.days.a30.a30days;

/**
 * Created by Malzberry on 12/16/2017.
 */

public class Challenge {
    int mDayCount;
    String mName;
    String mDesc;
    boolean mCompleteForToday;

    public Challenge(int days, String name, String desc, boolean complete) {
        mDayCount = days;
        mName = name;
        mDesc = desc;
        mCompleteForToday = complete;
    }
}
