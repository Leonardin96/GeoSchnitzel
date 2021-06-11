package com.example.testapp1;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.testapp1.Helper.LocationHelper;
import com.example.testapp1.Helper.ScavengerHuntWithPoisHelper;
import com.example.testapp1.Helper.ScavengerHuntSingleton;
import com.example.testapp1.Helper.actionFinishedCallback;
import com.example.testapp1.Helper.loadedListCallback;
import com.example.testapp1.Entities.PointOfInterest;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    private ScavengerHuntWithPoisHelper helper;
    private LocationHelper locationHelper;
    private Intent toPOICreationIntent;
    private int displayHeight;

    // UI-Elements
    private Button button_create_poi;
    private Button button_edit_poi;
    private Button button_creation_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_maps);

        getUIElements();
        toggleButtonClearance();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        displayHeight = metrics.heightPixels;

        // Set up Helpers and misc
        helper = new ScavengerHuntWithPoisHelper(this);
        locationHelper = new LocationHelper(this);
        toPOICreationIntent = new Intent(this, PointOfInterestCreationActivity.class);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loadScavengerHuntTest();
    }

    /**
     * Hides the System-UI (Navigation-Bar and Menu-Bar).
     */
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
    }

    /**
     * Listener as to when the app regains the focus.
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    /**
     * Get all neccessary UI-Elements.
     */
    private void getUIElements() {
        button_create_poi = findViewById(R.id.button_maps_create_poi);
        button_edit_poi = findViewById(R.id.button_maps_edit_poi);
        button_creation_done = findViewById(R.id.button_maps_finish_creation);
    }

    /**
     * Check how many POIS already exist to decide which buttons have to be enabled/disabled.
     */
    private void toggleButtonClearance() {
        ScavengerHuntSingleton single = ScavengerHuntSingleton.instance;
        List<PointOfInterest> poiList = single.getPOIList();
        if (poiList != null) {
            if (poiList.size() > 0) {
                button_edit_poi.setEnabled(true);
            }

            if (poiList.size() == 4) {
                button_create_poi.setEnabled(false);
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        locationHelper.getLastLocation(new actionFinishedCallback() {
            @Override
            public void onComplete(Object o) {
                Location loc = (Location) o;
                LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        });

        double calcHeight = displayHeight * 0.8;
        int paddingTop = (int) calcHeight;

        mMap.setPadding(0,paddingTop, 0, 0);

        // TODO: Align Location-Button with other Buttons - 24dp.
        /*int padding_in_dp = 6;  // 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);*/
    };

    public void setText(List<String> list) {
        TextView view = findViewById(R.id.textView_maps_scavengerHuntName);
        view.setText(list.get(0));
    }


    public void loadScavengerHuntTest() {
        helper.loadScavengerHuntName((loadedListCallback<String>) (stringList) -> {
            runOnUiThread(() -> { setText(stringList); });
        }, ScavengerHuntSingleton.instance.getId());
    }

    /**
     * Saves the progress "manually" and exits the ScavengerHunt-creation. Sends the user back to the title-screen.
     */
    public void finishCreation(View view) {

    }

    public void setUpNewPOI() {
        PointOfInterest poi = new PointOfInterest();
        locationHelper.getLastLocation(new actionFinishedCallback() {
            @Override
            public void onComplete(Object o) {
                // TODO: set up the new POI
            }
        });
    }

    public void changeActivity() {
        HashMap<String, Double> intentMap = new HashMap<String, Double>();

        ScavengerHuntSingleton single = ScavengerHuntSingleton.instance;
        List<PointOfInterest> poiList = single.getPOIList();

        Integer fakeMarkerNumber = 0;

        if (poiList != null) {
            fakeMarkerNumber = poiList.size() - 1;
        }

        final int markerNumber = fakeMarkerNumber * 1;

        locationHelper.getLastLocation(new actionFinishedCallback() {
            @Override
            public void onComplete(Object o) {
                Location loc = (Location) o;

                intentMap.put("loc_lat", loc.getLatitude());
                intentMap.put("loc_long", loc.getLongitude());
                intentMap.put("markerNumber", (double) markerNumber);

                startActivity(toPOICreationIntent);
            }
        });


    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}