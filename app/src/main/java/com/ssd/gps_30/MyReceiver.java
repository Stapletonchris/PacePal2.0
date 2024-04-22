package com.ssd.gps_30;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
public class MyReceiver extends BroadcastReceiver {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if location permissions are already granted
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If not, request them
            ActivityCompat.requestPermissions((MainActivityJ) context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // If permissions are already granted, perhaps notify or start a service directly
            Toast.makeText(context, "Location permission already granted", Toast.LENGTH_SHORT).show();
            // You can start a service or perform another action here
        }
    }
}
