package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView3);
        textView.setText(message);

        // returns PERMISSION_DENIED (-1) or PERMISSON_GRANTED (0)
        int isPermitted = ContextCompat.checkSelfPermission(this, "ACCESS_FINE_LOCATION");

        if (isPermitted == 0) {

        } else if (isPermitted == -1) {

        };
    }
}