/*
Resources for making this:

Android Developer Docs: https://developer.android.com/health-and-fitness/guides/basic-fitness-app/read-step-count-data
Project Gurukul: https://projectgurukul.org/android-kotlin-step-counter-app/
StepCounterDemo Repository: https://github.com/dev-mgkaung/StepCounterDemo
StepCounter: https://programmerworld.co/android/how-to-create-walking-step-counter-app-using-accelerometer-sensor-and-shared-preference-in-android/

*/

package com.ssd.gps_30;

// Required imports for sensor and other functionalities
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivityJ extends AppCompatActivity implements SensorEventListener {

    // Declaration of class variables related to location and UI elements
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private Button trackingButton;
    private TextView speedTextView;
    private TextView paceTextView;
    private Button viewRunHistoryButton;
    private TextView stepsTextView; // TextView for displaying steps

    private boolean isTracking = false;

    // Sensor variables for step detection
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private double lastMagnitude = 0;
    private int stepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        trackingButton = findViewById(R.id.startButton);
        speedTextView = findViewById(R.id.speedTextView);
        paceTextView = findViewById(R.id.paceTextView);
        viewRunHistoryButton = findViewById(R.id.viewRunHistoryButton);
        stepsTextView = findViewById(R.id.stepsTextView); // Make sure you have this TextView in your layout.xml

        // Initialize the FusedLocationProviderClient for location updates
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Define what happens when location updates are received
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    updateLocationInfo(location);
                }
            }
        };

        // Set up the button click listeners
        trackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTrackingButton();
            }
        });

        viewRunHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "View Run History" button click
                // Implementation for viewing run history would go here
            }
        });

        // 1. Data Extraction: Initialize the SensorManager and accelerometer sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            // 2. Preprocessing: Register the listener for the accelerometer
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // If the device does not have an accelerometer, inform the user
            Toast.makeText(this, "Accelerometer sensor not available", Toast.LENGTH_SHORT).show();
        }
    }


    // 3. Preprocessing and Feature Extraction: Handle sensor data when it changes
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x_acceleration = event.values[0];
            float y_acceleration = event.values[1];
            float z_acceleration = event.values[2];

            // Calculate the magnitude of the acceleration vector
            double magnitude = Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration);
            double magnitudeDelta = magnitude - lastMagnitude;
            lastMagnitude = magnitude;

            // 4. Classification: Determine if the change in magnitude is significant enough to count as a step
            if (magnitudeDelta > 4) {
                stepCount++;
                // Update the UI with the new step count
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stepsTextView.setText(String.format("Steps: %d", stepCount));
                    }
                });
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Sensor accuracy changes can be handled here if needed
    }


    private void toggleTrackingButton() {
        if (!isTracking) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                startLocationUpdates();
                trackingButton.setText("Stop Tracking");
                isTracking = true;
            }
        } else {
            stopLocationUpdates();
            trackingButton.setText("Start Tracking");
            isTracking = false;
            stepCount = 0; // Reset the step count when tracking is stopped
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // 10 seconds
        locationRequest.setFastestInterval(5000); // 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        // Register the sensor listener for step detection
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        // Unregister the sensor listener to save battery since we don't need it when not tracking
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTracking) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        stopLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        stopLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates(); // Permission was granted
            } else {
                Toast.makeText(this, "Location permission is required for tracking your pace and distance.", Toast.LENGTH_LONG).show();
            }
        }
    }


    // Method to update UI with location information
    private void updateLocationInfo(Location location) {
        if (location != null) {
            float speed = location.getSpeed(); // Speed in meters/second
            float speedKmH = speed * 3.6f; // converting m/s to km/h
            speedTextView.setText(String.format("Speed: %.2f km/h", speedKmH));

            // Calculate pace in min/km (pace is the inverse of speed)
            float pace = speed > 0 ? 1000 / (speed * 60) : 0; // pace in min/km
            paceTextView.setText(String.format("Pace: %.2f min/km", pace));
        }
    }

}






