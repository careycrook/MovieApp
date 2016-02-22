package edu.gatech.movierecommender;

public class Profile {

    private String major = "CS";
    private String desc = "Add description.";

    public Profile() {
    }

    public Profile(String m, String d) {
        major = m;
        desc = d;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
