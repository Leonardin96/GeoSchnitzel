package com.example.testapp1.Entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ScavengerHuntWithPois {
    @Embedded
    public ScavengerHunt scavengerHunt;

    @Relation(
            parentColumn = "scavengerHuntName",
            entityColumn = "scavengerHuntName"
    )
    public List<PointOfInterest> pois;
}
