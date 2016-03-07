package edu.gatech.movierecommender;

import java.util.ArrayList;

/**
 * Created by Nick on 2/27/2016. No, my friend
 */
public class Movie implements Comparable<Movie> {

    private String title;
    private ArrayList<Rating> ratings;
    private float averageRating;

    public Movie(String t) {
        title = t;
        ratings = new ArrayList<>();
    }

    public void addRating(Rating r) {
        ratings.add(r);
        float aggregateRating = (ratings.size() - 1) * averageRating;

        averageRating = (aggregateRating + ratings.get(ratings.size() - 1).getRating())
                    / ratings.size();
    }

    public String getTitle() { return title; }

    public float getAverageRating() { return averageRating; }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public int compareTo(Movie m) { // sorts from greatest to least average rating
        if (this.getAverageRating() > m.getAverageRating()) {
            return -1;
        } else if (this.getAverageRating() == m.getAverageRating()) {
            return 0;
        } else {
            return 1;
        }
    }

    public String toString() {
        return this.title + " : " + this.averageRating;
    }
}
