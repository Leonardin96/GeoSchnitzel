package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp1.Helper.LocationHelper;
import com.example.testapp1.Helper.LocationUpdate;

public class LocationActivity extends AppCompatActivity {
    // UI
    private static int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
    private View decorView;
    // Helper
    private LocationHelper locationHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(uiOptions);
                }
            }
        });

        locationHelper = new LocationHelper(this);
        locationHelper.setupLocationRequest(new LocationUpdate<Location>() {
            @Override
            public void onLocationUpdate(Location location) {
                setTextView(location);
            }
        });
        locationHelper.startLocationUpdates();
    }

    /**
     * Opens the PointOfInterestCreationActivity.
     * @param view
     */
    public void createSchnitzel(View view) {
        Intent intent = new Intent(this, PointOfInterestCreationActivity.class);
        if (locationHelper.currentLocation != null) {
            Location location = locationHelper.currentLocation;
            intent.putExtra("location", location);

            startActivity(intent);
        } else {
            String toastText = "Error - Couldn't stat acitvity due to the 'location' being null";
            Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Opens up the MapsActivity.
     * @param view
     */
    public void showMapActivity(View view) {
        Intent intent = new Intent(this, MapsActivity.class);

        startActivity(intent);
    }

    /**
     * Updates the Textview Elements in the Activity.
     * @param location
     */
    public void setTextView(Location location) {
        TextView geoLongi = findViewById(R.id.geoLongi);
        TextView geoLat = findViewById(R.id.geoLat);

        geoLongi.setText(Double.toString(location.getLongitude()));
        geoLat.setText(Double.toString((location.getLatitude())));
    }

    @Override
    protected void onResume() {
        super .onResume();
        if (locationHelper.requestingLocationUpdates) {
            locationHelper.startLocationUpdates();
        }
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(uiOptions);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super .onPause();
        locationHelper.stopLocationUpdates();
    }

}