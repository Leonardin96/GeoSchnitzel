package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testapp1.Entities.ScavengerHuntWithPois;
import com.example.testapp1.Helper.ScavengerHuntWithPoisHelper;
import com.example.testapp1.Helper.ScavengerHuntSingleton;
import com.example.testapp1.Helper.dataSetCallback;
import com.example.testapp1.Helper.loadedListCallback;

import org.w3c.dom.Text;

import java.util.List;

public class ScavengerHuntsList extends AppCompatActivity {
    EditText editText_hunt_id;
    EditText editText_creatorName;
    Button button_done;
    Intent intent;
    ScavengerHuntWithPoisHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_scavengerhunts_list);
        intent = new Intent(this, MapsActivity.class);
        helper = new ScavengerHuntWithPoisHelper(this);
        setList();
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

    public void setList() {
        helper.loadAllHunts(new loadedListCallback<ScavengerHuntWithPois>() {
            @Override
            public void onComplete(List<ScavengerHuntWithPois> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int x = 0; x < list.size(); x++) {
                            String tagName = "List Item " + x;
                            ScavengerHuntWithPois hunt = list.get(x);
                            String creatorName = hunt.scavengerHunt.creatorName;
                        }
                    }
                });
            }
        });
    };

    /**
    * Changing the layout when the user wants to create a new Schnitzeljagd, instead of choosing from an existing one.
    */
    public void changeLayout(View view) {
        setContentView(R.layout.activity_schnitzeljagd_creation_start);
        layoutChanged();
    }

    /**
    * Getting the layout elements when the correct layout is displayed.
    */
    private void layoutChanged() {
        editText_hunt_id = findViewById(R.id.editText_schnitzeljagdname);
        editText_creatorName = findViewById(R.id.editText_creatorname);
        button_done = findViewById(R.id.button_schnitzeljadgcreation_done);

        toggleButtonClearance();
    }

    /**
     * Checks if both input-Views have content and then enables the button to proceed.
     * TODO: Check DB if hunt with this ID already exists. + onInput(?) Listeners for both input-Views.
     */
    private void toggleButtonClearance() {
        String huntID = editText_hunt_id.getText().toString().trim();
        String creatorName = editText_creatorName.getText().toString().trim();
        TextView errorText = findViewById(R.id.textView_list_huntname_taken_error);

        editText_hunt_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                helper.huntNameTaken(new dataSetCallback() {
                    @Override
                    public void onComplete(Object o) {
                        Boolean isTaken = (boolean) o;
                        Log.i("isTaken", isTaken.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isTaken == true) {
                                    errorText.setVisibility(View.VISIBLE);
                                    if (creatorName != null && creatorName != "") {
                                        button_done.setEnabled(true);
                                    }
                                } else {
                                    errorText.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                }, editable.toString());
            }
        });

        editText_creatorName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String givenCreatorName = editable.toString();
                Log.i("CreatorName:", "text changed:" + editable.toString());

                if ((givenCreatorName != null && givenCreatorName != "") && (errorText.getVisibility() == View.INVISIBLE)) {
                    button_done.setEnabled(true);
                }
            }
        });
    }

    /**
     * Empty all tables to create a clean slate.
     * TODO: Hide this option, so users won't accidently- / over-use it.
     */
    public void deleteAllHunts(View view) {
        helper.emtpyAllTables();
    }

    /**
    * Changing activity upon completed creation process.
    */
    public void setUpDone(View view) {
        helper.createEmptyHunt(new dataSetCallback() {
            @Override
            public void onComplete(Object o) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                    }
                });
            }
        }, editText_hunt_id.getText().toString(), editText_creatorName.getText().toString());
    }
}