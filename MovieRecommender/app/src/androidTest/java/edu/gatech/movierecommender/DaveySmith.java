package edu.gatech.movierecommender;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class DaveySmith extends AndroidTestCase {

    @Before
    public void setUp() {
        this.getContext().deleteDatabase("testDB.db");
        World.setDatabase(this.getContext().openOrCreateDatabase("testDB.db", Context.MODE_PRIVATE,
                null));
        DBHelper.initUserTable();

        World.getDatabase().execSQL("DROP TABLE IF EXISTS movies");

        World.getDatabase().execSQL("CREATE TABLE IF NOT EXISTS movies (_id INTEGER PRIMARY KEY "
                + "AUTOINCREMENT, title TEXT NOT NULL, averageRating REAL NOT NULL DEFAULT '0', " +
                "imgURL TEXT NOT NULL)");

        World.getDatabase().execSQL("DROP TABLE IF EXISTS users");

        World.getDatabase().execSQL("CREATE TABLE IF NOT EXISTS users (_id INTEGER PRIMARY KEY "
                + "AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, username TEXT NOT NULL,"
                + " password INTEGER NOT NULL DEFAULT '0', status TEXT NOT NULL, " +
                "major TEXT NOT NULL, description TEXT NOT NULL)");


        User dave = new User("Davey", "davedababe@gmail.com", "Ellen", "$wag");
        User carey = new User("Carey", "carebear@gmail.com", "seventeen", "thirtyeight");
        User machoMan = new User("Randy Savage", "creamofdacrop@gmail.com", "World", "Champion");

        DBHelper.addUser(dave);
        DBHelper.addUser(carey);
        DBHelper.addUser(machoMan);

        Movie toystory = new Movie("Toy Story");
        Movie toystory2 = new Movie("Toy Story 2");
        Movie toystory3 = new Movie("Toy Story 3");

        DBHelper.addNewMovie(toystory);
        DBHelper.addNewMovie(toystory2);
        DBHelper.addNewMovie(toystory3);
    }

    @Test
    public void checkAvgRating() {
        List<User> users = DBHelper.getAllUsers();
        Rating rate1 = new Rating(1f,"Great Movie. Classic", users.get(0));
        Rating rate2 = new Rating(2f, "Kinda meh", users.get(1));
        Rating rate3 = new Rating(3f, "Wowee", users.get(2));

        DBHelper.addRating(DBHelper.getMovie("Toy Story"), rate1);
        assertTrue(DBHelper.getMovie("Toy Story").getAverageRating() == 1f);

        DBHelper.addRating(DBHelper.getMovie("Toy Story"), rate2);
        assertTrue(DBHelper.getMovie("Toy Story").getAverageRating() == 1.5f);

        DBHelper.addRating(DBHelper.getMovie("Toy Story"), rate3);
        assertTrue(DBHelper.getMovie("Toy Story").getAverageRating() == 2f);
    }

    @Test
    public void checkNegativeRatings() {
        List<User> users = DBHelper.getAllUsers();
        Rating rate1 = new Rating(1f,"Sucks", users.get(0));
        Rating rate2 = new Rating(0f, "Really Sucks", users.get(1));
        Rating rate3 = new Rating(-1f, "Literally Unwatchable", users.get(2));

        DBHelper.addRating(DBHelper.getMovie("Toy Story 2"), rate3);
        assertTrue(DBHelper.getMovie("Toy Story 2").getAverageRating() == -1f);

        DBHelper.addRating(DBHelper.getMovie("Toy Story 2"), rate2);
        assertTrue(DBHelper.getMovie("Toy Story 2").getAverageRating() == -0.5f);

        DBHelper.addRating(DBHelper.getMovie("Toy Story 2"), rate1);
        assertTrue(DBHelper.getMovie("Toy Story 2").getAverageRating() == 0f);
    }

    @Test
    public void checkMaxRating() {
        List<User> users = DBHelper.getAllUsers();
        Rating rateMax = new Rating(Float.MAX_VALUE,"OMG BEST MOVIE EVER MADE", users.get(0));

        DBHelper.addRating(DBHelper.getMovie("Toy Story 3"), rateMax);
        assertTrue(DBHelper.getMovie("Toy Story 3").getAverageRating() == Float.MAX_VALUE);
    }

    @Test
    public void checkMinRating() {
        List<User> users = DBHelper.getAllUsers();
        Rating rateMin = new Rating(Float.MIN_VALUE,"BROKE MY TELEVISION IMMA SUE", users.get(2));

        DBHelper.addRating(DBHelper.getMovie("Toy Story 3"), rateMin);
        assertTrue(DBHelper.getMovie("Toy Story 3").getAverageRating() == Float.MIN_VALUE);
    }

    @Test
    public void checkGiants() {
        List<User> users = DBHelper.getAllUsers();
        Rating rateMax = new Rating(Float.MAX_VALUE,"OMG BEST MOVIE EVER MADE", users.get(0));
        Rating rateMin = new Rating(Float.MIN_VALUE,"BROKE MY TELEVISION IMMA SUE", users.get(2));

        DBHelper.addRating(DBHelper.getMovie("Toy Story 3"), rateMax);
        DBHelper.addRating(DBHelper.getMovie("Toy Story 3"), rateMin);
        assertTrue(DBHelper.getMovie("Toy Story 3").getAverageRating()
                == ((Float.MIN_VALUE)+(Float.MAX_VALUE))/2);

    }
}
