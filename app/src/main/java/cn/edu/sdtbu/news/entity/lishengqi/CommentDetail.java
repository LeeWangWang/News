package cn.edu.sdtbu.news.entity.lishengqi;

import java.util.List;

public class CommentDetail {
    private int id;
    private String nickName;
    private String userLogo;
    private String content;
    private String imgId;
    private int replyTotal;
    private String createDate;
    private List<CommentReplyDetail> replyList;

    public CommentDetail(String nickName, String content, String createDate) {
        this.nickName = nickName;
        this.content = content;
        this.createDate = createDate;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getNickName() {
        return nickName;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }
    public String getUserLogo() {
        return userLogo;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
    public String getImgId() {
        return imgId;
    }

    public void setReplyTotal(int replyTotal) {
        this.replyTotal = replyTotal;
    }
    public int getReplyTotal() {
        return replyTotal;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getCreateDate() {
        return createDate;
    }

    public void setReplyList(List<CommentReplyDetail> replyList) {
        this.replyList = replyList;
    }
    public List<CommentReplyDetail> getReplyList() {
        return replyList;
    }

    @Override
    public String toString() {
        return "CommentDetail数据{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", userLogo='" + userLogo + '\'' +
                ", content='" + content + '\'' +
                ", imgId='" + imgId + '\'' +
                ", replyTotal=" + replyTotal +
                ", createDate='" + createDate + '\'' +
                ", replyList=" + replyList +
                '}';
    }

}
