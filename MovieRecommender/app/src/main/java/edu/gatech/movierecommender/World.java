package edu.gatech.movierecommender;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

class World extends Application {
    /**
     * Current User
     */
    private static User currentUser = null;
    /**
     * Database
     */
    private static SQLiteDatabase database = null;

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
    public static SQLiteDatabase getDatabase() {return database;}

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
    public static void setDatabase(SQLiteDatabase db) {database = db;}
}
