package com.example.myapplication2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.constants.AppConstant;
import com.example.preferences.MySharedPreference;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SplashActivity extends AppCompatActivity  {
    final int SPLASH_DELAY=3000;
    private final  int LOCATION_STATE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Check Permission
        if(checkLocationPermission()){
            transitPage();
        } else{
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},LOCATION_STATE);
        }
    }

    //This method is calling to validate screen and transit after 3 seconds
    public void transitPage(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (MySharedPreference.getInstance(SplashActivity.this).getUserLoginType()){
                    case AppConstant.SUPER_VISOR_TYPE:
                        Intent dashboardIntent=new Intent(SplashActivity.this, Dashboard.class);
                        startActivity(dashboardIntent);
                        finish();
                        break;
                    case AppConstant.PRINCIPAL_TYPE:
                        Intent signupIntent=new Intent(SplashActivity.this, Signup.class);
                        startActivity(signupIntent);
                        finish();
                        break;
                    case AppConstant.PARENT_TYPE:
                        Intent parentDashboard=new Intent(SplashActivity.this, ParentDashboard.class);
                        startActivity(parentDashboard);
                        finish();
                        break;
                    default:
                        Intent splashIntent=new Intent(SplashActivity.this, DisplayActivity.class);
                        startActivity(splashIntent);
                        finish();
                        break;

                }

            }
        },SPLASH_DELAY);

    }


    /*
     * This method is calling to check run time permission
     *
     * */
    private boolean checkLocationPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),ACCESS_FINE_LOCATION);
        if(result== PackageManager.PERMISSION_GRANTED) return true;
        else return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_STATE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted) { transitPage(); }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                            LOCATION_STATE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;

        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
