package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EntryActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.testapp1.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
    }

    /** Called when the user taps the send button */
    public void changeActivity(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        //EditText editText = (EditText) findViewById(R.id.textView1);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);

        switch(view.getId()) {
            case R.id.buttonCreate:
                // handle that fucker
                break;
            case R.id.buttonPlay:
                // handle that other fucker
                startActivity(intent);
                break;
            default:
                throw new RuntimeException("Unkown button ID - who dat?!");
        }

    }
}