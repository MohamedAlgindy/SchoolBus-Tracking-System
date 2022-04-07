package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.preferences.MySharedPreference;

public class ParentDashboard extends AppCompatActivity {

    private Button btnLogout;
    private Button btnBusLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);
        btnLogout=findViewById(R.id.btn_logout);
        btnBusLocation=findViewById(R.id.btn_bus_location);

        //callback for logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySharedPreference.getInstance(ParentDashboard.this).resetAll();
                Intent intent=new Intent(ParentDashboard.this,DisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Bus Location callback
        btnBusLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ParentDashboard.this,BusLocationMapActivity.class);
                startActivity(intent);
            }
        });
    }

    public void attendance(View view){
        Intent intent = new Intent(this, AttendanceParent.class);
        startActivity(intent);
    }

    public void route(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
    }
}