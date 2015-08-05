package uambition.ares.ywq.uambition.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by ares on 15/7/25.
 */
//评论
public class Comment extends BmobObject {

    private String content;//评论内容
    private Ambition ambition;//评论的目标
    private User commenter;//评论人


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Ambition getAmbition() {
        return ambition;
    }

    public void setAmbition(Ambition ambition) {
        this.ambition = ambition;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }
}
