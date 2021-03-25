package com.example.testapp1.Helper;

import android.location.Location;

public interface LocationUpdate<T> {
    void onLocationUpdate(Location location);
}
