package edu.gatech.movierecommender;

import android.app.Application;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

class World extends Application {

    private static User currentUser = null;
    private static Firebase database = null;

    private static Map<String, User> allUsers = new HashMap<>();
    private static Map<String, Movie> allMovies = new HashMap<>();

    public static User getCurrentUser() {return currentUser;}

    public static Firebase getDatabase() {return database;}

    public static Map<String, User> getUsersMap() {return allUsers;}

    public static Map<String, Movie> getMoviesMap() {return allMovies;}

    public static void setCurrentUser(User u) {currentUser = u;}

    public static void setDatabase(Firebase db) {database = db;}
}
