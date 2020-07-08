package cn.edu.sdtbu.news.entity;


/**
 * 收藏关系表(Favorite)表实体类
 *
 * @author yelihu
 * @since 2020-06-23 23:24:10
 */
public class Favorite{

    private Integer id;

    //新闻id    
    private Integer newsId;

    //用户id    
    private Integer createBy;

    //收藏时间    
    private Integer createTime;
}