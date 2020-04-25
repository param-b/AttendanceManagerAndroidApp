package com.example.attendancemanager1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class AttendanceShowActivity extends BaseDrawerActivity {
    private RecyclerView mRecyclerView;
    private AttendanceAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AttendanceItem> mAttendanceList;
    RelativeLayout frame_popup;
    Button closePopupBtn, saveBtn;
    DatabaseHelper dbhelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_attendance_show);
        getLayoutInflater().inflate(R.layout.activity_attendance_show, frameLayout);


        dbhelper = new DatabaseHelper(this);

        frame_popup = findViewById(R.id.attendanceparent);

        createExampleList();
        buildRecyclerView();
    }

    public void present(int position)
    {
        final String subject_name = mAttendanceList.get(position).getSubname();
        final int updated_tot = mAttendanceList.get(position).getAtt_tot() + 1;
        final int updated_pres = mAttendanceList.get(position).getPres_tot() + 1;

        dbhelper.updateTotal("PRESENT");

        createPopup(subject_name,"Present", updated_tot, updated_pres);
        //mAdapter.notifyDataSetChanged();
    }
    public void absent(int position)
    {
        final String subject_name = mAttendanceList.get(position).getSubname();
        final int updated_tot = mAttendanceList.get(position).getAtt_tot() + 1;
        final int updated_pres = mAttendanceList.get(position).getPres_tot();
        createPopup(subject_name,"Absent", updated_tot, updated_pres);

        dbhelper.updateTotal("ABSENT");

        //mAdapter.notifyDataSetChanged();
    }

    public void nolec(final int position)
    {
        final String subject_name = mAttendanceList.get(position).getSubname();
        saveinfo(subject_name, "NoLecture", "");
    }


    public void showToast(String text)
    {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }
    public void createPopup(final String sub_name, final String status, final int up_tot, final int up_pres)
    {
        LayoutInflater layoutInflater = (LayoutInflater) AttendanceShowActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customView = layoutInflater.inflate(R.layout.popup_info,null);
        closePopupBtn =  customView.findViewById(R.id.close_popup);
        saveBtn = customView.findViewById(R.id.save_popup);
        //instantiate popup window
        final PopupWindow popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //display the popup window
        popupWindow.showAtLocation(frame_popup, Gravity.CENTER, 0, 0);

        final EditText info = customView.findViewById(R.id.info_popup);

        if(status.equals("Absent"))
          info.setHint("What did you miss?");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String popup_content = info.getText().toString();
                updateTable(sub_name, up_tot, up_pres);
                saveinfo(sub_name, status, popup_content);
                showToast(status+" in " + sub_name);
                popupWindow.dismiss();

            }
        });

        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public void updateTable(String subname, int total, int present)
    {
        dbhelper.updateattendance(subname, total, present);
        createExampleList();
        buildRecyclerView();
    }

    public void saveinfo(String sub_name, String status, String info)
    {
        dbhelper.saveData(sub_name, info, status);
    }





    public void createExampleList() {
        mAttendanceList = dbhelper.getAttendance();
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.att_recycle);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AttendanceAdapter(mAttendanceList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AttendanceAdapter.OnItemClickListener() {
            @Override
            public void onpresentclick(int position) {
                present(position);
            }

            @Override
            public void onabsentclick(int position) {
                absent(position);

            }

            @Override
            public void onnolecclick(int position) {
                nolec(position);

            }
        });
    }
}
