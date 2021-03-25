package com.example.testapp1.Entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SchnitzeljagdwithPois {
    @Embedded
    public Schnitzeljagd schnitzeljagd;

    @Relation(
            parentColumn = "schnitzeljagdName",
            entityColumn = "schnitzeljagdName"
    )
    public List<PointOfInterest> pois;
}
