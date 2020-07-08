package cn.edu.sdtbu.news.entity;
import java.time.LocalDateTime;


/**
 * 新闻资讯表(News)表实体类
 *
 * @author yelihu
 * @since 2020-06-23 23:24:10
 */
public class News{

    private Integer id;

    //标题    
    private String title;

    //文章发布时间    
    private String createTime;

    //正文内容    
    private String content;

    //收藏数
    private Integer favoriteNumber;

    //专栏编号
    private Integer tagId;

    public News(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public News() {
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", createTime='" + createTime + '\'' +
                ", content='" + content + '\'' +
                ", favoriteNumber=" + favoriteNumber +
                ", tagId=" + tagId +
                '}';
    }
}