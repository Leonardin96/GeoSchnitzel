package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.testapp1.Entities.PointOfInterest;
import com.example.testapp1.Helper.PoiHelper;
import com.example.testapp1.Helper.ScavengerHuntSingleton;
import com.example.testapp1.Helper.actionFinishedCallback;
import com.example.testapp1.Helper.loadedListCallback;

import java.util.List;

import javax.security.auth.callback.Callback;

public class PointOfInterestCreationActivity extends AppCompatActivity implements Callback {
    // Helper-classes
    private PoiHelper poiHelper;

    // UI-Elements
    private String poiID;
    private EditText question;
    private EditText answer1;
    private EditText answer2;
    private EditText answer3;
    private EditText answer4;
    private ProgressBar pBar;

    // miscellaneous
    private Location givenLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_creation_entry);

        poiHelper = new PoiHelper(this);

        if (getIntent().getExtras().getParcelable("location") != null) {
            givenLocation = getIntent().getExtras().getParcelable("location");
        }
    }

    /**
     * Goes back to the entry layout.
     * @param view
     */
    public void goBack(View view) {
        setContentView(R.layout.activity_poi_creation_entry);
    }

    /**
     * Coming from the entry layout, depending of the id of the button, the layout changes.
     * @param view
     */
    public void changeLayout(View view) {
        int viewId = view.getId();

        if (viewId == R.id.button_questionCreation) {
            setContentView(R.layout.activity_poi_question_creation);
            getUIElements();
        } else {
            setContentView(R.layout.activity_poi_hint_creation);
        }
    }

    /**
     * Collects the needed Layout-elements for the activity_poi_question_creation-layout.
     */
    public void getUIElements() {
        question = findViewById(R.id.editView_question_poi_creation);
        answer1 = findViewById(R.id.editView_answer1_poi_creation);
        answer2 = findViewById(R.id.editView_answer2_poi_creation);
        answer3 = findViewById(R.id.editView_answer3_poi_creation);
        answer4 = findViewById(R.id.editView_answer4_poi_creation);
    }

    /**
     * Method to clear the EditText Elements once.
     */
    public void clearEditText(View view) {
        question.setText(" ");
        answer1.setText(" ");
        answer2.setText(" ");
        answer3.setText(" ");
        answer4.setText(" ");
    }

    /**
     * Saving-Method for POI.
     * TESTING
     * @param view
     */
    public void savePoi(View view) {
        final PointOfInterest poi1 = new PointOfInterest();
        poi1.poiID = "firstTryPoi";
        poi1.poiLocationLat = givenLocation.getLatitude();
        poi1.poiLocationLong = givenLocation.getLongitude();
        poi1.poiQuestion = question.getText().toString();
        poi1.poiFirstAnswer = answer1.getText().toString();
        poi1.poiSecondAnswer = answer2.getText().toString();
        poi1.poiThirdAnswer = answer3.getText().toString();
        poi1.poiFourthAnswer = answer4.getText().toString();

        clearEditText(null);

        poiHelper.savePoi(new actionFinishedCallback() {
            @Override
            public void onComplete(Object o) {
                // TODO: Do I need the callback here?
            }
        }, poi1);
    }
    /**
     * Method to get called by the callback of the loading process.
     * TESTING
     * @param allPoi
     */
    public void loadedPoi(List<PointOfInterest> allPoi) {
        PointOfInterest firstPoi = allPoi.get(0);

        if ( pBar != null) {
            pBar.setVisibility(View.GONE);
        }

        question.setText(firstPoi.poiQuestion != null ? firstPoi.poiQuestion : " ");
        answer1.setText(firstPoi.poiFirstAnswer != null ? firstPoi.poiFirstAnswer : " ");
        answer2.setText(firstPoi.poiSecondAnswer != null ? firstPoi.poiSecondAnswer : " ");
        answer3.setText(firstPoi.poiThirdAnswer != null ? firstPoi.poiThirdAnswer : " ");
        answer4.setText(firstPoi.poiFourthAnswer != null ? firstPoi.poiFourthAnswer : " ");
    }

    /**
     * Loading-Method for POI.
     * TESTING
     * @param view
     * @throws InterruptedException
     */
    public void loadPois(View view) throws InterruptedException {

        // to show the user that there is something happening IC loading all Poi
        if ( pBar != null ) {
            pBar.setVisibility(View.VISIBLE);
        }

        poiHelper.loadPOIs(new loadedListCallback<PointOfInterest>() {
            @Override
            public void onComplete(final List<PointOfInterest> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadedPoi(list);
                    }
                });
            }
        }, ScavengerHuntSingleton.instance.getId());
    }

}