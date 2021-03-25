package com.example.testapp1.Helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationHelper {
    // Location related
    public FusedLocationProviderClient fusedLocationClient;
    public LocationCallback locationCallback;
    public LocationRequest locationRequest;
    public boolean requestingLocationUpdates = true;
    public Location currentLocation;

    // miscellaneous
    Context appContext;
    int toastDuration;

    public LocationHelper(Context context) {
        appContext = context;
        toastDuration = Toast.LENGTH_SHORT;
    }

    public boolean checkPermission() {

        if (ActivityCompat.checkSelfPermission(appContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(appContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(appContext, "You need to enable permissions to display location !", toastDuration).show();
        } else {

            return true;
        }

        return false;
    }

    public void setupLocationRequest(final LocationUpdate<Location> callback) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    //Update UI
                    currentLocation = location;
                    callback.onLocationUpdate(location);
                }
            }
        };
    }

    public void startLocationUpdates() {
        boolean permissionGranted = checkPermission();
        if (permissionGranted == true) {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }
    }

    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
