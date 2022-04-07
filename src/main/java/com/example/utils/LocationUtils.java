package com.example.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import java.util.List;
public class LocationUtils {
    private static final int REQUEST_LOCATION = 1;
    public static Location getCurrentLocation(Context context){
        Location bestLocation=null;
        LocationManager locationManager= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            List<String> providers = locationManager.getProviders(true);
            for (String provider : providers) {
                Location  currentLocation = locationManager.getLastKnownLocation(provider);
                if (currentLocation == null) { continue; }
                if (bestLocation == null || currentLocation.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = currentLocation;
                }
            }
            return bestLocation;
        }
        return  bestLocation;
    }

}
