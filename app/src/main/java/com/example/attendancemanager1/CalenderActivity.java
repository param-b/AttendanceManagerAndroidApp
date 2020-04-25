package com.example.attendancemanager1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CalenderActivity extends AppCompatActivity {
    CalendarView calendar;
    DatabaseHelper dbhelper;
    ListView presList, absList, nolecList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbhelper = new DatabaseHelper(this);

        setContentView(R.layout.activity_calender);

        calendar = findViewById(R.id.calenderdata);
        presList = findViewById(R.id.present_list);
        absList = findViewById(R.id.absent_list);
        nolecList = findViewById(R.id.no_lecture_list);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String date= dayOfMonth+"-"+(month+1)+"-"+year;
                //SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
                //String date = df.format(c);
                showToast("Date "+date+ " Selected!");
                getListData(date);
            }
        });

    }

    public void getListData(String date) {

        ArrayList<CalendarEntry> arr_list;
        System.out.println("Comes Here1");
        ArrayList<String> pres_list = new ArrayList<>();
        ArrayList<String> abs_list = new ArrayList<>();
        ArrayList<String> nolec_list = new ArrayList<>();
        arr_list = dbhelper.getInfo(date);
        for (CalendarEntry ce : arr_list) {
            String status = ce.getStatus();
            String present, absent, nolec;
            if (status.equals("Present")) {
                present = ce.getSubject() + " : " + ce.getInfo();
                pres_list.add(present);
            } else if (status.equals("Absent")) {
                absent = ce.getSubject() + " : " + ce.getInfo();
                abs_list.add(absent);
            } else {
                nolec = ce.getSubject() + " : " + ce.getInfo();
                nolec_list.add(nolec);
            }
        }
        ArrayAdapter adapter1 = new ArrayAdapter<String>(this, R.layout.list_abspres, pres_list);
        ArrayAdapter adapter2 = new ArrayAdapter<String>(this, R.layout.list_abspres, abs_list);
        ArrayAdapter adapter3 = new ArrayAdapter<String>(this, R.layout.list_abspres, nolec_list);

        System.out.println("Comes Here2");

        presList.setAdapter(adapter1);
        absList.setAdapter(adapter2);
        nolecList.setAdapter(adapter3);

        decideVisibility(pres_list, presList);
        decideVisibility(abs_list, absList);
        decideVisibility(nolec_list, nolecList);


    }

    public void showToast(String text)
    {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public void decideVisibility(ArrayList<String> array, ListView lst)
    {
        if (!array.isEmpty()) {
            lst.setVisibility(View.VISIBLE);
        }
        else {
            lst.setVisibility(View.GONE);
        }
    }


}
