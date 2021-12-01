package com.example.testapp1;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.testapp1.Entities.ScavengerHunt;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    private ScavengerHuntWithPoisHelper helper;
    private LocationHelper locationHelper;
    private ScavengerHuntSingleton scavengerHuntSingleton;
    private Intent toPOICreationIntent;
    private int displayHeight;

    // UI-Elements
    private Button button_create_poi;
    private Button button_edit_poi;
    private Button button_creation_done;

    //miscellaneous
    private Boolean playMode;
    private BitmapDescriptor icon;
    private Bitmap ic_marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check which path the user took and display the corresponding layout
        checkIntent();

        // Set up Helpers and misc
        helper = new ScavengerHuntWithPoisHelper(this);
        locationHelper = new LocationHelper(this);
        scavengerHuntSingleton = ScavengerHuntSingleton.getInstance();
        toPOICreationIntent = new Intent(this, PointOfInterestCreationActivity.class);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        displayHeight = metrics.heightPixels;

        hideSystemUI();
        toggleButtonClearance();
        loadScavengerHuntTest();
        icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_foreground);
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
        String path = getIntent().getStringExtra("pressedBtn");

        if (path == "createBtn") {
            setContentView(R.layout.activity_maps);
            getCreateUIElements();
            playMode = false;
        } else {
            setContentView(R.layout.activity_maps_play);
            getPlayUIElements();
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
        button_creation_done = findViewById(R.id.button_maps_finish_creation);
    }

    /**
     * Get all necessary UI-Elements for the play mode.
     */
    private void getPlayUIElements() {

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

            if (poiList.size() == 12) {
                button_create_poi.setEnabled(false);
            }
        }
    }

    private void setPOIMarkers() {
        List<PointOfInterest> currentPois = scavengerHuntSingleton.getPOIList();
        List<LatLng> POIcoordinates;

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
        googleMap.clear();

        double calcHeight = displayHeight * 0.8;
        int paddingTop = (int) calcHeight;
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
        });

        for(int iterator = 0; iterator < pois.size(); iterator++) {
            poiCoordinates.add(
                    new LatLng(pois.get(iterator).poiLocationLat, pois.get(iterator).poiLocationLong)
            );
        }

        for(int iterator = 0; iterator < poiCoordinates.size(); iterator++) {
            googleMap.addMarker(new MarkerOptions()
                    .position(poiCoordinates.get(iterator))
                    .icon(icon)
            ).setTag(pois.get(iterator));
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                // TODO: Set up the PopUpWindow for the clicked marker with the associated text and title.
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.maps_popup_layout, null);

                final PopupWindow popupWindow = new PopupWindow(popupView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT,true);
                popupWindow.showAtLocation(findViewById(R.id.textView_maps_scavengerHuntName), Gravity.CENTER, 0, 0);
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
                 // TODO: Get the correct POI
                PointOfInterest associatedPoi = (PointOfInterest) marker.getTag();

                TextView popupTitleTextView = popupWindow.getContentView().findViewById(R.id.textView_maps_popup_title);
                popupTitleTextView.setText(associatedPoi.poiName);

                TextView popupRiddleTextView = popupWindow.getContentView().findViewById(R.id.textView_maps_popup_text);
                popupRiddleTextView.setText(associatedPoi.poiRiddle);

                return false;
            }
        });
        // TODO: Align Location-Button with other Buttons - 24dp.
        /*int padding_in_dp = 6;  // 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);*/
    };

    public void setText(String huntName) {
        TextView view = findViewById(R.id.textView_maps_scavengerHuntName);
        view.setText(huntName);
    }


    public void loadScavengerHuntTest() {
        setText(scavengerHuntSingleton.getId());
    }

    /**
     * Saves the progress "manually" and exits the ScavengerHunt-creation. Sends the user back to the title-screen.
     */
    public void finishCreation(View view) {

    }

    public void setUpNewPOI(int poiNumber, String scavengerHuntName, Location loc) {

        PointOfInterest poi = new PointOfInterest();

        poi.poiID = "poi" + poiNumber;
        poi.scavengerHuntName = scavengerHuntName;
        poi.poiLocationLat = loc.getLatitude();
        poi.poiLocationLong = loc.getLongitude();

        scavengerHuntSingleton.addPoiToList(poi);
    }

    public void startPoiCreationActivity(View view) {
        String viewId = view.getResources().getResourceEntryName(view.getId());

        switch (viewId) {
            case "button_maps_create_poi":
                // TODO: Do all of the below;
                List<PointOfInterest> poiList = scavengerHuntSingleton.getPOIList();

                Integer fakeMarkerNumber = 0;

                if (poiList != null && poiList.size() > 0 ) {
                    fakeMarkerNumber = poiList.size();
                }

                final int poiNumber = fakeMarkerNumber * 1;

                locationHelper.getLastLocation(new actionFinishedCallback() {
                    @Override
                    public void onComplete(Object o) {
                        Location loc = (Location) o;

                        setUpNewPOI(poiNumber, scavengerHuntSingleton.getId(), loc);

                        toPOICreationIntent.putExtra("poiNumber", poiNumber);
                        startActivity(toPOICreationIntent);
                    }
                });
                break;
            case "button_maps_edit_poi":
                // TODO: Do the stuff for the editBtn;
                break;
            default:

        }


    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}