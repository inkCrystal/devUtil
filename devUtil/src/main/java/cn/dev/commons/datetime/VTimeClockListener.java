package cn.dev.commons.datetime;

public interface VTimeClockListener {

    default String listenerInfo(){
        return getClass().toString();
    }

    void onSecondChange(int year ,int month ,int dayOfMonth ,int  hour ,int minute ,int second );

    void onMinuteChange(int year ,int month ,int dayOfMonth ,int  hour ,int minute );

    void onHourChange(int year ,int month ,int dayOfMonth , int hour );

    void onDayOfMonthChange(int year ,int month ,int dayOfMonth );
    void onMonthChange(int year ,int month );

    void onYearChange(int year);




}
