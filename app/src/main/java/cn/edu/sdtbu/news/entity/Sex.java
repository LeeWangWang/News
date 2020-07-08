package cn.edu.sdtbu.news.entity;

import java.io.Serializable;

/**
 * @Author:李旺旺
 * @Date:2020/7/8 8:38
 * @Description:
 */
public class Sex implements Serializable {

    private static final long serialVersionUID = -788683661488678398L;
    private int sexId;
    private String sex;

    public Sex() {
        super();
    }

    public Sex(int sexId, String sex) {
        this.sexId = sexId;
        this.sex = sex;
    }

    public int getSexId() {
        return sexId;
    }

    public void setSexId(int sexId) {
        this.sexId = sexId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
