package uambition.ares.ywq.uambition.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ares on 15/7/24.
 */
public class AmbitionDate {

    private int year;
    private int month;
    private int day;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public void setDate(int year,int month,int day){

        this.year=year;
        this.month=month;
        this.day=day;
    }

    public static AmbitionDate getDateFromStr(String str){
        AmbitionDate date=new AmbitionDate();
        int dexOfYear=str.indexOf("年");
        int dexOfMonth=str.indexOf("月");
        int dexOfDay = str.indexOf("日");
        int year=Integer.parseInt(str.substring(0, dexOfYear));
        int month=Integer.parseInt(str.substring(dexOfYear+1, dexOfMonth));
        int day=Integer.parseInt(str.substring(dexOfMonth+1, str.length()-1));

        date.setYear(year);
        date.setMonth(month);
        date.setDay(day);
        return date;

    }

    public String getDateStr(){
       String date="";
        date=year+"年"+"";
        if(month<10){
            date=date+"0"+month+"月";
        }else{
            date=date+month+"月";
        }

        if (day<10){
            date=date+"0"+day+"日";

        }else{
            date=date+day+"日";
        }
        return date;
    }

    /**
     * 判断结束日期是否比开始日期晚
     * @param begin
     * @param end
     * @return
     */
    public static boolean compareIsRightDates(AmbitionDate begin,AmbitionDate end){

        boolean isRight =true;

        if(end.year>begin.year){

            isRight=true;

            return isRight;
        }else{


            if(end.month>begin.month){

                isRight=true;
                return isRight;
            }else{
                if(end.day>=begin.day){

                    isRight=true;
                }else{
                    isRight=false;

                }
                return isRight;
            }


        }






    }


    //获取时间间隔日数
    public static Integer getBetweenDays(String beginDate,String endDate){

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");

        int day = 0;
        Date begin;
        Date end;

        try {
            begin=simpleDateFormat.parse(beginDate);
            end=simpleDateFormat.parse(endDate);
            //day=(int)(end.getTime()-begin.getTime())/(24*60*60*1000);
            Calendar fromCalendar = Calendar.getInstance();
            fromCalendar.setTime(begin);
            fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
            fromCalendar.set(Calendar.MINUTE, 0);
            fromCalendar.set(Calendar.SECOND, 0);
            fromCalendar.set(Calendar.MILLISECOND, 0);

            Calendar toCalendar = Calendar.getInstance();
            toCalendar.setTime(end);
            toCalendar.set(Calendar.HOUR_OF_DAY, 0);
            toCalendar.set(Calendar.MINUTE, 0);
            toCalendar.set(Calendar.SECOND, 0);
            toCalendar.set(Calendar.MILLISECOND, 0);
            day= (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));


        } catch (ParseException e) {
            e.printStackTrace();
        }


        return (Integer)day;
    }


}
