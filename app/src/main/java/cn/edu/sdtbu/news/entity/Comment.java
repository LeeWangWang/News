package cn.edu.sdtbu.news.entity;
import java.time.LocalDateTime;


/**
 * 评论表(Comment)表实体类
 *
 * @author yelihu
 * @since 2020-06-23 23:24:10
 */
public class Comment{

    private Integer id;
    
    //用户id    
    private Integer createBy;
    
    //资讯id    
    private Integer newsId;
    
    //回复的上一条id    
    private Integer parentId;
    
    //评论时间    
    private LocalDateTime createTime;
    
    //评论内容    
    private String commentContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}