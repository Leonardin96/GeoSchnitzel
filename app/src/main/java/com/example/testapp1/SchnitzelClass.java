package com.example.testapp1;

import android.location.Location;
import android.media.Image;

import java.util.HashMap;

public class SchnitzelClass {
    private Location schnitzelLocation;
    private String schnitzelRiddle;
    private HashMap<String, String> schnitzelAnswers;
    private HashMap<String, Image> schnitzelImages;

    public SchnitzelClass(Location location, String riddle, HashMap answers, HashMap images) {
        schnitzelLocation = location;
        schnitzelRiddle = riddle;
        schnitzelAnswers = answers;
        schnitzelImages = images;
    }

    public Location getSchnitzelLocation() {
        return schnitzelLocation;
    }

    public String getSchnitzelRiddle() {
        return schnitzelRiddle;
    }

    public HashMap<String, String> getSchnitzelAnswers() {
        return schnitzelAnswers;
    }

    public HashMap<String, Image> getSchnitzelImages() {
        return schnitzelImages;
    }

}
