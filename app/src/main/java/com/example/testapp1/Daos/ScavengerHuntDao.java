package com.example.testapp1.Daos;

import android.graphics.Point;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.testapp1.Entities.PointOfInterest;
import com.example.testapp1.Entities.ScavengerHunt;
import com.example.testapp1.Entities.ScavengerHuntWithPois;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface ScavengerHuntDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertScavengerHunt(ScavengerHunt scavengerHunt);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPOI(PointOfInterest poi);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveMultiplePOIs(List<PointOfInterest> pois);

    @Delete
    public void deletePoi(PointOfInterest poi);

    @Delete
    public void deleteHunt(ScavengerHunt hunt);

    @Query("DELETE FROM scavengerhunt")
    public void deleteAllHunts();

    @Query("SELECT * FROM PointOfInterest WHERE scavengerHuntName LIKE :scavengerHuntName")
    public List<PointOfInterest> loadPOIs(String scavengerHuntName);

    @Query("SELECT * FROM ScavengerHunt WHERE scavengerHuntName LIKE :scavengerHuntName")
    public ScavengerHunt loadSpecificHunt(String scavengerHuntName);

    @Transaction
    @Query("SELECT * FROM ScavengerHunt")
    public List<ScavengerHuntWithPois> loadAllHunts();

    @Transaction
    @Query("SELECT * FROM scavengerHunt WHERE scavengerHuntName LIKE :scavengerHuntName")
    public List<ScavengerHuntWithPois> loadScavengerHuntWithPOIs(String scavengerHuntName);

    @Update
    public void updatePois(List<PointOfInterest> pois);

}
