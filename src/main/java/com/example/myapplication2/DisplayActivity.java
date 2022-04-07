package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayActivity extends AppCompatActivity {
    Button btn_Principal;
    Button btn_Supervisor;
    Button btn_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        btn_Principal = (Button) findViewById(R.id.btn_Principal);
        btn_Supervisor = (Button) findViewById(R.id.btn_Supervisor);
        btn_parent = (Button) findViewById(R.id.btn_parent);

        btn_Principal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent fp=new Intent(getApplicationContext(),PrincipalLogin.class);
                startActivity(fp);

            }
        });

        btn_Supervisor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent fp=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(fp);

            }
        });

        btn_parent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent fp=new Intent(getApplicationContext(), ParentLogin.class);
                startActivity(fp);
            }
        });
    }
}
