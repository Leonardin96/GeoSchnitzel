package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.testapp1.Entities.PointOfInterest;
import com.example.testapp1.Helper.PoiHelper;
import com.example.testapp1.Helper.ScavengerHuntSingleton;
import com.example.testapp1.Callbacks.actionFinishedCallback;

import javax.security.auth.callback.Callback;

public class PointOfInterestCreationActivity extends AppCompatActivity implements Callback {
    // Helper-classes
    private PoiHelper poiHelper;
    private ScavengerHuntSingleton singleton;

    // UI-Elements for the riddle-creation-layout
    private EditText riddle;
    private EditText name;
    private Button btnCancel;
    private Button btnDone;

    // miscellaneous
    private Location givenLocation;
    private Integer poiNumber;
    private PointOfInterest poi;
    private Intent intent;
    private Boolean fromMapsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_creation);

        poiNumber = getIntent().getExtras().getInt("poiNumber");
        poiHelper = new PoiHelper(this);
        singleton = ScavengerHuntSingleton.getInstance();
        poi = singleton.getPoiFromList(poiNumber);

        fromMapsActivity = getIntent().getExtras().getBoolean("fromMapsActivity");

        hideSystemUI();
        getUIElements();

        if (fromMapsActivity) {
            intent = new Intent(this, MapsActivity.class);
            intent.putExtra("pressedBtn", "create");
        } else {
            intent = new Intent(this, EditingPoisActivity.class);
            setUpInformation();
        }
    }

    /**
     * Hide the Systems-UI not needed for the activity.
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
     * Collects the needed Layout-elements from the activity_poi_ridddle_creation-layout.
     */
    public void getUIElements() {
        riddle = findViewById(R.id.editTextTextMultiLine_poicreation);
        name = findViewById(R.id.editTextTextMultiLine_poicreation2);
        btnCancel = findViewById(R.id.button_poicreation_cancel);
        btnDone = findViewById(R.id.button_poicreation_done);
    }

    /**
     * Fills the layout with the information from the POI.
     */
    private void setUpInformation() {
        riddle.setText(singleton.getPoiFromList(poiNumber).poiRiddle);
        name.setText(singleton.getPoiFromList(poiNumber).poiName);
    }

    /**
     * Override POI in the singleton, save it in the DB and go back to MapsActivity.
     * @param view
     */
    public void insertPoi(View view) {
        poi.poiRiddle = riddle.getText().toString();
        poi.poiName =  name.getText().toString();
        poi.poiNumber = poiNumber;

        poiHelper.insertPoi(new actionFinishedCallback() {
            @Override
            public void onComplete(Object o) {
                singleton.overridePoi(poi, poiNumber);
                intent.putExtra("poiNumber", poiNumber);
                startActivity(intent);
            }
        }, poi);
    }

    /**
     * Cancel creation and go back.
     * @param view
     */
    public void goBack(View view) {

        startActivity(intent);
    }
}