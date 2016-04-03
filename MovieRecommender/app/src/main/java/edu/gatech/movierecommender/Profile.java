package edu.gatech.movierecommender;

public class Profile {

    /**
     * Major
     */
    private String major = "CS";
    /**
     * Description
     */
    private String desc = "Add description.";

    /**
     * Constructor
     */
    public Profile() {
    }

    /**
     * Constructor
     *
     * @param m movie
     * @param d description
     */
    public Profile(String m, String d) {
        major = m;
        desc = d;
    }

    /**
     * Return the major for this profile
     *
     * @return the major
     */
    public String getMajor() {
        return major;
    }

    /**
     * Set the major for this profile
     *
     * @param myMajor major
     */
    public void setMajor(String myMajor) {
        major = myMajor;
    }

    /**
     * Return the description for the profile
     *
     * @return the description
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Set the description for this profile
     *
     * @param myDesc description
     */
    public void setDesc(String myDesc) {
        this.desc = myDesc;
    }
}
