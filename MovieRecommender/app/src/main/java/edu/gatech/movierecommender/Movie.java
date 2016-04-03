package edu.gatech.movierecommender;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie implements Comparable<Movie> {

    /**
     * Title
     */
    private final String title;
    /**
     * Ratings
     */
    private final List<Rating> ratings;
    /**
     * Average Rating
     */
    private float averageRating;
    /**
     * Major Ratings
     */
    private final Map<String, ArrayList<Float>> majorRatings;
    /**
     * Image URL
     */
    private String imgURL = "";

    /**
     * Constructor
     *
     * @param t title
     */
    public Movie(String t) {
        title = t;
        ratings = new ArrayList<>();
        majorRatings = new HashMap<>();
    }

    /**
     * Add a rating to this movie
     *
     * @param r rating
     */
    public void addRating(Rating r) {
        ratings.add(r);

        final float aggregateRating = (ratings.size() - 1) * averageRating;

        averageRating = (aggregateRating + ratings.get(ratings.size() - 1).getRating())
                    / ratings.size();

        final String major = r.getPoster().getProfile().getMajor();

        //Add to majorRatings ArrayList
        if (majorRatings.containsKey(major)) {
            majorRatings.get(major).add(r.getRating());
        } else {
            majorRatings.put(major, new ArrayList<Float>());
            majorRatings.get(major).add(r.getRating());
        }
    }

    /**
     * Gets ratings for a major
     *
     * @return the HashMap of majors to ratings
     */
    public Map<String, ArrayList<Float>> getMajorRatings() { return majorRatings; }

    /**
     * Calculates average rating for a major
     *
     * @param major the major
     * @return the averaged float
     */
    public float getAverageMajorRating(String major) {
        if (majorRatings.containsKey(major)) {
            //Get all ratings for a major
            final ArrayList<Float> listRatings = majorRatings.get(major);

            //Calculate average rating
            float aggregateRating = 0;

            for (final Float f : listRatings) {
                aggregateRating += f;
            }

            return aggregateRating / listRatings.size();
        } else {
            return 0;
        }
    }

    /**
     * Set image url for this movie
     *
     * @param s url of image
     */
    public void setUrl(String s) { imgURL = s; }

    /**
     * Gets item position
     *
     * @param r float to set rating
     */
    public void setAverageRating(float r) {
        averageRating = r;
    }

    /**
     * Gets image url for this movie
     *
     * @return the image url
     */
    public String getURL() { return imgURL; }

    /**
     * Get the title of this movie
     *
     * @return the title of this movie
     */
    public String getTitle() { return title; }

    /**
     * Get the average rating of this movie
     *
     * @return the average rating for this movie
     */
    public float getAverageRating() { return averageRating; }

    /**
     * Get rating ArrayList for this movie
     *
     * @return ArrayList of ratings
     */
    public List<Rating> getRatings() {
        return ratings;
    }

    /**
     * Compares a movie against this movies average rating
     *
     * @param m compares this movie to average
     * @return -1 for <, 0 for =, 1 for >
     */
    public int compareTo(@NonNull Movie m) {
        if (this.getAverageRating() > m.getAverageRating()) {
            return -1;
        } else if (this.getAverageRating() == m.getAverageRating()) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Get String representation of the class
     *
     * @return class attributes as string
     */
    public String toString() {
        return this.title + " : " + this.averageRating;
    }
}
