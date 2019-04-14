package com.example.android.alzmate_client.ReminderDetail;



public class ReminderHolder {
    public String title;
    public String body;
    public String time;

    public ReminderHolder(String title, String body, String time)
    {
        this.title=title;
        this.body=body;
        this.time=time;
    }
    public String getTitle()
    {
        return title;
    }
    public String getBody()
    {
        return body;
    }
    public String getTime()
    {
        return time;
    }
}
