package com.example.testapp1.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"poiID", "scavengerHuntName"})
public class PointOfInterest {
    @NonNull
    public String poiID;

    // Key of the table of the scavengerHunt to enable the one-to-many-relationship
    @NonNull
    public String scavengerHuntName;

    public Double poiLocationLat;
    public Double poiLocationLong;

    @ColumnInfo(defaultValue = "")
    public String poiRiddle;

}
