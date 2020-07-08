package cn.edu.sdtbu.news.entity;

import java.io.Serializable;

/**
 * @Author:李旺旺
 * @Date:2020/7/8 8:37
 * @Description:
 */
public class Age implements Serializable {

    private static final long serialVersionUID = -6145823953887445629L;
    private int ageId;
    private String age;

    public int getAgeId() {
        return ageId;
    }

    public void setAgeId(int ageId) {
        this.ageId = ageId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Age() {
        super();
    }

    public Age(int ageId, String age) {
        this.ageId = ageId;
        this.age = age;
    }
}
