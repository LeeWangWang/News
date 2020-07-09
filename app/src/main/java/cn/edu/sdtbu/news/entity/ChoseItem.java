package cn.edu.sdtbu.news.entity;

public class ChoseItem {
    private String Name;
    private boolean Chose;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isChose() {
        return Chose;
    }

    public void setChose(boolean chose) {
        Chose = chose;
    }
}
