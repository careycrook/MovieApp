package edu.gatech.movierecommender;

import java.util.ArrayList;

/**
 * Created by Nick on 2/27/2016.
 */
public class Movie {

    private String title;
    private ArrayList<Rating> ratings;

    public Movie(String t) {
        title = t;
        ratings = new ArrayList<>();
    }

    public void addRating(Rating r) {
        ratings.add(r);
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

}
