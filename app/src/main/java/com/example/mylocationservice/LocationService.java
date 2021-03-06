package com.example.mylocationservice;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class LocationService extends Service {

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    String value = new StringBuilder(""+location.getLatitude()).append("--")
                                                    .append(location.getLongitude())
                                                    .toString();
                    try {
                        MainActivity.getInstance().showLocation(value);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), location.getAltitude()+"", Toast.LENGTH_SHORT).show();
                        Log.e("TAG3", "onReceive: " + location.getAltitude());
                    }
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    private void requestLocation() {
        locationRequest = new LocationRequest();
        // setInterval: Ph????ng th???c d??ng ????? ?????t kho???ng th???i gian mong mu???n cho c??c c???p nh???t v??? tr?? ??ang ho???t ?????ng, t??nh b???ng mili gi??y.
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        // setSmallestDisplacement: Ph????ng th???c d??ng ????? ?????t d???ch chuy???n t???i thi???u gi???a c??c l???n c???p nh???t v??? tr?? t??nh b???ng m??t
        // m???c ?????nh l?? 0
        //locationRequest.setSmallestDisplacement(10f);// kho???ng c??ch kho???ng 10 m??t m???i c???p nh???t v??? tr?? l???i
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }
}