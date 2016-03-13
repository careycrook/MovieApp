package edu.gatech.movierecommender;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

public class World extends Application {

    public static HashMap<String, Movie> videoHash = new HashMap<>();
    public static User currentUser = null;
    public static SQLiteDatabase DB = null;
}
