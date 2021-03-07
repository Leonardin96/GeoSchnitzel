package com.example.testapp1.Database;

import androidx.room.RoomDatabase;

import com.example.testapp1.Daos.SchnitzeljagdDao;
import com.example.testapp1.Entities.Schnitzeljagd;

@androidx.room.RoomDatabase(entities = {Schnitzeljagd.class}, version = 1)
abstract class Database extends RoomDatabase {
    abstract public SchnitzeljagdDao SchnitzeljagdDao();
}
