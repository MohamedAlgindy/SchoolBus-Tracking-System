package com.example.myapplication2;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapter.ParentAttendanceAdapter;
import com.example.Models.Attendance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendanceParent extends AppCompatActivity {

    EditText searchBar;
    DatabaseReference databaseReference;
    Attendance attendance;
    List<Attendance> attendanceList;
    RecyclerView recyclerView;
    ParentAttendanceAdapter parentAttendanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_parent);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recycler_view);
        attendanceList = new ArrayList<>();
        parentAttendanceAdapter = new ParentAttendanceAdapter(attendanceList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        searchBar = findViewById(R.id.search_bar);

        recyclerView.setAdapter(parentAttendanceAdapter);
    };

    public void search(View view){
        if(!searchBar.getText().toString().trim().equals("")){
            databaseReference.child("Attendance").orderByChild("id").equalTo(searchBar.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                            attendance = userSnapshot.getValue(Attendance.class);
                            attendance.setDate(attendance.getDate());
                            attendance.setAttendance(attendance.getAttendance());
                            attendanceList.add(attendance);
                        }
                        parentAttendanceAdapter.notifyDataSetChanged();
                    } else{
                        attendanceList.clear();
                        parentAttendanceAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "No record found.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}