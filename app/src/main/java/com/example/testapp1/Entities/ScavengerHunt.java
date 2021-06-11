package com.example.testapp1.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ScavengerHunt {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String scavengerHuntName;

    public String creatorName;
}
