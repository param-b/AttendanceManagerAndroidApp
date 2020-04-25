package com.example.attendancemanager1;

public class AttendanceItem {

    private String subname;
    private int att_tot;
    private int pres_tot;

    public AttendanceItem(String subname, int att_tot, int pres_tot)
    {
        this.subname = subname;
        this.att_tot = att_tot;
        this.pres_tot = pres_tot;
    }

    public String getSubname() {
        return subname;
    }

    public int getAtt_tot() {
        return att_tot;
    }
    public int getPres_tot() {
        return pres_tot;
    }



}
