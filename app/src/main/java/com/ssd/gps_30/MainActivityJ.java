package com.ssd.gps_30;

//import static com.ssd.gps_30.Manifest.*;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import com.ssd.gps_30.Manifest;


public class MainActivityJ extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request Location
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Request Location Permission
            startService();
        }

    }
    void startService(){
        Intent intent = new Intent(MainActivityJ.this, LocationServie.class);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                startService();
            } else {
                Toast.makeText(this, "GIVE ME PERMISSOINS!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
