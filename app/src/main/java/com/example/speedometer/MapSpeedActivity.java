package com.example.speedometer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Objects;

public abstract class MapSpeedActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationListener locationListener;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_speed);
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment;
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        EditText editTextLatitude = findViewById(R.id.editText);
        EditText editTextLongitude = findViewById(R.id.editText2);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Location");
        final ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            private String userId;


            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String databaseLatitudeString = Objects.requireNonNull(dataSnapshot.child("latitude").getValue()).toString().substring(1,
                            Objects.requireNonNull(dataSnapshot.child("latitude").getValue()).toString().length() - 1);
                    String databaseLongitudedeString = dataSnapshot.child("longitude").getValue().toString().substring(1, dataSnapshot.child("longitude").getValue().toString().length() - 1);
                    Objects.requireNonNull(dataSnapshot.child("tvSpeed").getValue()).toString().substring(1, dataSnapshot.child("tvSpeed").getValue().toString().length() - 1);

                    String[] stringLat = databaseLatitudeString.split(", ");
                    Arrays.sort(stringLat);
                    String latitude = stringLat[stringLat.length - 1].split("=")[1];

                    String[] stringLong = databaseLongitudedeString.split(", ");
                    Arrays.sort(stringLong);
                    String longitude = stringLong[stringLong.length - 1].split("=")[1];

                    LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                    mMap.addMarker(new MarkerOptions().position(latLng).title("userId: " + userId + " ,tvSpeed: " + latitude + " , " + longitude));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }
}

