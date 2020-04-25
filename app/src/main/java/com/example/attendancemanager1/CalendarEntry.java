package com.example.attendancemanager1;

public class CalendarEntry {



    private String date;
    private String status;
    private String info;
    private String subject;



    public CalendarEntry(String date, String status, String info, String subject)
    {
        this.date = date;
        this.status = status;
        this.info = info;
        this.subject = subject;
    }
    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public String getSubject() {
        return subject;
    }


}
