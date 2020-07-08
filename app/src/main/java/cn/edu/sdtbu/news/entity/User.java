package cn.edu.sdtbu.news.entity;

import java.io.Serializable;

/**
 * 用户表(User)表实体类
 *
 * @author yelihu
 * @since 2020-06-23 09:22:55
 */
public class User implements Serializable {

    private static final long serialVersionUID = 7624797623450361836L;
    private Integer id;

    //用户名账号（手机号码）
    private String username;

    //密码（6-16位中英混合+英文开头）
    private String password;
    
    //用户昵称（1-20位*支持emoji）    
    private String nickname;

    //头像服务器路径    
    private String avatarPath;
    
    //性别    
    private String gender;
    
    //年龄    
    private Integer age;

    //学历编号    
    private Integer degreeId;

    //职业编号    
    private Integer careerId;
    
    //创建时间
    private String createTime;

    //创建人    
    private Integer createBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Integer degreeId) {
        this.degreeId = degreeId;
    }

    public Integer getCareerId() {
        return careerId;
    }

    public void setCareerId(Integer careerId) {
        this.careerId = careerId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public User() {
    }

    public User(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(Integer id, String username, String password, String nickname, String avatarPath,
                String gender, Integer age, Integer degreeId, Integer careerId, Integer createBy) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.avatarPath = avatarPath;
        this.gender = gender;
        this.age = age;
        this.degreeId = degreeId;
        this.careerId = careerId;
        this.createTime = createTime;
        this.createBy = createBy;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", degreeId=" + degreeId +
                ", careerId=" + careerId +
                ", createTime=" + createTime +
                ", createBy=" + createBy +
                '}';
    }
}