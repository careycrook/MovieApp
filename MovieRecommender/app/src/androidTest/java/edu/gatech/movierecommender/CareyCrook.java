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

public class CareyCrook extends AndroidTestCase {
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


        User john = new User("John", "john1@gmail.com", "blue", "twelve");
        User alex = new User("Alex", "alex2@gmail.com", "red", "ten");
        User mary = new User("Mary", "mary3@gmail.com", "green", "twenty");

        DBHelper.addUser(john);
        DBHelper.addUser(alex);
        DBHelper.addUser(mary);

        Movie toystory = new Movie("Fight Club");
        Movie toystory2 = new Movie("Finding Nemo");
        Movie toystory3 = new Movie("Bugs Life");

        DBHelper.addNewMovie(toystory);
        DBHelper.addNewMovie(toystory2);
        DBHelper.addNewMovie(toystory3);
    }

    @Test
    public void isInDBtest() {
        boolean first, second, third, fourth, fifth;
        first = DBHelper.checkIfInDB("movies", "TITLE", "Fight Club");
        second = DBHelper.checkIfInDB("movies", "TITLE", "Lord of the Rings");
        third = DBHelper.checkIfInDB("users", "NAME", "John");
        fourth = DBHelper.checkIfInDB("users", "EMAIL", "alex2@gmail.com");
        fifth = DBHelper.checkIfInDB("users", "NAME", "Bob");

        assertTrue(first);
        assertFalse(second);
        assertTrue(third);
        assertTrue(fourth);
        assertFalse(fifth);
    }

    @Test
    public void banAndLockUsers() {
        boolean first, second;
        DBHelper.banUser("John");
        first = DBHelper.getStatus("John") == "Banned";
        DBHelper.lockUser("Alex");
        second = DBHelper.getStatus("Alex") == "Locked";

        assertTrue(first);
        assertTrue(second);
    }

}