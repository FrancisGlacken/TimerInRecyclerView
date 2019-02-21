package com.mani.rc;


public class FormatMillis {
    private String time;

    public String FormatMillisIntoHMS(long milliseconds) {

        int seconds, minutes, hours;

        //Turn millisecondsLong into int and convert into correct values
        seconds = (int) (milliseconds/1000);
        minutes = seconds/60;
        hours = minutes/60;
        // Mod those ints to keep them from 0-59
        seconds = seconds % 60;
        minutes = minutes % 60;

        // Do String.format magic and return the string
        time = (String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
        return time;
    }

    public String FormatMillisIntoDHM(long milliseconds) {
        int minutes, hours, days;
        minutes = (int)(milliseconds/60000);
        hours = minutes/60;
        days = hours/24;
        minutes = minutes % 60;
        hours = hours % 24;

        // Do String.format magic and return the string
        time = days+"d"+hours+"h"+minutes+"m";
        return time;
    }
}
