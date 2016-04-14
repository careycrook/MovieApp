package edu.gatech.movierecommender;

import android.app.Application;

import com.firebase.client.Firebase;

class World extends Application {
    /**
     * Current User
     */
    private static User currentUser = null;
    /**
     * Database
     */
    private static Firebase database = null;

    /**
     * Gets current user
     *
     * @return user
     */
    public static User getCurrentUser() {return currentUser;}

    /**
     * Gets database
     *
     * @return SQLiteDatabase
     */
    public static Firebase getDatabase() {return database;}

    /**
     * Set global current user
     *
     * @param u user to set
     */
    public static void setCurrentUser(User u) {currentUser = u;}

    /**
     * Sets database
     *
     * @param db SQLiteDatabase
     */
    public static void setDatabase(Firebase db) {database = db;}
}
