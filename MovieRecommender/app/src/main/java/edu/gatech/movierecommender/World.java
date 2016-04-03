package edu.gatech.movierecommender;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

class World extends Application {
    private static User currentUser = null;
    private static SQLiteDatabase database = null;

    public static User getCurrentUser() {return currentUser;}
    public static SQLiteDatabase getDatabase() {return database;}

    public static void setCurrentUser(User u) {currentUser = u;}
    public static void setDatabase(SQLiteDatabase db) {database = db;}
}
