package com.example.testapp1;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = @Index(value = {"tourName"}, unique = true))
public class Tour {
    @PrimaryKey
    public int tourId;

    public String tourName;

    @Embedded
    public PointOfInterest poi1;

    @Embedded
    public PointOfInterest poi2;

    @Embedded
    public PointOfInterest poi3;

    @Embedded
    public PointOfInterest poi4;
}
