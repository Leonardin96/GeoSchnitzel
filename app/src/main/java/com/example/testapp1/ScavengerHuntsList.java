package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.testapp1.Helper.VerticalSpaceItemDecoration;
import com.example.testapp1.Helper.dataSetCallback;
import com.example.testapp1.Helper.loadedListCallback;
import com.example.testapp1.Helper.scavengerhuntListAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ScavengerHuntsList extends AppCompatActivity implements scavengerhuntListAdapter.OnItemListener {
    private static final int VERTICAL_ITEM_SPACE = 48;
    EditText editText_hunt_id;
    EditText editText_creatorName;
    Button button_done;
    Intent intent;
    ScavengerHuntWithPoisHelper helper;
    RecyclerView recyclerView;

    List<ScavengerHuntWithPois> hunts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(this, MapsActivity.class);
        helper = new ScavengerHuntWithPoisHelper(this);

        hideSystemUI();
        if (getIntent().getStringExtra("pressedBtn").equals("playBtn")) {
            setContentView(R.layout.activity_scavengerhunts_list);
            recyclerView = findViewById(R.id.recylerView_scavengerhuntlist);
            createList();
            intent.putExtra("pressedBtn", "playBtn");
        } else {
            setContentView(R.layout.activity_schnitzeljagd_creation_start);
            getScavHuntCreationElements();
            intent.putExtra("pressedBtn", "createBtn");
        }
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

    public void createList() {
        helper.loadAllHunts(new loadedListCallback<ScavengerHuntWithPois>() {
            @Override
            public void onComplete(List<ScavengerHuntWithPois> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hunts = list;
                        // TODO: set empty adapter on main, then fill it as soon as the data is there
                        setAdapter(list);
                    }
                });
            }
        });
    };

    /**
     * Set up the adapter for the recylerView to properly display all the scavengerhunts in the list.
     * @param huntList
     */
    private void setAdapter(List<ScavengerHuntWithPois> huntList) {
        scavengerhuntListAdapter adapter = new scavengerhuntListAdapter(huntList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        recyclerView.setAdapter(adapter);
    }

    /**
    * Getting the layout elements when the correct layout is displayed.
    */
    private void getScavHuntCreationElements() {
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
        // Reset the Singleton to get a fresh instance
        ScavengerHuntSingleton.reset();

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

    @Override
    public void onItemClicked(int position) {
        // Reset the singleton to get a fresh instance
        ScavengerHuntSingleton.reset();

        ScavengerHuntWithPois clickedHunt = hunts.get(position);
        ScavengerHuntSingleton.getInstance().setHunt(clickedHunt);
        ScavengerHuntSingleton.getInstance().setCreator(clickedHunt.scavengerHunt.creatorName);
        ScavengerHuntSingleton.getInstance().setId(clickedHunt.scavengerHunt.scavengerHuntName);

        startActivity(intent);
    }
}