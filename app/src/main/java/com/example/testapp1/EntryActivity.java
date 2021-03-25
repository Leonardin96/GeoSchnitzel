package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
    }

    /**
     * Called when the user taps the send button.
     */
    public void changeActivity(View view) {
        Intent intent = new Intent(this, LocationActivity.class);

        switch(view.getId()) {
            case R.id.buttonCreate:
                startActivity(intent);
                break;
            case R.id.buttonPlay:
                break;
            default:
                throw new RuntimeException("Unkown button ID!");
        }

    }
}