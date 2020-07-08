package cn.edu.sdtbu.news.entity;


/**
 * 新闻资讯表(News)表实体类
 *
 * @author yelihu
 * @since 2020-06-23 23:24:10
 */
public class NewsVO extends News {

    private String tagName;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public NewsVO() {
    }

    @Override
    public String toString() {
        return "NewsVO{" + super.toString()+
                "tagName='" + tagName + '\'' +
                '}';
    }
}