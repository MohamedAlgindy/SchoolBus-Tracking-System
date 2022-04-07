package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Models.User;
import com.example.constants.AppConstant;
import com.example.preferences.MySharedPreference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText txt_userid, txt_password;
    Button btn_submit;
    private ProgressBar progressBar;
    private DatabaseReference ref;
    private String userid;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_userid = (EditText) findViewById(R.id.txt_userid);
        txt_password = (EditText) findViewById(R.id.txt_password);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void login(View view) {
        try {
            userid = txt_userid.getText().toString();
            password = txt_password.getText().toString();

            if (TextUtils.isEmpty(userid)) {
                txt_userid.setError("Please enter your UserID");
                txt_userid.requestFocus();
                return;
            }


            if (TextUtils.isEmpty(password)) {
                txt_password.setError("Please enter your password");
                txt_password.requestFocus();
                return;
            }

            ref.orderByChild("userid").equalTo(userid.trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String dbPassword = null;
                        String role = null;

                        for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                            dbPassword = userSnapshot.getValue(User.class).getPass();
                            role = userSnapshot.getValue(User.class).getRole();
                        }

                        if (password.equals(dbPassword) && role.equals("Supervisor")) {
                            Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            MySharedPreference.getInstance(MainActivity.this).setUserLoginType(AppConstant.SUPER_VISOR_TYPE);
                            MySharedPreference.getInstance(MainActivity.this).setUserId(userid.toString().trim());
                            Intent fp = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(fp);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Username and Password", Toast.LENGTH_SHORT).show();

                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "Invalid Username and Password", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Failed to read value.", Toast.LENGTH_SHORT).show();
                }
            });

            progressBar.setVisibility(View.VISIBLE);

        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), "IP address not working", Toast.LENGTH_SHORT).show();
        }
    }


}








