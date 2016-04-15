package edu.gatech.movierecommender;

import android.app.Application;

import com.firebase.client.Firebase;

class World extends Application {

    private static User currentUser = null;
    private static Firebase database = null;
    private static DBHelper dbHelper;

    public static User getCurrentUser() {return currentUser;}

    public static Firebase getDatabase() {return database;}

    public static DBHelper getDbHelper() {return dbHelper;}

    public static void setCurrentUser(User u) {currentUser = u;}

    public static void setDatabase(Firebase db) {database = db;}

    public static void setDbHelper(DBHelper dbHelperr) {dbHelper = dbHelperr;}
}
