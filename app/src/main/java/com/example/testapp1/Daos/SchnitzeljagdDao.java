package com.example.testapp1.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.testapp1.Entities.PointOfInterest;
import com.example.testapp1.Entities.Schnitzeljagd;
import com.example.testapp1.Entities.SchnitzeljagdwithPois;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

@Dao
public interface SchnitzeljagdDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Completable insertSchnitzeljagd(Schnitzeljagd schnitzeljagd);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPOI(PointOfInterest poi);

    @Query("SELECT * FROM PointOfInterest")
    public List<PointOfInterest> loadPOIs();

    @Transaction
    @Query("SELECT * FROM Schnitzeljagd")
    public Single<List<SchnitzeljagdwithPois>> loadSchnitzelJagdwithPOIs();

}
