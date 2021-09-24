package com.example.testapp1.Helper;

import android.util.Log;

import com.example.testapp1.Entities.PointOfInterest;
import com.example.testapp1.Entities.ScavengerHuntWithPois;

import java.util.ArrayList;
import java.util.List;

public class ScavengerHuntSingleton {
    public static ScavengerHuntSingleton instance;

    private String huntId = "";
    private String creatorName = "";

    private ScavengerHuntWithPois hunt = new ScavengerHuntWithPois();
    private List<PointOfInterest> pois = new ArrayList<PointOfInterest>();

    private ScavengerHuntSingleton() {}

    public static void instantiate() {
        if (instance == null) {
            synchronized (ScavengerHuntSingleton.class) {
                if (instance == null) {
                    instance = new ScavengerHuntSingleton();
                }
            }
        }
    }

    public static synchronized void reset(){
        instance = new ScavengerHuntSingleton();
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

    public void setHunt(ScavengerHuntWithPois hunt) {
        this.hunt = hunt;
    }

    public ScavengerHuntWithPois getHunt() {
        return hunt;
    }

    public PointOfInterest getPoiFromList(int position) {
        return pois.get(position);
    }

    public void addPoiToList(PointOfInterest poi) {
        if (this.pois == null) {
            this.pois = new ArrayList<PointOfInterest>();
        }
        this.pois.add(poi);
    }

    public void overridePoi(PointOfInterest poi, int poiNumber) {
        this.pois.set(poiNumber, poi);
    }

    public void removePoiFromList(int position) {
        this.pois.remove(position);
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
