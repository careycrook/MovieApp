package edu.gatech.movierecommender;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper {
    private static final String ERROR = "ERROR";

    private static Firebase USER_TABLE;
    private static Firebase MOVIE_TABLE;

    public DBHelper() {}

    protected static String getDigest(String password) {
        byte[] key = password.getBytes();
        MessageDigest md = null;
        try {
             md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = md.digest(key);

        StringBuilder sb = new StringBuilder(hash.length * 2);
        for (Byte b : hash) {
            sb.append(String.format("%02X", b & 0xFF));
        }

        return sb.toString();
    }

    public static void initUserTable() {
        USER_TABLE = World.getDatabase().child("users");

        USER_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String name = postSnapshot.child("name").getValue(String.class);
                    String email = postSnapshot.child("email").getValue(String.class);
                    String username = postSnapshot.child("username").getValue(String.class);
                    String passwordHash = postSnapshot.child("passwordHash").getValue(String.class);

                    User tempU = new User(name, email, username, passwordHash);

                    String major = postSnapshot.child("major").getValue(String.class);
                    String description = postSnapshot.child("description").getValue(String.class);
                    String status = postSnapshot.child("status").getValue(String.class);

                    tempU.setStatus(status);

                    if (!"".equals(major) && !"".equals(description)) {
                        tempU.setProfile(new Profile(major, description));
                    }

                    World.getUsersMap().put(username, tempU);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.toString());
            }
        });
    }

    public static void initMovieTable() {
        MOVIE_TABLE = World.getDatabase().child("movies");

        MOVIE_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String title = postSnapshot.child("title").getValue(String.class);
                    float avg = postSnapshot.child("averageRating").getValue(Float.class);
                    String imgURL = postSnapshot.child("imgURL").getValue(String.class);

                    Movie tempM = new Movie(title);

                    tempM.setAverageRating(avg);
                    tempM.setUrl(imgURL);

                    for (DataSnapshot post2Snapshot : postSnapshot.child("ratings").getChildren()) {
                        float rating = post2Snapshot.child("rating").getValue(Float.class);
                        String comment = post2Snapshot.child("comment").getValue(String.class);
                        String poster = post2Snapshot.child("poster").getValue(String.class);

                        Rating r = new Rating(rating, comment, getUser(poster));

                        if (!tempM.getRatings().contains(r)) {
                            tempM.addRating(r);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.toString());
            }
        });
    }

    /**
     * Returns true if a User with that username is contained in the database
     * @param username The User's username
     * @return true if a User with that username is contained in the database
     */
    public static boolean isUser(String username) {
        return World.getUsersMap().containsKey(username);
    }

    /**
     * Adds a user to the user database
     * @param u The User object we want to add
     * @return true if the User was successfully added
     */
    public static boolean addUser(User u) {
        u.setStatus("Active");

        USER_TABLE.child(u.getUsername()).setValue(u.toMap());

        World.getUsersMap().put(u.getUsername(), u);

        return World.getUsersMap().containsKey(u.getUsername());
    }

    /**
     * Adds a new movie with no ratings to the table of movies
     * @param m the Movie object we want to add
     * @return boolean worked
     */
    public static boolean addNewMovie(Movie m) {
        Map<String, String> basicAttribs = new HashMap<String, String>();
        basicAttribs.put("title", m.getTitle());
        basicAttribs.put("averageRating", String.valueOf(m.getAverageRating()));
        basicAttribs.put("imgURL", m.getURL());

        MOVIE_TABLE.child(m.getTitle()).setValue(basicAttribs);

        World.getMoviesMap().put(m.getTitle(), m);

        return World.getMoviesMap().containsKey(m.getTitle());
    }

    /**
     * Returns an ArrayList of all of the Movies in the database
     * @return an ArrayList of all of the Movies in the database
     */
    public static List<Movie> getAllMovies() {
        return new ArrayList<>(World.getMoviesMap().values());
    }

    /**
     * Returns true if a Movie with that title is contained in the database
     * @param title The title of the Movie
     * @return true if a Movie with that title is contained in the database
     */
    public static boolean isMovie(String title) {
        return World.getMoviesMap().containsKey(title);
    }

    /**
     * Returns the Movie with the corresponding given title
     * @param title The title of the Movie
     * @return The Movie object associated with the title
     */
    public static Movie getMovie(String title) {
        return World.getMoviesMap().get(title);
    }

    /**
     * Returns true if the Rating is successfully added for a Movie
     * @param m The Movie we want to add a Rating to
     * @param r The Rating object we want to add
     */
    public static void addRating(Movie m, Rating r) {
        Firebase tempRef = MOVIE_TABLE.child(m.getTitle()).child("ratings");

        tempRef.push().setValue(r.toMap());

        m.addRating(r);

        updateAverageRating(m, r.getRating());
    }

    /**
     * Updates the average rating for a Movie with a new Rating value
     * @param m The Movie we want to update the Rating for
     * @param r The Rating we want to add
     */
    private static void updateAverageRating(Movie m, float r) {
        Map<String, String> basicAttribs = new HashMap<String, String>();
        basicAttribs.put("averageRating", String.valueOf(m.getAverageRating()));

        MOVIE_TABLE.child(m.getTitle()).setValue(basicAttribs);
    }

    /**
     * Returns an ArrayList of all Ratings for a given Movie
     * @param m The Movie we're looking up the Ratings for
     * @return an ArrayList of all Ratings for a given Movie
     */
    private static List<Rating> getAllRatings(Movie m) {
        return m.getRatings();
    }

    /**
     * Get all users in table
     *
     * @return ArrayList<User> all users
     *
     */
    public static List<User> getAllUsers() {
        return new ArrayList<>(World.getUsersMap().values());
    }

    /**
     * Returns the User object associated with a given username
     * @param username The username of the User object we're looking for
     * @return the User object associated with a given username
     */
    private static User getUser(String username) {
        return World.getUsersMap().get(username);
    }

    /**
     * Set a user's status to locked
     *
     * @param user to lock
     */
    public static void lockUser(String user) {
        setStatus(user, "Locked");
    }

    /**
     * Set a user's status to banned
     *
     * @param user to ban
     */
    public static void banUser(String user) {
        setStatus(user, "Banned");
    }

    /**
     * Set a user's major
     *
     * @param user user in question
     * @param major to set
     */
    public static void setMajor(String user, String major) {
        USER_TABLE.child(user).child("major").setValue(major);
    }

    /**
     * Set a user's description
     *
     * @param user user in question
     * @param description to set
     */
    public static void setDescription(String user, String description) {
        USER_TABLE.child(user).child("description").setValue(description);
    }

    /**
     * Set a user's status
     *
     * @param user user in question
     * @param status to set
     */
    public static void setStatus(String user, String status) {
        USER_TABLE.child(user).child("status").setValue(status);
    }

    /**
     * Get a user's description
     *
     * @param user user
     * @return user's email
     */
    public static String getEmail(String user) {
        return World.getUsersMap().get(user).getEmail();
    }

    /**
     * Get a user's name
     *
     * @param user user
     * @return user's name
     */
    public static String getName(String user) {
        return World.getUsersMap().get(user).getName();
    }

    /**
     * Get a user's status
     *
     * @param user user
     * @return user's status
     */
    public static String getStatus(String user) {
        return World.getUsersMap().get(user).getStatus();
    }

    /**
     * Get a user's major
     *
     * @param user user
     * @return user's major
     */
    public static String getMajor(String user) {
        return World.getUsersMap().get(user).getProfile().getMajor();
    }

    /**
     * Get a user's description
     *
     * @param user user
     * @return user's description
     */
    public static String getDescription(String user) {
        return World.getUsersMap().get(user).getProfile().getDesc();
    }

    /**
     * Get a user's password as hash
     *
     * @param user user
     * @return user's password as hash
     */
    public static String getPassHash(String user) {
        return World.getUsersMap().get(user).getPasswordHash();
    }
}
