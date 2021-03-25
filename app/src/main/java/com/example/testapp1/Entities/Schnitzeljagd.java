package com.example.testapp1.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Schnitzeljagd {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String schnitzeljagdName;
}
