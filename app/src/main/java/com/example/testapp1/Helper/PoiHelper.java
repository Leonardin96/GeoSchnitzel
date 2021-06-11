package com.example.testapp1.Helper;

import android.content.Context;

import com.example.testapp1.Daos.ScavengerHuntDao;
import com.example.testapp1.Database.ScavengerHuntDatabase;
import com.example.testapp1.Entities.PointOfInterest;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class PoiHelper {
    private ScavengerHuntDatabase schnitzelDB;
    private ScavengerHuntDao schnitzelDao;
    private Executor executor;


    public PoiHelper(Context context) {
        schnitzelDB = ScavengerHuntDatabase.getInstance(context);
        schnitzelDao = schnitzelDB.SchnitzeljagdDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public void loadPOIs(final loadedListCallback<PointOfInterest> callback, final String schnitzeljagdName) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<PointOfInterest> schnitzeljagdPOIs = schnitzelDao.loadPOIs(schnitzeljagdName);
                callback.onComplete(schnitzeljagdPOIs);
            }
        });
    }

    public void savePoi(final actionFinishedCallback callback, final PointOfInterest poi) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                schnitzelDao.insertPOI(poi);
                callback.onComplete(null);
            }
        });
    }

    public void saveMultiplePOIs(final actionFinishedCallback callback, PointOfInterest... pois) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                schnitzelDao.saveMultiplePOIs(pois);
                callback.onComplete(null);
            }
        });
    }
}
