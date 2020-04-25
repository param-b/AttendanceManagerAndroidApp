package com.example.attendancemanager1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Attendance";
    private static final String COLUMN_TOTAL = "Total";
    private static final String COLUMN_PRESENT = "Present";
    private static final String COLUMN_SUB_NAME = "SubName";
    private static final String COLUMN_SUBJECT_DATE = "date";
    private static final String COLUMN_SUBJECT_INFO = "info";
    private static final String COLUMN_SUBJECT_STATUS = "status";
    private static final String COLUMN_SUBJECT_TYPE = "type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DATABASE_NAME
                        + "( "
                        + COLUMN_SUB_NAME + " TEXT PRIMARY KEY, "
                        +COLUMN_PRESENT + " INTEGER, "
                        +COLUMN_TOTAL +  " INTEGER )");

        //Initialization for testing will be removed after user entries is configured
        addSubjectinit(db,"TOTAL123");
        addSubjectinit(db,"MP");
        addSubjectinit(db,"DM");
        addSubjectinit(db,"DF");
        createSubjectTable(db,"MP");
        createSubjectTable(db,"DM");
        createSubjectTable(db,"DF");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
    public void createSubjectTable(SQLiteDatabase db, String tableName) {
        //final SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE " + tableName
                + " ( "
                + COLUMN_SUBJECT_DATE + " TEXT, "
                + COLUMN_SUBJECT_STATUS + " TEXT, "
                + COLUMN_SUBJECT_INFO +  " TEXT )");
    }
    public void addSubjectinit(SQLiteDatabase db, String sub_name)
    {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SUB_NAME, sub_name);
        contentValues.put(COLUMN_TOTAL, 0);
        contentValues.put(COLUMN_PRESENT, 0);
        db.insert(DATABASE_NAME,null, contentValues);
    }
    public void updateTotal(String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String sub_name = "TOTAL123";
        String query = "select * from " + DATABASE_NAME +" where TRIM( "+ COLUMN_SUB_NAME + " ) = '"+ sub_name + "'";
        Cursor res = db.rawQuery(query,null);
        res.moveToNext();
        int tot = Integer.parseInt(res.getString(res.getColumnIndex(COLUMN_TOTAL)));
        int present = Integer.parseInt(res.getString(res.getColumnIndex(COLUMN_PRESENT)));
        if(status.equalsIgnoreCase("ABSENT"))
            tot += 1;
        else
        {
            tot +=1;
            present +=1;
        }
        ContentValues contentValues  = new ContentValues();
        contentValues.put(COLUMN_TOTAL,tot);
        contentValues.put(COLUMN_PRESENT,present);
        db.update(DATABASE_NAME, contentValues, COLUMN_SUB_NAME+" = ? ", new String[] {sub_name } );
    }

    public int[] retureTotal()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String sub_name = "TOTAL123";
        String query = "select * from " + DATABASE_NAME +" where TRIM( "+ COLUMN_SUB_NAME + " ) = '"+ sub_name + "'";
        Cursor res = db.rawQuery(query,null);
        res.moveToNext();
        int tot = Integer.parseInt(res.getString(res.getColumnIndex(COLUMN_TOTAL)));
        int present = Integer.parseInt(res.getString(res.getColumnIndex(COLUMN_PRESENT)));
        int arr[] = new int[2];
        arr[0] = tot;
        arr[1] = present;
        return arr;
    }


    public void saveData(String sub_name, String info, String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Calendar calendar = Calendar.getInstance();

        int thisYear = calendar.get(Calendar.YEAR);
        Log.d(TAG, "# thisYear : " + thisYear);

        int thisMonth = calendar.get(Calendar.MONTH);
        Log.d(TAG, "@ thisMonth : " + thisMonth);

        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d(TAG, "$ thisDay : " + thisDay);

        String date = thisDay + "-" +(thisMonth+1)+"-"+thisYear;
        contentValues.put(COLUMN_SUBJECT_INFO,info);
        contentValues.put(COLUMN_SUBJECT_STATUS, status);
        contentValues.put(COLUMN_SUBJECT_DATE,date);
        db.insert(sub_name, null, contentValues);
    }

    public void updateattendance(String sub_name, int updated_total, int updated_present)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRESENT, updated_present);
        contentValues.put(COLUMN_TOTAL, updated_total);
        db.update(DATABASE_NAME, contentValues, COLUMN_SUB_NAME+" = ? ", new String[] {sub_name } );
    }

    public ArrayList<AttendanceItem> getAttendance()
    {
        ArrayList<AttendanceItem> array_list = new ArrayList<AttendanceItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + DATABASE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String sub = (res.getString(res.getColumnIndex(COLUMN_SUB_NAME)));
            if(!sub.equals("TOTAL123")) {
                int tot = Integer.parseInt(res.getString(res.getColumnIndex(COLUMN_TOTAL)));
                int present = Integer.parseInt(res.getString(res.getColumnIndex(COLUMN_PRESENT)));
                array_list.add(new AttendanceItem(sub, tot, present));
            }
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<CalendarEntry> getInfo(String date)
    {
        ArrayList<CalendarEntry> array_list = new ArrayList<CalendarEntry>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + DATABASE_NAME, null );
        res.moveToFirst();
        System.out.println("Comes Here3");


        while(res.isAfterLast() == false) {
            System.out.println("Comes Here8");

            String sub = (res.getString(res.getColumnIndex(COLUMN_SUB_NAME)));
            if (!sub.trim().equals("TOTAL123")) {
                System.out.println("Comes Here5");

                String query = "select * from " + sub+" where TRIM( "+ COLUMN_SUBJECT_DATE + " ) = '"+ date.trim()+ "'";
                Cursor c2 = db.rawQuery(query,null);
                if (c2.moveToFirst()){
                    do {
                        System.out.println("Comes Here6");


                        String datein = (c2.getString(c2.getColumnIndex(COLUMN_SUBJECT_DATE)));
                        String info = (c2.getString(c2.getColumnIndex(COLUMN_SUBJECT_INFO)));
                        String status = (c2.getString(c2.getColumnIndex(COLUMN_SUBJECT_STATUS)));
                        array_list.add(new CalendarEntry(datein, status, info, sub));

                    } while(c2.moveToNext());
                }
            }
            res.moveToNext();
        }
        return array_list;
    }
}
