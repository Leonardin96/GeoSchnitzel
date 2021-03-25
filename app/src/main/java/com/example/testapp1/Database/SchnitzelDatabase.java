package com.example.testapp1.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.testapp1.Daos.SchnitzeljagdDao;
import com.example.testapp1.Entities.PointOfInterest;
import com.example.testapp1.Entities.Schnitzeljagd;

@Database(entities = {Schnitzeljagd.class, PointOfInterest.class}, version = 1)
public abstract class SchnitzelDatabase extends RoomDatabase {
    abstract public SchnitzeljagdDao SchnitzeljagdDao();

    private static volatile SchnitzelDatabase INSTANCE;

    public static SchnitzelDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SchnitzelDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SchnitzelDatabase.class, "Schnitzeljagd_Database").build();
                }
            }
        }
        return INSTANCE;
    }

}
