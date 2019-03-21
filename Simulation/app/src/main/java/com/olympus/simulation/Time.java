package com.olympus.simulation;

public class Time {
    public static boolean validateTime(String time){
        int hour;
        int minute;
        try{
            if(time.charAt(1) == ':'){
                hour = Integer.parseInt(time.charAt(0) + "");
                minute = Integer.parseInt(time.charAt(2) + "" + time.charAt(3));
            }
            else if(time.charAt(2) == ':'){
                hour = Integer.parseInt(time.charAt(0) + "" + time.charAt(1));
                minute = Integer.parseInt(time.charAt(3) + "" + time.charAt(4));
            }
            else {
                return false;
            }
        }
        catch(NumberFormatException e){
            return false;
        }

        if(hour < 0 || hour > 23){
            return false;
        }
        else if(minute < 0 || minute > 59) {
            return false;
        }
        return true;
    }

    public static int convertToInt(String time) {
        int hour;
        int minute;
        if(time.charAt(1) == ':'){
            hour = Integer.parseInt((time.charAt(0) + ""));
            minute = Integer.parseInt(time.charAt(2) + "" + time.charAt(3));
        }
        else{
            hour = Integer.parseInt(time.charAt(0) + "" + time.charAt(1));
            minute = Integer.parseInt(time.charAt(3) + "" + time.charAt(4));
        }

        //TODO: Account for cases where the start time is not 00:00

        return (hour * 60) + minute;
    }

    public static String convertToString(int time) {
        int hours = time / 60;
        int minutes = time % 60;

        //TODO: Account for cases where start time is not 00:00
        return String.format("%02d:%02d", hours, minutes);
    }
}
