package com.days.a30.a30days;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Malzberry on 12/17/2017.
 */

public class NewChallengeActivity extends Activity {

    Button mButton;
    MaterialEditText mNameField;
    MaterialEditText mDescField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_class);

        mNameField = (MaterialEditText) findViewById(R.id.edittext_name);
        mDescField = (MaterialEditText) findViewById(R.id.edittext_desc);

        mButton = (Button) findViewById(R.id.button_create_challenge);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if fields are empty
                // TODO: replace error toast using material edittexts build in functionality
                if (mNameField.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "Please enter a name for your new challenge", Toast.LENGTH_SHORT).show();
                } else if (SharedPrefs.doesChallengeExist(v.getContext(), mNameField.getText().toString())) {
                    Toast.makeText(v.getContext(), "A challenge with that name already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // make the challenge, save the data to SP
                    Challenge challenge = new Challenge(1, mNameField.getText().toString(), mDescField.getText().toString());
                    SharedPrefs.saveNewChallenge(getApplicationContext(), challenge);
                    // set activity flags
                    Intent extras = new Intent();

                    Gson gson = new Gson();
                    String json = gson.toJson(challenge, Challenge.class);
                    extras.putExtra(MainActivity.EXTRAS_KEY_CHALLENGE_JSON, json);

                    setResult(Activity.RESULT_OK, extras);
                    finish();
                }
            }
        });

        stylizeFields();
    }

    private void stylizeFields() {
        mNameField.setHelperText("Challenge Name");
        mNameField.setHelperTextAlwaysShown(true);
        mNameField.setHint("Wake up early");

        mDescField.setHelperText("Challenge Description (optional)");
        mDescField.setHelperTextAlwaysShown(true);
        mDescField.setHint("Out of bed by 7am! No snoozing!");
    }
}
