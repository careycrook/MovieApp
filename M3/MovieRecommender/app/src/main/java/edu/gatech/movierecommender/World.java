package edu.gatech.movierecommender;

import android.app.Application;
import java.util.HashMap;

public class World extends Application {

    public static HashMap<String, User> accountHash = new HashMap<>();
    public static User currentUser = null;
}
