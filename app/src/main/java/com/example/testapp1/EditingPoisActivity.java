package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.testapp1.Entities.PointOfInterest;
import com.example.testapp1.Callbacks.ItemMoveCallback;
import com.example.testapp1.Helper.PoiHelper;
import com.example.testapp1.Helper.ScavengerHuntSingleton;
import com.example.testapp1.Misc.VerticalSpaceItemDecoration;
import com.example.testapp1.Callbacks.actionFinishedCallback;
import com.example.testapp1.Adapter.poiEditingListAdapter;

import java.util.List;

public class EditingPoisActivity extends AppCompatActivity {

    PoiHelper poiHelper;

    Intent toMapIntent;

    private static final int VERTICAL_ITEM_SPACE = 48;
    RecyclerView recyclerView;
    poiEditingListAdapter mAdapter;
    List<PointOfInterest> pois;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_pois);

        poiHelper = new PoiHelper(this);

        toMapIntent = new Intent(this, MapsActivity.class);
        toMapIntent.putExtra("pressedBtn", "createBtn");

        hideSystemUI();
        recyclerView = findViewById(R.id.recyclerView_editing_pois);
        pois = ScavengerHuntSingleton.getInstance().getPOIList();
        setupRecyclerView();
    }

    /**
     * Sets up the adapter and the recyclerView used in the activity to display the correct dataset.
     */
    private void setupRecyclerView() {

        mAdapter = new poiEditingListAdapter(pois, new poiEditingListAdapter.ClickListener() {
            @Override
            public void onPositionClicked(int position) {

            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        Log.d("mAdapterData before swap", pois.get(0).poiName);

        ItemTouchHelper.Callback callback = new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        recyclerView.setAdapter(mAdapter);
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
     * Applies the changes made by the user to the data in the db and the singleton.
     * @param view
     */
    public void applyChanges(View view) {
        List<PointOfInterest> dataRef = mAdapter.getData();

        for (int iterator = 0; iterator < dataRef.size(); iterator++) {
            dataRef.get(iterator).poiNumber = iterator;
        }


        poiHelper.updatePois(new actionFinishedCallback() {
            @Override
            public void onComplete(Object o) {
                ScavengerHuntSingleton.getInstance().overridePois(dataRef);
                startActivity(toMapIntent);
            }
        }, dataRef);
    }
}