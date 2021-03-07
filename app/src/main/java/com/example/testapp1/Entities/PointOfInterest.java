package com.example.testapp1;

import android.location.Location;
import android.media.Image;

import androidx.room.Entity;

@Entity
public class PointOfInterest {
    public int poiID;
    public Location poiLocation;
    public String poiHint;
    public String[] poiAnswers;
    public Image poiImage;

}
