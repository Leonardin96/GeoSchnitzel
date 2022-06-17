package com.example.testapp1.Helper;

import android.content.Context;

import com.example.testapp1.Callbacks.actionFinishedCallback;
import com.example.testapp1.Callbacks.loadedListCallback;
import com.example.testapp1.Daos.ScavengerHuntDao;
import com.example.testapp1.Database.ScavengerHuntDatabase;
import com.example.testapp1.Entities.ScavengerHunt;
import com.example.testapp1.Entities.ScavengerHuntWithPois;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ScavengerHuntHelper {
    private final ScavengerHuntDatabase scavengerHuntDB;
    private final ScavengerHuntDao scavengerHuntDao;
    private final Executor executor;

    public ScavengerHuntHelper(Context context) {
        scavengerHuntDB = ScavengerHuntDatabase.getInstance(context);
        scavengerHuntDao = scavengerHuntDB.SchnitzeljagdDao();
        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Creates an empty hunt and save it in the DB.
     * @param callback {actionFinishedCallback}
     * @param huntName {String}
     * @param creatorName {String}
     */
    public void createEmptyHunt(final actionFinishedCallback callback, String huntName, String creatorName) {
        ScavengerHunt hunt = new ScavengerHunt();

        hunt.scavengerHuntName = huntName;
        hunt.creatorName = creatorName;

        executor.execute(new Runnable() {
            @Override
            public void run() {
                scavengerHuntDao.insertScavengerHunt(hunt);
                callback.onComplete(hunt);
            }
        });

    }

    /**
     * Loads all hunts including the pois.
     * @param callback {loadedListCallback}
     */
    public void getAllHunts(final loadedListCallback<ScavengerHuntWithPois> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<ScavengerHuntWithPois> list = scavengerHuntDao.loadAllHunts();
                callback.onComplete(list);
            }
        });
    };

    /**
     * Saves the provided hunt in the DB and executes the provided callback.
     * @param callback {actionFinishedCallback}
     * @param hunt {ScavengerHunt}
     */
    public void insertScavengerHunt(actionFinishedCallback callback, ScavengerHunt hunt) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                scavengerHuntDao.insertScavengerHunt(hunt);
                callback.onComplete(null);
            }
        });
    }

    /**
     * Clears all tables of the DB.
     */
    public void emptyAllTables() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                scavengerHuntDB.clearAllTables();
            }
        });
    }

    /**
     * Checks if there already is a hunt with the given name.
     * @param callback {actionFinishedCallback}
     * @param huntname {String}
     */
    public void checkForName(actionFinishedCallback callback, String huntname) {
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
}
