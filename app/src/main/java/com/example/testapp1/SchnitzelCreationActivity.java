package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.testapp1.LocationActivity.locationKey;

public class SchnitzelCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schnitzel_creation);

        Location location = getIntent().getExtras().getParcelable(locationKey);

        setTextView(location);
    }

    public void setTextView(Location location) {
        EditText geoLongi = findViewById(R.id.et);
        EditText geoLati = findViewById(R.id.et2);

        geoLongi.setText(Double.toString(location.getLongitude()));
        geoLati.setText(Double.toString(location.getLatitude()));

    }


}