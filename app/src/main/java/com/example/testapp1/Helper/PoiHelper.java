package com.example.testapp1.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.testapp1.Daos.SchnitzeljagdDao;
import com.example.testapp1.Database.SchnitzelDatabase;
import com.example.testapp1.Entities.PointOfInterest;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PoiLoader {
    private Context appContext;
    private int toastDuration;
    private SchnitzelDatabase schnitzelDB;
    private SchnitzeljagdDao schnitzelDao;
    private Executor executor;

    public PointOfInterest poi1;
    public PointOfInterest poi2;
    public PointOfInterest poi3;
    public PointOfInterest poi4;

    public List<PointOfInterest> allPois;


    public PoiLoader (Context context) {
        appContext = context;
        toastDuration = Toast.LENGTH_SHORT;
        schnitzelDB = SchnitzelDatabase.getInstance(context);
        schnitzelDao = schnitzelDB.SchnitzeljagdDao();
        executor = Executors.newSingleThreadExecutor();
    }
    public void loadAll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                allPois = schnitzelDao.loadPOIs();
                String toastText = "All pois loaded";
                Toast theToast = Toast.makeText(appContext, toastText, toastDuration);
                theToast.show();
            }
        });
    }
}
