package edu.gatech.movierecommender;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

class World extends Application {
    private static User currentUser = null;
    private static SQLiteDatabase DB = null;

    public static User getCurrentUser() {return currentUser;}
    public static SQLiteDatabase getDB() {return DB;}

    public static void setCurrentUser(User u) {currentUser = u;}
    public static void setDB(SQLiteDatabase db) {DB = db;}
}
