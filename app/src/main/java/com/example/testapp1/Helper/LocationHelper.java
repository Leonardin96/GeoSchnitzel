package com.example.testapp1.Helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.testapp1.Callbacks.actionFinishedCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationHelper {
    // Location related
    public FusedLocationProviderClient fusedLocationClient;
    public LocationCallback locationCallback;

    // miscellaneous
    Context appContext;

    public LocationHelper(Context context) {
        appContext = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext);
    }

    /**
     * Checks if the application has the permission to track the location of the user.
     * @param activity {Activity}
     * @return boolean
     */
    public boolean checkForLocationPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }

    public void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                1
        );
    };


    /**
     * Checks for the last known location and gives it to the provided callback.
     * @param callback {actionFinishedCallback}
     */
    public void getSingleLocation(actionFinishedCallback callback, Activity activity) {
        boolean permissionGranted = checkForLocationPermission(activity);
        if (permissionGranted) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) appContext, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                callback.onComplete(location);
                            }
                        }
                    });
        }

    }

    /**
     * Starts location updates of the fusedLocationProviderClient.
     * @param locationCallback {actionFinishedCallback}
     * @param activity {Activity}
     */
    public void startLocationUpdates(LocationCallback locationCallback, Activity activity) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest
            .setInterval(1000)
            .setPriority(100);
        boolean permissionGranted = checkForLocationPermission(activity);
        if (permissionGranted) {
            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }
    }

    /**
     * Stops the location updates from the fusedLocationProviderClient.
     */
    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
