package edu.gatech.movierecommender;

/**
 * Created by Nick on 2/27/2016.
 */
public class Rating {

    private float rating;
    private String comment;
    private User poster;

    public Rating(float r, String c, User p) {
        rating = r;
        comment = c;
        poster = p;
    }

    public float getRating(){
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public User getPoster() {
        return poster;
    }

}
