package com.example.testapp1.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.testapp1.Daos.ScavengerHuntDao;
import com.example.testapp1.Entities.PointOfInterest;
import com.example.testapp1.Entities.ScavengerHunt;

@Database(entities = {ScavengerHunt.class, PointOfInterest.class}, version = 1)
public abstract class ScavengerHuntDatabase extends RoomDatabase {
    abstract public ScavengerHuntDao SchnitzeljagdDao();

    private static volatile ScavengerHuntDatabase INSTANCE;

    public static ScavengerHuntDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ScavengerHuntDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ScavengerHuntDatabase.class, "Schnitzeljagd_Database").build();
                }
            }
        }
        return INSTANCE;
    }

}
