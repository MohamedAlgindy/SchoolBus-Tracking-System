package com.example.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Attendance;
import com.example.myapplication2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {

    View view;
    List<Attendance> attendanceList;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public AttendanceAdapter(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, name;
        Spinner attendance;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            attendance = itemView.findViewById(R.id.attendanceSpinner);
        }
    }

    @NonNull
    @Override
    public AttendanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceAdapter.MyViewHolder holder, final int position) {
        final Attendance attendance = attendanceList.get(position);
        holder.id.setText(attendance.getId());
        holder.name.setText(attendance.getName());

        holder.attendance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = holder.attendance.getSelectedItem().toString();
                if(!selectedItem.equals(attendance.getAttendance())){
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    attendanceList.get(position).setAttendance(selectedItem);
                }
                attendance.setAttendance(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public List<Attendance> getAttendanceList(){
        return attendanceList;
    }
}
