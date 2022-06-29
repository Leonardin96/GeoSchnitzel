package com.example.testapp1.Helper;

import android.content.Context;

import com.example.testapp1.Callbacks.actionFinishedCallback;
import com.example.testapp1.Daos.ScavengerHuntDao;
import com.example.testapp1.Database.ScavengerHuntDatabase;
import com.example.testapp1.Entities.PointOfInterest;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class PoiHelper {
    private final ScavengerHuntDao schnitzelDao;
    private final Executor executor;


    public PoiHelper(Context context) {
        ScavengerHuntDatabase schnitzelDB = ScavengerHuntDatabase.getInstance(context);
        schnitzelDao = schnitzelDB.SchnitzeljagdDao();
        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Saves a POI in the DB and calls the provided callback function.
     * @param callback {actionFinishedCallback}
     * @param poi {PointOfInterest}
     */
    public void insertPoi(final actionFinishedCallback callback, final PointOfInterest poi) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                schnitzelDao.insertPOI(poi);
                callback.onComplete(null);
            }
        });
    }

    /**
     * Deletes a specific POI
     * @param callback {actionFinishedCallback}
     * @param poi {PointOfInterest}
     */
    public void deletePoi(final actionFinishedCallback callback, final PointOfInterest poi) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                schnitzelDao.deletePoi(poi);
                callback.onComplete(null);
            }
        });
    }

    /**
     * Updates one or more POIs based on the list provided
     * @param callback {actionFinishedCallback}
     * @param pois {List}
     */
    public void updatePois(final actionFinishedCallback callback, List<PointOfInterest> pois) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                schnitzelDao.saveMultiplePOIs(pois);
                callback.onComplete(null);
            }
        });
    }
}
