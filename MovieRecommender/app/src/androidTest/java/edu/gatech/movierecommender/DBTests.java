package edu.gatech.movierecommender;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

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

    @Test
    public void getMovieExists() {
        this.getContext().deleteDatabase("testDB1.db");

        World.setDatabase(this.getContext().openOrCreateDatabase("testDB1.db", Context.MODE_PRIVATE, null));

        World.getDatabase().execSQL("DROP TABLE IF EXISTS movies");

        World.getDatabase().execSQL("CREATE TABLE IF NOT EXISTS movies (_id INTEGER PRIMARY KEY "
                + "AUTOINCREMENT, title TEXT NOT NULL, averageRating REAL NOT NULL DEFAULT '0', " +
                "imgURL TEXT NOT NULL)");

        Movie m = new Movie("The Hunger Games");
        m.setUrl("URL");
        m.setAverageRating(5);
        DBHelper.addNewMovie(m);

        Movie m2 = DBHelper.getMovie("The Hunger Games");

        assertTrue(m2.getTitle().equals(m.getTitle()));
        assertTrue(m2.getURL().equals(m.getURL()));
        assertTrue(m2.getAverageRating() == m.getAverageRating());
    }

    @Test
    public void getMovieNotExists() {
        this.getContext().deleteDatabase("testDB1.db");

        World.setDatabase(this.getContext().openOrCreateDatabase("testDB1.db", Context.MODE_PRIVATE, null));

        World.getDatabase().execSQL("DROP TABLE IF EXISTS movies");

        World.getDatabase().execSQL("CREATE TABLE IF NOT EXISTS movies (_id INTEGER PRIMARY KEY "
                + "AUTOINCREMENT, title TEXT NOT NULL, averageRating REAL NOT NULL DEFAULT '0', " +
                "imgURL TEXT NOT NULL)");

        Movie m2 = DBHelper.getMovie("Up");

        assertTrue(m2 == null);
    }

    @Test(expected = SQLiteException.class)
    public void getMovieInvalidSQL() {
        this.getContext().deleteDatabase("testDB1.db");

        World.setDatabase(this.getContext().openOrCreateDatabase("testDB1.db", Context.MODE_PRIVATE, null));

        World.getDatabase().execSQL("DROP TABLE IF EXISTS movies");

        World.getDatabase().execSQL("CREATE TABLE IF NOT EXISTS movies (_id INTEGER PRIMARY KEY "
                + "AUTOINCREMENT, title TEXT NOT NULL, averageRating REAL NOT NULL DEFAULT '0', " +
                "imgURL TEXT NOT NULL)");

        Movie m = new Movie("The Hunger Games");
        m.setUrl("URL");
        m.setAverageRating(5);
        DBHelper.addNewMovie(m);

        Movie m2 = DBHelper.getMovie(";\'");

        assertTrue(m2 == null);
    }

    @Test
    public void getMovieDBExists() {
        this.getContext().deleteDatabase("testDB1.db");

        World.setDatabase(this.getContext().openOrCreateDatabase("testDB1.db", Context.MODE_PRIVATE, null));

        World.getDatabase().execSQL("DROP TABLE IF EXISTS movies");

        World.getDatabase().execSQL("CREATE TABLE IF NOT EXISTS movies (_id INTEGER PRIMARY KEY "
                + "AUTOINCREMENT, title TEXT NOT NULL, averageRating REAL NOT NULL DEFAULT '0', " +
                "imgURL TEXT NOT NULL)");


        Movie m = new Movie("The Hunger Games");
        m.setUrl("URL");
        m.setAverageRating(5);
        DBHelper.addNewMovie(m);

        Movie m2 = DBHelper.getMovie("The Hunger Games");

        assertTrue(m2.getTitle().equals(m.getTitle()));
        assertTrue(m2.getURL().equals(m.getURL()));
        assertTrue(m2.getAverageRating() == m.getAverageRating());
    }

    @Test
    public void DBNotNull() {
        this.getContext().deleteDatabase("testDB1.db");

        World.setDatabase(this.getContext().openOrCreateDatabase("testDB1.db", Context.MODE_PRIVATE, null));

        assertTrue(World.getDatabase() != null);
    }
}
