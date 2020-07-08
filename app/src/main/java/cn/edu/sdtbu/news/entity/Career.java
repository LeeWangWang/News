package cn.edu.sdtbu.news.entity;

import java.io.Serializable;

/**
 * 职业表(Career)表实体类
 *
 * @author yelihu
 * @since 2020-06-23 23:24:10
 */
public class Career implements Serializable {

    private static final long serialVersionUID = -7625125884071931366L;
    //职业主键
    private int id;

    //职业名称：无职业、自由职业者、服务行业、IT行业、金融行业、餐饮行业、政府机关、教师    
    private String careerName;

    public Career() {
        super();
    }

    public Career(Integer id, String careerName) {
        this.id = id;
        this.careerName = careerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCareerName() {
        return careerName;
    }

    public void setCareerName(String careerName) {
        this.careerName = careerName;
    }

    @Override
    public String toString() {
        return "Career{" +
                "id=" + id +
                ", careerName='" + careerName + '\'' +
                '}';
    }
}