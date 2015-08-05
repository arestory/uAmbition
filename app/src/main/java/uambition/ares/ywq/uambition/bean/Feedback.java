package uambition.ares.ywq.uambition.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by ares on 15/8/1.
 */
public class Feedback extends BmobObject {

    private User feedbacker;//反馈人
    private String content;//反馈内容


    public User getFeedbacker() {
        return feedbacker;
    }

    public void setFeedbacker(User feedbacker) {
        this.feedbacker = feedbacker;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
