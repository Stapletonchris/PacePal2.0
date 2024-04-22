package com.ssd.gps_30;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

//import android.content.Context;
//import android.app.Activity;
//
//import android.Manifest;
//import android.content.pm.PackageManager;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationServie extends Service {

    //Location Request String
    public static final String ACTION_REQUEST_PERMISSIONS = "com.ssd.gps_30.REQUEST_PERMISSIONS";

    //Constants
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    //private Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Background actions that take place before onCreate()
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    //On create works line a main function is kt
    @Override
    public void onCreate() {
        super.onCreate();
        //context = getApplicationContext();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (!locationResult.getLocations().isEmpty()) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    Location location = locationResult.getLocations().get(latestLocationIndex);
                    Log.d("mylog", "Lat is: " + ((Location) location).getLatitude() + ", Lng is: " + location.getLongitude());
                }

            }
        };


    }
    // request location from the user
    private void requestLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // Check if the app has location permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Send broadcast to request permissions
            Intent permissionIntent = new Intent(ACTION_REQUEST_PERMISSIONS);
            // This method will result in a callback to onRequestPermissionsResult() in the calling Activity
            // Handle the result there to start location updates if permission is granted
            //ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

    }
}
