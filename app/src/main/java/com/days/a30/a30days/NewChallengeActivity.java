package com.days.a30.a30days;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        mNameField.setHint("Challenge Name");
        mDescField = (MaterialEditText) findViewById(R.id.edittext_desc);
        mDescField.setHint("Challenge Description (optional)");

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
                    Challenge challenge = new Challenge(1, mNameField.getText().toString(), mDescField.getText().toString(), false);
                    SharedPrefs.saveNewChallenge(getApplicationContext(), challenge);
                    // set activity flags
                    setResult(Activity.RESULT_OK, null);
                    finish();
                }
            }
        });
    }
}
