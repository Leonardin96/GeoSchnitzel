package com.example.testapp1;

import android.location.Location;
import android.media.Image;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PointOfInterest {
    //PK ist Name den der User für die assoziierte Schnitzeljagd auswählt + Ziffer (1-4)
    @PrimaryKey
    public int poiID;

    public Location poiLocation;
    public String poiHint;
    public String poiAnswer1;
    public String poiAnswer2;
    public String poiAnswer3;
    public String poiAnswer4;
    public Image poiImage;

}
