package edu.gatech.movierecommender;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nick on 2/27/2016. No, my friend
 */
public class Movie implements Comparable<Movie> {

    private String title;
    private ArrayList<Rating> ratings;
    private float averageRating;
    private HashMap<String, ArrayList<Float>> majorRatings;

    public Movie(String t) {
        title = t;
        ratings = new ArrayList<>();
        majorRatings = new HashMap<String, ArrayList<Float>>();
    }

    public void addRating(Rating r) {
        ratings.add(r);
        float aggregateRating = (ratings.size() - 1) * averageRating;

        averageRating = (aggregateRating + ratings.get(ratings.size() - 1).getRating())
                    / ratings.size();

        String major = World.currentUser.getProfile().getMajor();

        if (majorRatings.containsKey(major)) {
            majorRatings.get(major).add(r.getRating());
        } else {
            majorRatings.put(major, new ArrayList<Float>());
        }
    }

    public HashMap<String, ArrayList<Float>> getMajorRatings() { return majorRatings; }

    public float getAverageMajorRating(String major) {
        ArrayList<Float> listRatings = majorRatings.get(major);

        float aggregateRating = 0;

        for (Float f : listRatings) {
            aggregateRating += f;
        }

        return aggregateRating / listRatings.size();
    }

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
