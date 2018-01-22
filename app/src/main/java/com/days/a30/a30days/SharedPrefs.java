package com.days.a30.a30days;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Malzberry on 12/17/2017.
 * static shared preference functions for persistence
 */

public final class SharedPrefs {

    private static final String PREFS_FILE_NAME = "30DAYS_PREFS_FILE_NAME";
    private static final String KEY_NAMES_OF_CHALLENGES = "KEY_NAMES_OF_CHALLENGES";

    private SharedPrefs() {}

    public static void editChallenge(Context context, Challenge challenge) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(challenge);
        editor.putString(challenge.mName, json);
        editor.commit();
    }

    public static void saveNewChallenge(Context context, Challenge challenge) {
        // check if challenge exists
        HashSet<String> keyList = getChallengeKeys(context);
        if (!keyList.contains(challenge.mName)) {
            SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).edit();
            // if not, add to list of keys
            keyList.add(challenge.mName);
            editor.putStringSet(KEY_NAMES_OF_CHALLENGES, keyList);
            // commit
            editor.commit();
            // save challenge data
            editChallenge(context, challenge);
        }
    }

    public static ArrayList<Challenge> getAllChallenges(Context context) {
        HashSet<String> keyList = getChallengeKeys(context);
        ArrayList<Challenge> challenges = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);

        // loop through key list, extract challenge obj.
        for (String key : keyList) {
            String json = prefs.getString(key, null);
            if (json != null) {
                // create challenge obj
                Gson gson = new Gson();
                Challenge challenge = gson.fromJson(json, Challenge.class);
                // add to list
                challenges.add(challenge);
            } else {
                throw new JsonParseException("Error parsing json from grabbing challenges");
            }
        }
        return challenges;
    }

    // TODO: we can probably do sth here about the wasted calls to getsharedpref, etc.
    public static boolean doesChallengeExist(Context context, String name) {
        return getChallengeKeys(context).contains(name);
    }

    public static HashSet<String> getChallengeKeys(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        HashSet<String> challengeKeys = new HashSet<>();

        if (prefs.contains(KEY_NAMES_OF_CHALLENGES)) {
            challengeKeys = (HashSet<String>) prefs.getStringSet(KEY_NAMES_OF_CHALLENGES, challengeKeys);
        }

        return challengeKeys;
    }

    public static void deleteChallenge(Context context,Challenge challenge) {
        // remove data itself
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(challenge.mName);
        // also remove from list of challenge keys
        HashSet<String> challengeKeys = getChallengeKeys(context);
        challengeKeys.remove(challenge.mName);
        editor.putStringSet(KEY_NAMES_OF_CHALLENGES, challengeKeys);

        editor.commit();
    }

}
