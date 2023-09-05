package com.example.mylingual.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Helper {
    public static String getTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("PST"));
        return sdf.format(new Date());
    }
}
