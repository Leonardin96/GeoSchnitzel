package com.example.testapp1.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PointOfInterest {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String poiID;
    public String schnitzeljagdName;

    public Double poiLocationLat;
    public Double poiLocationLong;

    @ColumnInfo(defaultValue = "")
    public String poiHint;

    @ColumnInfo(defaultValue = "")
    public String poiQuestion;

    @ColumnInfo(defaultValue = "")
    public String poiFirstAnswer;

    @ColumnInfo(defaultValue = "")
    public String poiSecondAnswer;

    @ColumnInfo(defaultValue = "")
    public String poiThirdAnswer;

    @ColumnInfo(defaultValue = "")
    public String poiFourthAnswer;

}
