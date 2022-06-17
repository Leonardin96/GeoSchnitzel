package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

import com.example.testapp1.Callbacks.actionFinishedCallback;
import com.example.testapp1.Entities.ScavengerHunt;
import com.example.testapp1.Entities.ScavengerHuntWithPois;
import com.example.testapp1.Helper.ScavengerHuntHelper;
import com.example.testapp1.Helper.ScavengerHuntSingleton;
import com.example.testapp1.Misc.VerticalSpaceItemDecoration;
import com.example.testapp1.Callbacks.loadedListCallback;
import com.example.testapp1.Adapter.scavengerhuntListAdapter;

import java.io.File;
import java.util.List;

public class ScavengerHuntsList extends AppCompatActivity implements scavengerhuntListAdapter.OnItemListener {
    private static final int VERTICAL_ITEM_SPACE = 48;
    EditText editText_hunt_id;
    EditText editText_creatorName;
    Button button_done;
    Intent intent;
    ScavengerHuntHelper helper;
    RecyclerView recyclerView;

    List<ScavengerHuntWithPois> hunts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(this, MapsActivity.class);
        helper = new ScavengerHuntHelper(this);

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    /**
     * Creates the list of scavengerhunts for the recyclerView.
     */
    public void createList() {
        helper.getAllHunts(new loadedListCallback<ScavengerHuntWithPois>() {
            @Override
            public void onComplete(List<ScavengerHuntWithPois> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hunts = list;
                        setAdapter(list);
                    }
                });
            }
        });
    };

    /**
     * Sets up the adapter for the recylerView to properly display all the scavengerhunts in the list.
     * @param huntList {List}
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
    * Gets the layout elements when the correct layout is displayed.
    */
    private void getScavHuntCreationElements() {
        editText_hunt_id = findViewById(R.id.editText_schnitzeljagdname);
        editText_creatorName = findViewById(R.id.editText_creatorname);
        button_done = findViewById(R.id.button_schnitzeljadgcreation_done);

        toggleButtonClearance();
    }

    /**
     * Checks if both input-Views have content and then enables the button to proceed.
     * Also checks if the name of the scavengerhunt is already taken since it has to be unique.
     * TODO: disable the checker when the input doesnt match with the the id anymore
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
                helper.checkForName(new actionFinishedCallback() {
                    @Override
                    public void onComplete(Object o) {
                        Boolean isTaken = (boolean) o;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isTaken) {
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
     */
    public void deleteAllHunts(View view) {
        helper.emptyAllTables();
    }

    /**
    * Changing activity upon completed creation process.
     * @param view {View}
    */
    public void setUpDone(View view) {
        // Reset the Singleton to get a fresh instance
        ScavengerHuntSingleton.reset();
        ScavengerHuntSingleton.instantiate();
        deleteCache(this);

        helper.createEmptyHunt(new actionFinishedCallback() {
            @Override
            public void onComplete(Object o) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ScavengerHuntSingleton.instance.setHunt((ScavengerHunt) o);
                        startActivity(intent);
                    }
                });
            }
        }, editText_hunt_id.getText().toString(), editText_creatorName.getText().toString());
    }

    /**
     * Deletes the cache of the application
     * Used to counteract the error in correlation with the bug mentioned in the following issue on issuetracker.google.com:
     * https://issuetracker.google.com/issues/219879780?pli=1
     * @param context {Context}
     */
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    /**
     * Deletes the File provided.
     * Helper function for the deleteCache function
     * @param dir {File}
     * @return boolean
     */
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onItemClicked(int position) {
        // Reset the singleton to get a fresh instance
        ScavengerHuntSingleton.reset();

        ScavengerHuntSingleton.getInstance().setHuntWithPois(hunts.get(position));

        deleteCache(this);
        startActivity(intent);
    }
}