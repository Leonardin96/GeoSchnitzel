package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.testapp1.Helper.LocationHelper;

public class EntryActivity extends AppCompatActivity {

    private Intent scavengerHuntListIntent;

    // UI Elements
    private Button button_play;
    private Button button_create;
    private Button button_popup;
    private PopupWindow popupWindow;
    private View popupView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_entry);

        LocationHelper locationHelper = new LocationHelper(this);

        if (!locationHelper.checkForLocationPermission(this)) {
            locationHelper.requestLocationPermission(this);
        };

        scavengerHuntListIntent = new Intent(this, ScavengerHuntsListActivity.class);

        button_play = findViewById(R.id.button_entry_play);
        button_create = findViewById(R.id.button_entry_create);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_layout, null);
        popupWindow = new PopupWindow(popupView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT,false);
        popupWindow.setOutsideTouchable(false);

        startWiggleAnimation();
    }

    /**
     * Hides the Systems-UI not needed for the activity.
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
     * Starts the wiggle-Animation for the buttons.
     */
    public void startWiggleAnimation() {

        Animation wiggle = AnimationUtils.loadAnimation(this, R.anim.anim_button_wiggle);
        wiggle.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button_create.startAnimation(wiggle);
                button_play.startAnimation(wiggle);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        button_create.startAnimation(wiggle);
        button_play.startAnimation(wiggle);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    /**
     * Called when the user presses one of the buttons. Starts new activity based on the button pressed.
     */
    @SuppressLint("NonConstantResourceId")
    public void changeActivity(View view) {
        LocationHelper locationHelper = new LocationHelper(this);

        if (locationHelper.checkForLocationPermission(this)) {
            switch(view.getId()) {
                case R.id.button_entry_create:
                    scavengerHuntListIntent.putExtra("pressedBtn", "create");
                    startActivity(scavengerHuntListIntent);
                    break;
                case R.id.button_entry_play:
                    scavengerHuntListIntent.putExtra("pressedBtn", "play");
                    startActivity(scavengerHuntListIntent);
                    break;
                default:
                    throw new RuntimeException("Unkown button ID!");
            }
        } else {

            Activity activity = this;
            popupWindow.showAtLocation(findViewById(R.id.textView_popup_root), Gravity.CENTER, 0, 0);

            TextView popupTitleTextView = popupWindow.getContentView().findViewById(R.id.textView_popup_title);
            popupTitleTextView.setText(R.string.permission_request_title);

            TextView popupRiddleTextView = popupWindow.getContentView().findViewById(R.id.textView_popup_text);
            popupRiddleTextView.setText(R.string.permission_request_body);

            popupWindow.getContentView().findViewById(R.id.button_popup).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    locationHelper.requestLocationPermission(activity);
                }
            });

        }

    }
}