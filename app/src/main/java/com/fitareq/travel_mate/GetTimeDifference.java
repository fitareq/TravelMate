package com.fitareq.travel_mate;

import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

class GetTimeDifference
{
    static void CalculateTime( String post_time,  String timezone,  TextView textView, int id) {

        int min = 0, hour, day, month, year;
        String s = "";
        SimpleDateFormat sd;
        if (id == 2)
            sd = new SimpleDateFormat("dd-MM-yyyy");
        else
            sd = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        sd.setTimeZone(TimeZone.getTimeZone(timezone));
        String CurrentDateAndTime = sd.format(new Date());
        try {
            Date date1 = sd.parse(CurrentDateAndTime);
            Date date2 = sd.parse(post_time);
            long millis = date1.getTime() - date2.getTime();
            min = (int) (millis / 60000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (id == 1) {


            if (min >= 60) {
                hour = min / 60;
                if (hour >= 24) {
                    day = hour / 24;
                    Calendar c = Calendar.getInstance();
                    int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if (day >= monthMaxDays) {
                        month = day / monthMaxDays;
                        if (month >= 12) {
                            year = month / 12;
                            s = year + " years ago.";
                        } else
                            s = month + " months ago.";
                    } else
                        s = day + " days ago.";
                } else
                    s = hour + " hours ago.";

            } else if (min < 1)
                s = "just now.";
            else
                s = min + " minutes ago.";

            //s=min+" mins ago";
            textView.setText(s);

        }
        if (id == 2)
        {
            day = Math.abs((min/60)/24);
            if (day==0)
            {
                s = "expired";
            }else
            {
                s = day +" days left";
            }

            textView.setText(s);
        }
    }




    }

