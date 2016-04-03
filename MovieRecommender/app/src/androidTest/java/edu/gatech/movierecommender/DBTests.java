package edu.gatech.movierecommender;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import static edu.gatech.movierecommender.DBHelper.DATABASE_NAME;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by theon on 4/3/2016.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class DBTests extends AndroidTestCase {

    Random rand;

    @Before
    public void setUp() {
        this.getContext().deleteDatabase("testDB.db");

        World.setDatabase(this.getContext().openOrCreateDatabase("testDB.db", Context.MODE_PRIVATE, null));

        World.getDatabase().execSQL("DROP TABLE IF EXISTS movies");

        World.getDatabase().execSQL("CREATE TABLE IF NOT EXISTS movies (_id INTEGER PRIMARY KEY "
                + "AUTOINCREMENT, title TEXT NOT NULL, averageRating REAL NOT NULL DEFAULT '0', " +
                "imgURL TEXT NOT NULL)");

        rand = new Random();
    }

    @Test
    public void getAllMoviesSize() {
        int j = rand.nextInt(100);
        ArrayList<Movie> movies = new ArrayList<Movie>();

        User u = new User("u", "u", "u", "u");
        u.setProfile(new Profile());
        DBHelper.addUser(u);

        for (int i = 0; i < j; i++) {
            Movie m = new Movie(generateString());
            m.setUrl(generateString());
            DBHelper.addNewMovie(m);

            float rating1 = 5 * rand.nextFloat();
            float rating2 = 5 * rand.nextFloat();
            float rating3 = 5 * rand.nextFloat();

            Rating r1 = new Rating(rating1, "yep", u);
            Rating r2 = new Rating(rating2, "yep", u);
            Rating r3 = new Rating(rating3, "yep", u);

            DBHelper.addRating(m, r1);
            DBHelper.addRating(m, r2);
            DBHelper.addRating(m, r3);

            m.addRating(r1);
            m.addRating(r2);
            m.addRating(r3);
        }

        List<Movie> result = DBHelper.getAllMovies();

        assertTrue(movies.size() == result.size());
    }

    @Test
    public void getAllMoviesStableOrder() {
        int j = rand.nextInt(100);
        ArrayList<Movie> movies = new ArrayList<Movie>();

        User u = new User("u", "u", "u", "u");
        u.setProfile(new Profile());
        DBHelper.addUser(u);

        for (int i = 0; i < j; i++) {
            Movie m = new Movie(generateString());
            m.setUrl(generateString());
            DBHelper.addNewMovie(m);

            float rating1 = 5 * rand.nextFloat();
            float rating2 = 5 * rand.nextFloat();
            float rating3 = 5 * rand.nextFloat();

            Rating r1 = new Rating(rating1, "yep", u);
            Rating r2 = new Rating(rating2, "yep", u);
            Rating r3 = new Rating(rating3, "yep", u);

            DBHelper.addRating(m, r1);
            DBHelper.addRating(m, r2);
            DBHelper.addRating(m, r3);

            m.addRating(r1);
            m.addRating(r2);
            m.addRating(r3);
        }

        List<Movie> result = DBHelper.getAllMovies();

        for (int i = 0; i < j; i++) {
            assertTrue(movies.get(i).getTitle().equals(result.get(i)));
        }
    }

    @Test
    public void getAllMoviesPropertiesPreserved() {
        int j = rand.nextInt(100);
        ArrayList<Movie> movies = new ArrayList<Movie>();

        User u = new User("u", "u", "u", "u");
        u.setProfile(new Profile());
        DBHelper.addUser(u);

        for (int i = 0; i < j; i++) {
            Movie m = new Movie(generateString());
            m.setUrl(generateString());
            DBHelper.addNewMovie(m);

            float rating1 = 5 * rand.nextFloat();
            float rating2 = 5 * rand.nextFloat();
            float rating3 = 5 * rand.nextFloat();

            Rating r1 = new Rating(rating1, "yep", u);
            Rating r2 = new Rating(rating2, "yep", u);
            Rating r3 = new Rating(rating3, "yep", u);

            DBHelper.addRating(m, r1);
            DBHelper.addRating(m, r2);
            DBHelper.addRating(m, r3);

            m.addRating(r1);
            m.addRating(r2);
            m.addRating(r3);
        }

        List<Movie> result = DBHelper.getAllMovies();

        for (int i = 0; i < j; i++) {
            assertTrue(movies.get(i).getTitle().equals(result.get(i).getTitle()));
            assertTrue(movies.get(i).getURL().equals(result.get(i).getURL()));
            assertTrue(movies.get(i).getAverageRating() == result.get(i).getAverageRating());
        }
    }

    private String generateString() {
        String characters = "abcdefghijklmnopqrstuvwxyz ";
        char[] text = new char[10];
        for (int i = 0; i < 10; i++)
        {
            text[i] = characters.charAt(rand.nextInt(characters.length()));
        }
        return new String(text);
    }
}
