package com.example.testapp1.Helper;

import android.util.Log;

import com.example.testapp1.Entities.PointOfInterest;

import java.util.List;

public class ScavengerHuntSingleton {
    public static ScavengerHuntSingleton instance;

    private String huntId;
    private String creatorName;

    private List<PointOfInterest> pois;

    private ScavengerHuntSingleton() {}

    public static void instantiate() {
        if (instance == null) {
            instance = new ScavengerHuntSingleton();
        }
        else {
            Log.d("ScavengerHuntSingleton", "There already is an instance of the ScavengerHuntSingleton");
        }
    }

    public static ScavengerHuntSingleton getInstance() {
        return instance;
    }

    public void setId(String identifier) {
        this.huntId = identifier;
    }

    public void setCreator(String creator) {
        this.creatorName = creator;
    }

    public void setPOIs(List<PointOfInterest> poiList) {
        this.pois = poiList;
    }

    public String getId() {
        return huntId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public List<PointOfInterest> getPOIList() {
        return pois;
    }
}
