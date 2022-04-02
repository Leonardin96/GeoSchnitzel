package com.example.testapp1.Helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext);
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


    @SuppressLint("MissingPermission")
    public void getLastLocation(actionFinishedCallback callback) {
        Task<Location> last_location = fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) appContext, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.i("Last Known Location", location.getLatitude() + " " + location.getLongitude());
                            callback.onComplete(location);
                        }
                    }
                });
    }

    public void startLocationUpdates(LocationCallback locationCallback) {
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
