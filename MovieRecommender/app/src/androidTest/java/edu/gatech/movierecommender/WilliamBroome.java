package edu.gatech.movierecommender;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbroome14 on 4/4/16.
 */
@RunWith(AndroidJUnit4.class)

public class WilliamBroome extends AndroidTestCase {

    List<Movie> movies = new ArrayList<>();

    @Before
    public void setUp() {
        this.getContext().deleteDatabase("testDB1.db");
        World.setDatabase(this.getContext().openOrCreateDatabase("testDB1.db", Context.MODE_PRIVATE, null));
        DBHelper.initMovieTable();
        DBHelper.initUserTable();

        User me = new User("Will", "wbroome14@gmail.com", "wbroome14", "soccer14");
        me.setProfile(new Profile("CS", "sup"));
        DBHelper.addUser(me);


        Movie stepbros = new Movie("Step Brothers");
        stepbros.setUrl("step");
        Movie hangover = new Movie("Hangover");
        hangover.setUrl("hang");
        Movie red = new Movie("RED");
        red.setUrl("r");
        Movie inception = new Movie("Inception");
        inception.setUrl("in");
        Movie spm = new Movie("Saving Private Ryan");
        spm.setUrl("s");

        DBHelper.addNewMovie(stepbros);
        DBHelper.addNewMovie(hangover);
        DBHelper.addNewMovie(red);
        DBHelper.addNewMovie(inception);
        DBHelper.addNewMovie(spm);

        Rating r = new Rating(1, "nah", me);

        DBHelper.addRating(stepbros, r);
        DBHelper.addRating(hangover, r);
        DBHelper.addRating(red, r);
        DBHelper.addRating(inception, r);
        DBHelper.addRating(spm, r);

        stepbros.addRating(r);
        hangover.addRating(r);
        red.addRating(r);
        inception.addRating(r);
        spm.addRating(r);

        movies.add(stepbros);
        movies.add(hangover);
        movies.add(red);
        movies.add(inception);
        movies.add(spm);
    }

    @Test
    public void addMoviesOrder() {
        List<Movie> call = DBHelper.getAllMovies();
        boolean first, second, third, fourth, last;
        first = call.get(0).getTitle().equals(movies.get(0).getTitle());
        second = call.get(1).getTitle().equals(movies.get(1).getTitle());
        third = call.get(2).getTitle().equals(movies.get(2).getTitle());
        fourth = call.get(3).getTitle().equals(movies.get(3).getTitle());
        last = call.get(4).getTitle().equals(movies.get(4).getTitle());
        assertTrue(first && second && third && fourth && last);
    }

    @Test
    public void isMovie() {
        assertTrue(DBHelper.isMovie("Saving Private Ryan"));
        assertFalse(DBHelper.isMovie("savingprivateryan"));
        assertTrue(DBHelper.isMovie("Inception"));
        assertFalse(DBHelper.isMovie("Hungar Games"));
        assertTrue(DBHelper.isMovie("RED"));
        assertTrue(DBHelper.isMovie("Step Brothers"));
    }
    
}