package com.example.testapp1.Helper;

import android.content.Context;

import com.example.testapp1.Daos.SchnitzeljagdDao;
import com.example.testapp1.Database.SchnitzelDatabase;
import com.example.testapp1.Entities.PointOfInterest;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class PoiHelper {
    private SchnitzelDatabase schnitzelDB;
    private SchnitzeljagdDao schnitzelDao;
    private Executor executor;


    public PoiHelper(Context context) {
        schnitzelDB = SchnitzelDatabase.getInstance(context);
        schnitzelDao = schnitzelDB.SchnitzeljagdDao();
        executor = Executors.newSingleThreadExecutor();
    }
    public void loadAll(final loadedListCallback<PointOfInterest> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<PointOfInterest> allPois = schnitzelDao.loadPOIs();
                callback.onComplete(allPois);
            }
        });
    }

    public void savePoi(final PointOfInterest poi) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                schnitzelDao.insertPOI(poi);
            }
        });
    }
}
