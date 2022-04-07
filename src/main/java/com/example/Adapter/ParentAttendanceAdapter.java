package com.example.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Attendance;
import com.example.myapplication2.R;

import java.util.List;

public class ParentAttendanceAdapter extends RecyclerView.Adapter<ParentAttendanceAdapter.MyViewHolder> {

    List<Attendance> attendanceList;
    View view;

    public ParentAttendanceAdapter(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date, attendance;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            attendance = itemView.findViewById(R.id.attendance);
        }
    }

    @NonNull
    @Override
    public ParentAttendanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_attendance_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentAttendanceAdapter.MyViewHolder holder, int position) {
        Attendance attendance = attendanceList.get(position);
        holder.date.setText(attendance.getDate());
        holder.attendance.setText(attendance.getAttendance());
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }
}
