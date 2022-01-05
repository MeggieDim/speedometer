package com.example.speedometer;

import android.location.Location;

public class GetLocation extends Location {
    public GetLocation(Location l) {
        super(l);

    }
    @Override
    public float distanceTo(Location dest) {
        return super.distanceTo(dest);
    }
    @Override
    public double getAltitude() {
        return super.getAltitude();
    }

    @Override
    public float getSpeed() {
        return super.getSpeed();
    }

    @Override
    public float getAccuracy() {
        return super.getAccuracy();
    }
}
