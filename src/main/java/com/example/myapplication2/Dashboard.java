package com.example.myapplication2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapter.AttendanceAdapter;
import com.example.Models.Attendance;
import com.example.Models.BusLocation;
import com.example.Models.User;
import com.example.preferences.MySharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dashboard extends AppCompatActivity implements LocationListener {
    RecyclerView recyclerView;
    AttendanceAdapter attendanceAdapter;
    List<Attendance> attendanceList;
    DatabaseReference databaseReference;
    Attendance attendance;
    User user;
    String thisDate;
    TextView startText;
    private Button btnLogout;
    private Button btnSendLocation;
    private static final int REQUEST_LOCATION = 1;
    private Location busLocation;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = findViewById(R.id.recycler_view);
        startText = findViewById(R.id.startText);
        btnLogout = findViewById(R.id.btn_logout);
        btnSendLocation = findViewById(R.id.btn_send_location);
        progressBar=findViewById(R.id.progressbar);

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        thisDate = currentDate.format(todayDate);
        startText.setText("Attendance for the date of " + thisDate);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        attendanceList = new ArrayList<>();

        //callback for logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySharedPreference.getInstance(Dashboard.this).resetAll();
                Intent intent = new Intent(Dashboard.this, DisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        attendanceAdapter = new AttendanceAdapter(attendanceList);
        recyclerView.setAdapter(attendanceAdapter);
        //Calling this method to get details..
        prepareData();
        getLocation();
        //Send bus location on server
        btnSendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateBusLocation();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }

    public void prepareData() {
        databaseReference.child("Users").orderByChild("role").equalTo("Student").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        user = userSnapshot.getValue(User.class);
                        attendance = new Attendance();
                        attendance.setId(user.getUserid());
                        attendance.setName(user.getName());
                        attendance.setAttendance("");
                        attendanceList.add(attendance);
                    }
                    attendanceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void submit(View view) {
        List<Attendance> attendanceList = attendanceAdapter.getAttendanceList();
        for (Attendance attendance : attendanceList) {
            attendance.setDate(thisDate);
            databaseReference.child("Attendance").child(databaseReference.push().getKey()).setValue(attendance);
        }
        Toast.makeText(getApplicationContext(), "Attendance updated successfully.", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
    }


    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    //This method is calling to get bus location
    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0f, this);
    }

    //Location changed
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.e("location----",""+location.getLongitude());
        busLocation=location;
        if(btnSendLocation.getVisibility()==View.GONE){
            progressBar.setVisibility(View.GONE);
            btnSendLocation.setVisibility(View.VISIBLE);
        }
    }


    /*
    * This method is calling to validate bus location
    * */
    private void validateBusLocation(){
        progressBar.setVisibility(View.VISIBLE);
        if(busLocation!=null) {
            saveLocation();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveLocation();
                }
            },1000);
        }
    }

    /*
     * This method is calling to send bus location
     * */
    private void saveLocation(){
        String userId = MySharedPreference.getInstance(this).getUserId();
        BusLocation busLocations = new BusLocation(userId, busLocation.getLatitude(), busLocation.getLongitude());
        FirebaseDatabase.getInstance().getReference("bus_location")
                .child(userId)
                .setValue(busLocations).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Dashboard.this, getString(R.string.location_message),Toast.LENGTH_LONG).show();
            }
        });
    }
}