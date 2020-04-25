package com.example.attendancemanager1;

import android.graphics.drawable.RotateDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private ArrayList<AttendanceItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {

        void onpresentclick(int position);
        void onabsentclick(int position);
        void onnolecclick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder{
        public TextView subname;
        public TextView attendance;
        public ImageButton present;
        public ImageButton nolec;
        public ImageButton absent;
        public ProgressBar percent;
        public TextView percent_text;


        public AttendanceViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            subname = itemView.findViewById(R.id.subnameatt);
            attendance = itemView.findViewById(R.id.attendanceatt);
            present =  itemView.findViewById(R.id.presentatt);
            absent = itemView.findViewById(R.id.absentatt);
            nolec = itemView.findViewById(R.id.nolecatt);
            percent = itemView.findViewById(R.id.progressatt);
            percent_text = itemView.findViewById(R.id.progresstextatt);

            present.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onpresentclick(position);
                        }
                    }
                }
            });

            absent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onabsentclick(position);
                        }
                    }
                }
            });

            nolec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onnolecclick(position);
                        }
                    }
                }
            });


        }
    }
    public AttendanceAdapter(ArrayList<AttendanceItem> exampleList) {
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_item, parent, false);
        AttendanceViewHolder avh = new AttendanceViewHolder(v, mListener);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        AttendanceItem curentItem = mExampleList.get(position);
        int curpres = curentItem.getPres_tot();
        int curtot = curentItem.getAtt_tot();
        int att=0;
        if(curtot!=0)
             att =Math.round((curpres/(float)curtot)*100);
        String dispatt = curpres + "/" + curtot;
        holder.subname.setText(curentItem.getSubname());
        holder.attendance.setText(dispatt);
        holder.percent_text.setText(""+att);
        holder.percent.setMax(100);
        holder.percent.setProgress(0);
        holder.percent.setProgress(100-att);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
