package cn.edu.sdtbu.news.entity;


import java.io.Serializable;

/**
 * 学历表(Degree)表实体类
 *
 * @author yelihu
 * @since 2020-06-23 23:24:08
 */
public class Degree implements Serializable {

    private static final long serialVersionUID = 5809379412074243543L;
    //学历主键
    private int id;
    
    //学历名称：高中及以下、高中、大学、研究生、博士及以上    
    private String degreeName;

    public Degree() {
        super();
    }

    public Degree(Integer id, String degreeName) {
        this.id = id;
        this.degreeName = degreeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    @Override
    public String toString() {
        return "Degree{" +
                "id=" + id +
                ", degreeName='" + degreeName + '\'' +
                '}';
    }
}