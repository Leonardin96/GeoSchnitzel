package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.testapp1.Helper.LocationHelper;

public class EntryActivity extends AppCompatActivity {

    private Intent scavengerHuntListIntent;

    // UI Elements
    private Button button_play;
    private Button button_create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_entry);
        findUIElements();
        startWiggleAnimation();

        LocationHelper locationHelper = new LocationHelper(this);
        locationHelper.checkForLocationPermission(this);

        scavengerHuntListIntent = new Intent(this, ScavengerHuntsList.class);

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
     * Collectively searches for all UI-Elements necessary.
     */
    private void findUIElements() {
        button_play = findViewById(R.id.button_entry_play);
        button_create = findViewById(R.id.button_entry_create);
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
        switch(view.getId()) {
            case R.id.button_entry_create:
                scavengerHuntListIntent.putExtra("pressedBtn", "createBtn");
                startActivity(scavengerHuntListIntent);
                break;
            case R.id.button_entry_play:
                scavengerHuntListIntent.putExtra("pressedBtn", "playBtn");
                startActivity(scavengerHuntListIntent);
                break;
            default:
                throw new RuntimeException("Unkown button ID!");
        }
    }
}