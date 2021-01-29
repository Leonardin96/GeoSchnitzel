package com.example.testapp1;

import android.location.Location;
import android.media.Image;

import androidx.room.ColumnInfo;

public class PointOfInterest {
    @ColumnInfo(name = "poiId") public int poiId;

    public Location poiLocation;
    public String poiHint;
    public String poiAnswer1;
    public String poiAnswer2;
    public String poiAnswer3;
    public String poiAnswer4;
    public Image poiImage;

}
