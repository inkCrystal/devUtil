package cn.dev.clock;

import cn.dev.commons.datetime.DateTimeUtil;
import cn.dev.commons.verification.VerificationTool;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 时间缓存对象
 *
 */
public class DateTimeEntry implements Serializable {
    @Serial
    private static final long serialVersionUID = 8019214826500419507L;

    private int year ;
    private int month ;
    private int dayOfMonth =1;
    private int hour =0;
    private int minute =0;
    private int second =0;

    public DateTimeEntry(LocalDateTime localDateTime) {
        this(localDateTime.getYear(),localDateTime.getMonthValue(),localDateTime.getDayOfMonth(),localDateTime.getHour(),localDateTime.getMinute(),localDateTime.getSecond());
    }

    public DateTimeEntry(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        setYear(year);
        setMonth(month);
        setDayOfMonth(dayOfMonth);
        setHour(hour);
        setMinute(minute);
        setSecond(second);
    }

    public DateTimeEntry(int year, int month) {
        setYear(year);
        setMonth(month);
    }

    public DateTimeEntry(int year, int month, int dayOfMonth, int hour, int minute) {
        setYear(year);
        setMonth(month);
        setDayOfMonth(dayOfMonth);
        setHour(hour);
        setMinute(minute);

    }

    public DateTimeEntry(int year, int month, int dayOfMonth) {
        setYear(year);
        setMonth(month);
        setDayOfMonth(dayOfMonth);
    }


    /**
     * 获取当前时间
     * @return
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * 设置 月
     * @param month
     */
    public void setMonth(int month) {
        VerificationTool.isMatch(month,t->t<13&&t>0,"月设置错误");
        this.month = month;
    }

    /**
     * 设置 日
     * @param dayOfMonth
     */
    public void setDayOfMonth(int dayOfMonth) {
        final int maxValueForDayOfMonth = DateTimeUtil.maxValueForDayOfMonth(this.year, this.month);
        VerificationTool.isMatch(dayOfMonth,t->t<maxValueForDayOfMonth,"日设置错误");
        this.dayOfMonth = dayOfMonth;
    }

    /**
     * 设置 时
     * @param hour
     */
    public void setHour(int hour) {
        VerificationTool.isMatch(hour,t->t<24&&t>=0,"小时设置错误");
        this.hour = hour;
    }

    /**
     * 设置 分
     * @param minute
     */
    public void setMinute(int minute) {
        VerificationTool.isMatch(minute,t->t<60&&t>=0,"分设置错误");
        this.minute = minute;
    }

    /**
     * 设置 秒
     * @param second
     */
    public void setSecond(int second) {
        VerificationTool.isMatch(second,t->t<60&&t>=0,"秒设置错误");
        this.second = second;
    }


    /**
     * 获取下一年的时间Entry
     * @return
     */
    public DateTimeEntry nextYear(){
        this.year++;
        return this;
    }

    /**
     * 获取下一个月的时间Entry
     * @return
     */
    public DateTimeEntry nextMonth(){
        this.month++;
        if(this.month>12){
            this.month = 1;
            return this.nextYear();
        }
        return this;
    }

    /**
     * 获取下一天的时间Entry
     * @return
     */
    public DateTimeEntry nextDay(){
        this.dayOfMonth++;
        if(this.dayOfMonth>DateTimeUtil.maxValueForDayOfMonth(this.year,this.month)){
            this.dayOfMonth = 1;
            return nextMonth();
        }
        return this;
    }

    /**
     * 获取下一个小时的时间Entry
     * @return
     */
    public DateTimeEntry nextHour(){
        this.hour++;
        if(this.hour>23){
            this.hour = 0;
            return this.nextDay();
        }
        return this;
    }

    /**
     * 获取下一分 的时间Entry
     * @return
     */
    public DateTimeEntry nextMinute(){
        this.minute++;
        if(this.minute>59){
            this.minute = 0;
            return this.nextHour();
        }
        return this;
    }


    /**
     * 获取下一个秒的时间Entry
     * @return
     */
    public DateTimeEntry nextSecond(){
        this.second++;
        if(this.second>59){
            this.second = 0;
            return this.nextMinute();
        }
        return this;
    }

    /**
     * 获取下一个秒为目标秒的时间
     * @param targetSecond
     * @return
     */
    public DateTimeEntry getNextTimeWhileSecondIs(int targetSecond){
        VerificationTool.isMatch(targetSecond,t->t<60&&t>=0,"目标秒（"+targetSecond+"）不可达到");
        do{
            nextSecond();
            if(this.second == targetSecond ){
                return this;
            }
        }while (true);
    }

    /**
     * 获取下一个分为目标分的时间
     * @param targetMinute 目标分
     * @return
     */
    public DateTimeEntry getNextTimeWhileMinuteIs(int targetMinute){
        VerificationTool.isMatch(targetMinute,t->t<60&&t>=0,"目标分（"+targetMinute+"）不可达到");
        do{
            nextMinute();
            if(this.minute == targetMinute ){
                return this;
            }
        }while (true);
    }
    /**
     * 获取下一个小时为目标小时的时间
     * @param targetHour 目标小时
     * @return
     */
    public DateTimeEntry getNextTimeWhileHourIs(int targetHour){
        VerificationTool.isMatch(targetHour,t->t<24&&t>=0,"目标小时（"+targetHour+"）不可达到");
        do{
            nextHour();
            if(this.hour == targetHour ){
                return this;
            }
        }while (true);
    }

    /**
     * 获取下一个日为目标日的时间
     * @param targetDayOfMonth 目标日
     * @return
     */
    public DateTimeEntry getNextTimeWhileDayOfMonthIs(int targetDayOfMonth){
        VerificationTool.isMatch(targetDayOfMonth,t->t<32&&t>=1,"目标日（"+targetDayOfMonth+"）不可达到");
        do{
            nextDay();
            if(this.dayOfMonth == targetDayOfMonth ){
                return this;
            }
        }while (true);
    }

    /**
     * 获取下一个月份为目标月份的时间
     * @param targetMonth 目标月份
     * @return
     */
    public DateTimeEntry getNextTimeWhileMonthIs(int targetMonth){
        VerificationTool.isMatch(targetMonth,t->t<13&&t>=1,"目标月（"+targetMonth+"）不可达到");
        do{
            nextMonth();
            if(this.month == targetMonth ){
                return this;
            }
        }while (true);
    }

    /**
     * 转换为LocalDateTime
     * @return
     */
    public LocalDateTime toLocalDateTime(){
        return LocalDateTime.of(this.year,this.month,this.dayOfMonth,this.hour,this.minute,this.second);
    }

    /**
     * 转换为Date
     * @return
     */
    public Date toDate(){
        return DateTimeUtil.toDate(toLocalDateTime());
    }

}
