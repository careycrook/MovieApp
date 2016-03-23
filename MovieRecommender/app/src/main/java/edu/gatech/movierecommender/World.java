package edu.gatech.movierecommender;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class World extends Application {
    public static User currentUser = null;
    public static SQLiteDatabase DB = null;
}
