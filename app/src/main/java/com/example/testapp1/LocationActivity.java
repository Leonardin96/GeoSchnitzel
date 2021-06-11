package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp1.Entities.ScavengerHuntWithPois;
import com.example.testapp1.Helper.LocationHelper;
import com.example.testapp1.Helper.LocationUpdate;
import com.example.testapp1.Helper.ScavengerHuntSingleton;
import com.example.testapp1.Helper.ScavengerHuntWithPoisHelper;
import com.example.testapp1.Helper.loadedListCallback;

import java.util.List;

public class LocationActivity extends AppCompatActivity {
    // UI
    private static int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
    private View decorView;
    // helper
    private LocationHelper locationHelper;
    private ScavengerHuntWithPoisHelper schnitzelHelper;
    // miscellaneous
    private String huntID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        /*decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(uiOptions);
                }
            }
        });*/

        huntID = ScavengerHuntSingleton.instance.getId();
        locationHelper = new LocationHelper(this);
        locationHelper.setupLocationRequest(new LocationUpdate<Location>() {
            @Override
            public void onLocationUpdate(Location location) {
                setTextView(location);
            }
        });
        locationHelper.startLocationUpdates();

        schnitzelHelper = new ScavengerHuntWithPoisHelper(this);

        // Loading all the current information about the hunt and using it to fill the singleton
        // TODO: needs to be surrounded by a mechanism to check if the information even needs to be loaded (information has never been loaded / - needs to be updated)
        try {
            getSchnitzeljagd();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
     * Updates the TextView-Elements in the Activity.
     * @param location
     */
    public void setTextView(Location location) {
        TextView geoLongi = findViewById(R.id.geoLongi);
        TextView geoLat = findViewById(R.id.geoLat);
        TextView schnitzelId = findViewById(R.id.textView_schnitzelId);

        geoLongi.setText(Double.toString(location.getLongitude()));
        geoLat.setText(Double.toString((location.getLatitude())));
        schnitzelId.setText(huntID);
    }

    /**
     * Method to fill the Singleton with new informations.
     * @param completeHunt
     */
    public void setSingleton(List<ScavengerHuntWithPois> completeHunt) {

    }

    /**
     * Loading all the information neccessary for displaying the Schnitzeljagd and its POIs
     */
    public void getSchnitzeljagd() throws InterruptedException {
        schnitzelHelper.loadCompleteHunt((loadedListCallback<ScavengerHuntWithPois>) (list) -> {
            runOnUiThread(() -> { setSingleton(list); });
        }, huntID);
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