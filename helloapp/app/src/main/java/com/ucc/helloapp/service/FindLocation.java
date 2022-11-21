package com.ucc.helloapp.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.List;

/**
 * @author Meiqi Huang
 * @date 2022-11-06
 * <p>
 * Get the device's current location and store the location info into AudioInfo when Location object is not null;
 * When calling its FindLocation, it would check permission first and then check Network Connection and GPS Service.
 * If one of them is available, it would use Network Provider first to get location, then use GPS Provider if the first is not available.
 * Update the location when location change.
 */
public class FindLocation extends Service implements LocationListener {
    public static final String NETWORK_GPS = "NetworkGPS";
    private Context context;
    private boolean isGPSEnabled = false;
    // flag for network status
    private boolean isNetworkEnabled = false;
    // flag for GPS status
    private boolean canGetLocation = false;
    private Location location;
    private double latitude = 0;
    private double longitude = 0;

    private static final long MIN_DISTANCE_UPDATES = 3;// 3 meters

    private static final long MIN_TIME_UPDATES = 3000; // 3sec

    protected LocationManager locationManager;

    public FindLocation(Context context) {
        this.context = context;
        getLocation();
    }

    private Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            // get network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            // get GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isNetworkEnabled && !isGPSEnabled) {
                // both provider not avaliable
                Log.e(NETWORK_GPS, "Disable");
            } else {
                this.canGetLocation = true;
                // get location from network provider first
                if (false) { //isNetworkEnabled
                    Log.d(NETWORK_GPS, "NetworkEnabled");
                    if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d(NETWORK_GPS, "checkSelfPermission=false");
                        return null;
                    }
                    Log.d(NETWORK_GPS, "locationManager.requestLocationUpdates");
                    locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_UPDATES, new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            Log.d(NETWORK_GPS, "onLocationChanged");
                        }
                    });
                    if (locationManager != null) {
                        Log.d(NETWORK_GPS, "locationManager.getLastKnownLocation");
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.d(NETWORK_GPS, "longtitude,latitude" + longitude + "," + latitude);
                        }

                    }
                }
                // GPS
                else if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_UPDATES, new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            Log.e(NETWORK_GPS, "onLocationChanged");
                        }
                    });
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public double getLatitude() {
        if (location != null)
            latitude = location.getLatitude();

        return latitude;
    }

    public double getLongitude() {
        if (location != null)
            longitude = location.getLongitude();

        return longitude;
    }

    public boolean canGetlocation() {
        return this.canGetLocation;
    }

    public void Alert() {

        Toast.makeText(context, "GPS is not opened", Toast.LENGTH_SHORT);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    /**
     * @param provider
     * @param status
     * @param extras
     * @deprecated
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        //  LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        //    LocationListener.super.onProviderDisabled(provider);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
