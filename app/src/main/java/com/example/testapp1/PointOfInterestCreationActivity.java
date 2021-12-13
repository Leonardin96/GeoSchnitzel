package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.testapp1.Entities.PointOfInterest;
import com.example.testapp1.Helper.PoiHelper;
import com.example.testapp1.Helper.ScavengerHuntSingleton;
import com.example.testapp1.Helper.actionFinishedCallback;
import com.example.testapp1.Helper.loadedListCallback;

import java.util.List;
import java.util.Map;

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
    private Intent toMapIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_riddle_creation);

        poiNumber = getIntent().getExtras().getInt("poiNumber");
        poiHelper = new PoiHelper(this);
        singleton = ScavengerHuntSingleton.getInstance();
        poi = singleton.getPoiFromList(poiNumber);

        toMapIntent = new Intent(this, MapsActivity.class);
        toMapIntent.putExtra("pressedBtn", "createBtn");


        hideSystemUI();
        getRiddleUIElements();
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
    public void getRiddleUIElements() {
        riddle = findViewById(R.id.editTextTextMultiLine_poicreation);
        name = findViewById(R.id.editTextTextMultiLine_poicreation2);
        btnCancel = findViewById(R.id.button_poicreation_cancel);
        btnDone = findViewById(R.id.button_poicreation_done);
    }

    /**
     * Override POI in the singleton, save it in the DB and go back to MapsActivity.
     * @param view
     */
    public void savePoi(View view) {
        poi.poiRiddle = riddle.getText().toString();
        poi.poiName =  name.getText().toString();

        singleton.overridePoi(poi, poiNumber);

        poiHelper.savePoi(new actionFinishedCallback() {
            @Override
            public void onComplete(Object o) {
                toMapIntent.putExtra("poiNumber", poiNumber);
                startActivity(toMapIntent);
            }
        }, poi);
    }

    /**
     * Cancel creation and go back.
     * @param view
     */
    public void goBack(View view) {
        startActivity(toMapIntent);
    }
}