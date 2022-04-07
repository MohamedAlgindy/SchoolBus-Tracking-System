package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Models.User;
import com.example.preferences.MySharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    private EditText txt_userid, txt_fullname, txt_email, txt_password, getTxt_password, txt_phone;
    private Spinner spin_role;
    static String role;
    Button btn_submit;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txt_userid = findViewById(R.id.txt_userid);
        txt_fullname = findViewById(R.id.txt_fullname);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        txt_phone = findViewById(R.id.txt_phone);
        btnLogout=findViewById(R.id.btn_logout);
        spin_role = (Spinner) findViewById(R.id.spinner_role);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        btn_submit = (Button) findViewById(R.id.btn_submit);

        spin_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub

                role = spin_role.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        //callback for logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySharedPreference.getInstance(Signup.this).resetAll();
                Intent intent=new Intent(Signup.this,DisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
    }

    public void registerUser(View view) {


        final String userid = txt_userid.getText().toString().trim();
        final String fullname = txt_fullname.getText().toString().trim();
        final String email = txt_email.getText().toString().trim();
        final String pass = txt_password.getText().toString().trim();
        final String phone = txt_phone.getText().toString().trim();
        final String selectedrole = role;

        if (userid.isEmpty() && fullname.isEmpty() && email.isEmpty() && pass.isEmpty() && phone.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please insert value???", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userid.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please Enter userid", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fullname.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please Enter Full name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please Enter Pass", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please Enter Phone", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedrole.equals("Role")) {
            Toast.makeText(getBaseContext(), "Please Enter Role", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getBaseContext(), "Please Enter Valid email", Toast.LENGTH_SHORT).show();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(
                                    userid,
                                    fullname,
                                    email,
                                    pass,
                                    phone,
                                    selectedrole
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(Signup.this, MainActivity.class));
                                        finish();


                                        Toast.makeText(Signup.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                    } else {
                                        //display a failure message
                                        Toast.makeText(Signup.this, "Cant Registered", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
