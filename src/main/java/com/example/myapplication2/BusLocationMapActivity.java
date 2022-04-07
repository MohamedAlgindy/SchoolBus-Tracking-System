package com.example.myapplication2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Models.BusLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BusLocationMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private DatabaseReference databaseReference;
    private ArrayList<BusLocation> busLocations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_location);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        busLocations=new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.bus_map);
        mapFragment.getMapAsync(this);
        }

        /*
        * On Map Ready
        * */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap=googleMap;
        getBusLocation();
        //callback on marker
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude), 20.0f));
                return true;
            }
        });
    }



    //This method is calling to get all bus location
    public void getBusLocation() {
        busLocations.clear();
        databaseReference.child("bus_location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        BusLocation busLocation = userSnapshot.getValue(BusLocation.class);
                        busLocations.add(busLocation);
                    }
                    displayMarker();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    //This method is calling to display all buses on map
    private void displayMarker(){
        for(BusLocation busLocation:busLocations) {
            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(busLocation.getBusLatitude(), busLocation.getBusLongitude())).title(busLocation.getUserId()).snippet("Lat :"+busLocation.getBusLatitude()+" Long :"+busLocation.getBusLongitude());
            googleMap.addMarker(options);
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.345130, 55.386190), 12.0f));
    }
}
