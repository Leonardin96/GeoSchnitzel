package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class DisplayMessageActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);



        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        final TextView textView = findViewById(R.id.textView3);
        textView.setText(message);

        // returns PERMISSION_DENIED (-1) or PERMISSON_GRANTED (0)
        int isPermitted = ContextCompat.checkSelfPermission(this, "ACCESS_FINE_LOCATION");

        if (isPermitted == 0) {
            final Activity thisActivity = this;
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double lastKnownLongitude = location.getLongitude();
                                double lastKnownLatitude = location.getLatitude();
                                textView.setText(Double.toString(lastKnownLatitude));
                            } else {
                                // Initializing new locationRequests - one high and one balanced
                                LocationRequest mLocationRequestHighAccuracy = new LocationRequest().setPriority(100);
                                LocationRequest mLocationRequestBalancedPowerAccuracy = new LocationRequest().setPriority(102);

                                // creating a builder and add the locationRequests
                                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                                        .addLocationRequest(mLocationRequestHighAccuracy)
                                        .addLocationRequest(mLocationRequestBalancedPowerAccuracy);
                                builder.setNeedBle(true);

                                // using the builder to check wether the locationSettings are as needed
                                Task<LocationSettingsResponse> result =
                                        LocationServices.getSettingsClient(thisActivity).checkLocationSettings(builder.build());
                            }
                        }
                    });
        } else if (isPermitted == -1) {

        };
    }
}