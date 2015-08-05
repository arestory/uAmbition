package uambition.ares.ywq.uambition.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by ares on 15/7/23.
 */
public class Ambition extends BmobObject  implements Serializable{


    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    private String title;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    private String create_user_id;
    private String beginTime;
    private String endTime;
    private User author;



    public BmobRelation getLiker() {
        return liker;
    }

    public void setLiker(BmobRelation liker) {
        this.liker = liker;
    }

    private BmobRelation liker;//点赞的人
    private Comment comment;

    private BmobRelation commentList;//评论列表

    public BmobRelation getCommentList() {
        return commentList;
    }

    public void setCommentList(BmobRelation commentList) {
        this.commentList = commentList;
    }

    private Integer favorite_num;

    public Integer getFavorite_num() {
        return favorite_num;
    }

    public void setFavorite_num(Integer favorite_num) {
        this.favorite_num = favorite_num;
    }

    public Integer getBetweenDay() {
        return betweenDay;
    }

    public void setBetweenDay(Integer betweenDay) {
        this.betweenDay = betweenDay;
    }

    private Integer betweenDay;
    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
    //是否公开


    public Boolean isPersonal() {
        return personal;
    }

    public void setPersonal(Boolean personal) {
        this.personal = personal;
    }

    private Boolean personal;


    public  void  setTitle(String title){

        this.title=title;
    }
    public  String getTitle(){
        return title;
    }



    //获取时间间隔日数
    public static Integer getBetweenDays(Ambition ambition){
        String beginDate=ambition.beginTime;
        String endDate=ambition.endTime;
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
