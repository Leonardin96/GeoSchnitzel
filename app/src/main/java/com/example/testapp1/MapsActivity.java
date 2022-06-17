package com.example.testapp1;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.testapp1.Entities.ScavengerHuntWithPois;
import com.example.testapp1.Helper.LocationHelper;
import com.example.testapp1.Helper.ScavengerHuntHelper;
import com.example.testapp1.Helper.ScavengerHuntSingleton;
import com.example.testapp1.Callbacks.actionFinishedCallback;
import com.example.testapp1.Entities.PointOfInterest;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    private ScavengerHuntHelper helper;
    private LocationHelper locationHelper;
    private ScavengerHuntSingleton scavengerHuntSingleton;
    private Intent toPOICreationIntent;
    private Intent toTitleScreenIntent;
    private Intent toEditingPoisIntent;

    // UI-Elements
    private Button button_create_poi;
    private Button button_edit_poi;

    //miscellaneous
    private Boolean playMode;
    private BitmapDescriptor icon;
    private int markerShown;

    public MapsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up Helpers and misc
        helper = new ScavengerHuntHelper(this);
        locationHelper = new LocationHelper(this);
        scavengerHuntSingleton = ScavengerHuntSingleton.getInstance();
        toTitleScreenIntent = new Intent(this, EntryActivity.class);
        toPOICreationIntent = new Intent(this, PointOfInterestCreationActivity.class);
        toEditingPoisIntent = new Intent(this, EditingPoisActivity.class);

        // check which path the user took, display the corresponding layout and load the the corresponding hunt if needed
        checkIntent();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        hideSystemUI();
        if (playMode == false) {
            toggleButtonClearance();
        }
        // icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_foreground);
        markerShown = 0;
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
     * Checks with path the user took and then shows the according UI.
     */
    private void checkIntent() {
        if (getIntent().getStringExtra("pressedBtn").equals("createBtn")) {
            setContentView(R.layout.activity_maps);
            getCreateUIElements();
            playMode = false;
        } else {
            setContentView(R.layout.activity_maps_play);
            getPlayUIElements();
            ScavengerHuntWithPois playableHunt = scavengerHuntSingleton.getHuntWithPois();
            playMode = true;
        }
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
     * Get all necessary UI-Elements for the creation mode.
     */
    private void getCreateUIElements() {
        button_create_poi = findViewById(R.id.button_maps_create_poi);
        button_edit_poi = findViewById(R.id.button_maps_edit_poi);
        Button button_creation_done = findViewById(R.id.button_maps_finish_creation);
    }

    /**
     * Get all necessary UI-Elements for the play mode.
     */
    private void getPlayUIElements() {
        // TODO: get the UI Elements required for playing
    }

    /**
     * Check how many POIS already exist to decide which buttons have to be enabled/disabled.
     */
    private void toggleButtonClearance() {
        List<PointOfInterest> poiList = scavengerHuntSingleton.getPOIList();
        if (poiList != null) {
            if (poiList.size() > 0) {
                button_edit_poi.setEnabled(true);
            }

            if (poiList.size() == 12) {
                button_create_poi.setEnabled(false);
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        locationHelper.checkForLocationPermission(this);

        ArrayList<LatLng> poiCoordinates = new ArrayList<LatLng>();
        List<PointOfInterest> pois = scavengerHuntSingleton.getPOIList();

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
        }, this);
        ;
        if (playMode) {
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {

                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (markerShown < locationResult.getLocations().size()) {
                            double distance = calculateDistanceToNextPoi(location, pois.get(markerShown));

                            if (distance < 15 && markerShown == pois.get(markerShown).poiNumber) {
                                googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(pois.get(markerShown).poiLocationLat, pois.get(markerShown).poiLocationLong))
                                ).setTag(pois.get(markerShown));
                                markerShown++;
                            }
                        }
                    }
                }
            };
            locationHelper.startLocationUpdates(locationCallback, this);
        }

        if (!playMode) {
            for(int iterator = 0; iterator < pois.size(); iterator++) {
                poiCoordinates.add(
                        new LatLng(pois.get(iterator).poiLocationLat, pois.get(iterator).poiLocationLong)
                );
            }

            for(int iterator = 0; iterator < poiCoordinates.size(); iterator++) {
                googleMap.addMarker(new MarkerOptions()
                        .position(poiCoordinates.get(iterator))
                ).setTag(pois.get(iterator));
            }
        }

        mMap.setOnMarkerClickListener(marker -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.maps_popup_layout, null);

            final PopupWindow popupWindow = new PopupWindow(popupView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT,true);
            popupWindow.showAtLocation(findViewById(R.id.textView_maps_scavengerHuntObjective), Gravity.CENTER, 0, 0);
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    popupWindow.dismiss();
                    return true;
                }
            });

            PointOfInterest associatedPoi = (PointOfInterest) marker.getTag();

            TextView popupTitleTextView = popupWindow.getContentView().findViewById(R.id.textView_maps_popup_title);
            popupTitleTextView.setText(associatedPoi.poiName);

            TextView popupRiddleTextView = popupWindow.getContentView().findViewById(R.id.textView_maps_popup_text);
            popupRiddleTextView.setText(associatedPoi.poiRiddle);

            return false;
        });
    };

    /**
     * Calculates the distances between the provided poi and the provided location.
     * @param location {Location}
     * @param poi {PointOfInterest}
     * @return distance between the two points
     */
    public double calculateDistanceToNextPoi(Location location, PointOfInterest poi) {
        double lat1 = location.getLatitude();
        double lat2 = poi.poiLocationLat;
        double long1 = location.getLongitude();
        double long2 = poi.poiLocationLong;

        double lat = (lat1 + lat2) / 2 * 0.01745;
        double dx = 111.3 * Math.cos(lat) * (long1 - long2);
        double dy = 111.3 * (lat1 - lat2);

        return Math.sqrt((dx * dx) + (dy * dy)) * 1000;
    }

    /**
     * Saves the progress "manually" and exits the ScavengerHunt-creation. Sends the user back to the entry-activity.
     * @param view {View}
     */
    public void finishCreation(View view) {
        helper.insertScavengerHunt(new actionFinishedCallback() {
            @Override
            public void onComplete(Object o) {
                startActivity(toTitleScreenIntent);
            }
        }, scavengerHuntSingleton.getHunt());
    }

    public void setUpNewPOI(int poiNumber, String scavengerHuntName, Location loc) {

        PointOfInterest poi = new PointOfInterest();

        poi.poiID = "poi" + poiNumber;
        poi.scavengerHuntName = scavengerHuntName;
        poi.poiLocationLat = loc.getLatitude();
        poi.poiLocationLong = loc.getLongitude();

        scavengerHuntSingleton.addPoiToList(poi);
    }

    /**
     * Starts the activity in which the user can create a POI.
     * @param view
     */
    public void startPoiCreationActivity(View view) {
        List<PointOfInterest> poiList = scavengerHuntSingleton.getPOIList();

        int fakeMarkerNumber = 0;

        if (poiList != null && poiList.size() > 0 ) {
            fakeMarkerNumber = poiList.size();
        }

        final int poiNumber = fakeMarkerNumber;

        locationHelper.getLastLocation(new actionFinishedCallback() {
            @Override
            public void onComplete(Object o) {
                Location loc = (Location) o;

                setUpNewPOI(poiNumber, scavengerHuntSingleton.getId(), loc);

                toPOICreationIntent.putExtra("poiNumber", poiNumber);
                startActivity(toPOICreationIntent);
            }
        }, this);
    }

    /**
     * Starts the activity in which the user can edit the list of POIs.
     * @param view
     */
    public void startPoiEditingActivity(View view) {
        startActivity(toEditingPoisIntent);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}