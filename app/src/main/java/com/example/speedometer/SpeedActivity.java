package com.example.speedometer;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;


public class SpeedActivity extends AppCompatActivity  {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference userViolations;
    TextView tv_speed;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser =mFirebaseAuth.getCurrentUser();
        updateUI(currentUser);
        userViolations = FirebaseDatabase.getInstance().getReference("violations/" + currentUser.getUid());
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mFirebaseAuth = FirebaseAuth.getInstance();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);


        checkLocationPermission();

        tv_speed = findViewById(R.id.textView);

    }

    private void checkLocationPermission() {
        Log.i("message", "CheckLocationPermission method started.");
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 765);
            } else {
                ((LocationManager) getSystemService(LOCATION_SERVICE)).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
            }
            Log.i("message", "CheckLocationPermission method completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("message", "Exception during CheckLocationPermission method:" + e.getMessage());
            Toast.makeText(this, "Exception occurred, check log file for more information.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("message", "OnRequestPermissionsResult method started.");
        try {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ((TextView) findViewById(R.id.messageView)).setText(R.string.permission_not_granted);
                checkLocationPermission();
            } else {
                ((LocationManager) getSystemService(LOCATION_SERVICE)).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
            }
            Log.i("message", "OnRequestPermissionsResult method completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("message", "Exception during OnRequestPermissionsResult method:" + e.getMessage());
            Toast.makeText(this, "Exception occurred, check log file for more information.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("DefaultLocale")
    public void onLocationChanged(Location l) {
        Log.i("message", "km/h");
        if (l != null) {
            Float speed = (18 * l.getSpeed()) / 5;
            Log.i("message", String.format("%.2f", speed) + " km/h");
            if (speed > 0) {
                tv_speed.setText(String.format("%.2f", speed) + " km/h");
                if (speed > 60) {
                    Object userSpeedLimit = new UserSpeedLimit.Builder().currentspeed(speed).currentTimestamp(new Date()).build();
                    userViolations.push().setValue(userSpeedLimit);
                }


            } else {
                tv_speed.setText("You don't move");

            }
        }

    }

    @SuppressLint("MissingPermission")
    private void doStuff() {
        ((LocationManager) getSystemService(LOCATION_SERVICE)).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        Toast.makeText(this, "Wait to connect!", Toast.LENGTH_SHORT).show();
    }


    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doStuff();
            } else {
                finish();

            }
        }
    }

}