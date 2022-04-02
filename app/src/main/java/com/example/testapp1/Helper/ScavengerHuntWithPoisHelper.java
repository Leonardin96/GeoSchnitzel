package com.example.testapp1.Helper;

import android.content.Context;
import android.graphics.Point;

import com.example.testapp1.Daos.ScavengerHuntDao;
import com.example.testapp1.Database.ScavengerHuntDatabase;
import com.example.testapp1.Entities.PointOfInterest;
import com.example.testapp1.Entities.ScavengerHunt;
import com.example.testapp1.Entities.ScavengerHuntWithPois;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ScavengerHuntWithPoisHelper {
    private ScavengerHuntDatabase scavengerHuntDB;
    private ScavengerHuntDao scavengerHuntDao;
    private Executor executor;

    public ScavengerHuntWithPoisHelper(Context context) {
        scavengerHuntDB = ScavengerHuntDatabase.getInstance(context);
        scavengerHuntDao = scavengerHuntDB.SchnitzeljagdDao();
        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Method to initially create a scavengerHunt.
     */
    public void createEmptyHunt(final dataSetCallback callback,String huntName, String creatorName) {
        ScavengerHunt hunt = new ScavengerHunt();

        hunt.scavengerHuntName = huntName;
        hunt.creatorName = creatorName;

        // Set data in the Singleton.
        ScavengerHuntSingleton.instantiate();
        ScavengerHuntSingleton.instance.setId(huntName);
        ScavengerHuntSingleton.instance.setCreator(creatorName);

        finishEmptyCreationCallback(callback, hunt);

    }

    /**
     * Callback for the createEmptyHunt-method to finish it up, without interrupting DB inserts.
     */
    private void finishEmptyCreationCallback(final dataSetCallback callback, ScavengerHunt hunt) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                scavengerHuntDao.insertScavengerHunt(hunt);
                ScavengerHuntSingleton.instance.setHunt(hunt);
                callback.onComplete(null);
            }
        });
    }

    /**
     * Completly nukes the DB (clears all tables).
     */
    public void emtpyAllTables() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                scavengerHuntDB.clearAllTables();
            }
        });
    }

    public void insertScavengerHunt(dataSetCallback callback, ScavengerHunt hunt) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                scavengerHuntDao.insertScavengerHunt(hunt);
                callback.onComplete(null);
            }
        });
    }

    /**
     * Checks if there already is a hunt with the given name.
     */
    public void huntNameTaken(dataSetCallback callback, String huntname) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ScavengerHunt hunt = scavengerHuntDao.loadSpecificHunt(huntname);
                if (hunt != null) {
                    callback.onComplete(true);
                }
            }
        });
    }

    /**
     *  Loads the complete hunt (hunt+pois) with the corresponding name.
     */
    public void loadCompleteHunt(final loadedListCallback<ScavengerHuntWithPois> callback, final String scavengerHuntName) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<ScavengerHuntWithPois> completeHunt = scavengerHuntDao.loadScavengerHuntWithPOIs(scavengerHuntName);
                callback.onComplete(completeHunt);
            }
        });

    }

    /**
     * Loads all hunts inclusive of the pois.
     */
    public void loadAllHunts(final loadedListCallback<ScavengerHuntWithPois> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<ScavengerHuntWithPois> list = scavengerHuntDao.loadAllHunts();
                callback.onComplete(list);
            }
        });
    };


    /**
     * Loads the name of a hunt specific to the name given.
     * TESTING
     */
    public void loadScavengerHuntName(final loadedListCallback<String> callback, String scavengerHuntName) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ScavengerHunt hunt = scavengerHuntDao.loadSpecificHunt(scavengerHuntName);
                List<String> nameList = new ArrayList<String>();
                nameList.add(hunt.scavengerHuntName);
                callback.onComplete(nameList);
            }
        });
    }
}
