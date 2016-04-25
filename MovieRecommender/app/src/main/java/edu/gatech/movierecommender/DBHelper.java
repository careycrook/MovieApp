package edu.gatech.movierecommender;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DBHelper {
    private final String NAME = "name";
    private final String EMAIL = "email";
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String STATUS = "status";
    private final String MAJOR = "major";
    private final String DESCRIPTION = "description";
    private final String TITLE = "title";
    private final String AVERAGE_RATING = "averageRating";
    private final String IMG_URL = "imgURL";
    private final String FATAL_DB_ERROR = "Fatal DB error";
    private static final String ERROR = "ERROR";

    private static AtomicBoolean resolved;

    private static Firebase USER_TABLE;
    private static Firebase MOVIE_TABLE;

    public DBHelper() {}

    /**
     * Create table for users
     */
    public static void initUserTable() {
        USER_TABLE = World.getDatabase().child("users");

        USER_TABLE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String name = postSnapshot.child("name").getValue(String.class);
                    String email = postSnapshot.child("email").getValue(String.class);
                    String username = postSnapshot.child("username").getValue(String.class);
                    int passwordHash = postSnapshot.child("passwordHash").getValue(Integer.class);

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

    /**
     * Create table for movies
     */
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
        final AtomicBoolean result = new AtomicBoolean();
        final Firebase tempRef = USER_TABLE.child(username);
        final String fusername = username;

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                result.set(dataSnapshot.getValue() != null
                    && fusername.equals(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        return result.get();
    }

    /**
     * Adds a user to the user database
     * @param u The User object we want to add
     * @return true if the User was successfully added
     */
    public static boolean addUser(User u) {
        final AtomicBoolean result = new AtomicBoolean();
        u.setStatus("Active");

        USER_TABLE.child(u.getUsername()).setValue(u.toMap(), new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.e(ERROR, "Data could not be saved. " + firebaseError.getMessage());
                    result.set(false);
                } else {
                    result.set(true);
                }
            }
        });

        return result.get();
    }

    /**
     * Adds a new movie with no ratings to the table of movies
     * @param m the Movie object we want to add
     * @return boolean worked
     */
    public static boolean addNewMovie(Movie m) {
        final AtomicBoolean result = new AtomicBoolean();
        Map<String, String> basicAttribs = new HashMap<String, String>();
        basicAttribs.put("title", m.getTitle());
        basicAttribs.put("averageRating", String.valueOf(m.getAverageRating()));
        basicAttribs.put("imgURL", m.getURL());

        MOVIE_TABLE.child(m.getTitle()).setValue(basicAttribs, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.e(ERROR, "Data could not be saved. " + firebaseError.getMessage());
                    result.set(false);
                } else {
                    result.set(true);
                }
            }
        });

        return result.get();
    }

    /**
     * Returns an ArrayList of all of the Movies in the database
     * @return an ArrayList of all of the Movies in the database
     */
    public static List<Movie> getAllMovies() {
        final ArrayList<Movie> temp = new ArrayList<>();

        MOVIE_TABLE.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Movie m = new Movie(postSnapshot.child("title").getValue(String.class));
                    m.setAverageRating(postSnapshot.child("averageRating").getValue(Float.class));
                    m.setUrl(postSnapshot.child("imgURL").getValue(String.class));

                    List<Rating> tempR = getAllRatings(m);

                    for (final Rating r : tempR) {
                        m.addRating(r);
                    }

                    temp.add(m);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        for (Movie m : temp) {
            System.out.println(m);
        }

        return temp;
    }

    /**
     * Returns true if a Movie with that title is contained in the database
     * @param title The title of the Movie
     * @return true if a Movie with that title is contained in the database
     */
    public static boolean isMovie(String title) {
        final AtomicBoolean result = new AtomicBoolean();
        final Firebase tempRef = MOVIE_TABLE.child(title);
        final String ftitle = title;

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                result.set(dataSnapshot.getValue() != null
                    && ftitle.equals(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        return result.get();
    }

    /**
     * Returns the Movie with the corresponding given title
     * @param title The title of the Movie
     * @return The Movie object associated with the title
     */
    public static Movie getMovie(String title) {
        final Movie result = new Movie(title);
        final Firebase tempRef = MOVIE_TABLE.child(title);

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> container = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (!postSnapshot.hasChildren()) {
                        container.add(postSnapshot.getValue(String.class));
                    }
                }

                result.setAverageRating(Float.parseFloat(container.get(0)));
                result.setUrl(container.get(1));

                result.setRatings(getAllRatings(result));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        return result;
    }

    /**
     * Returns true if the Rating is successfully added for a Movie
     * @param m The Movie we want to add a Rating to
     * @param r The Rating object we want to add
     */
    public static void addRating(Movie m, Rating r) {
        final AtomicBoolean result = new AtomicBoolean();
        Firebase tempRef = MOVIE_TABLE.child(m.getTitle()).child("ratings");

        tempRef.push().setValue(r.toMap(), new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.e(ERROR, "Data could not be saved. " + firebaseError.getMessage());
                    result.set(false);
                } else {
                    result.set(true);
                }
            }
        });

        if (result.get()) {
            updateAverageRating(m, r.getRating());
        }
    }

    /**
     * Updates the average rating for a Movie with a new Rating value
     * @param m The Movie we want to update the Rating for
     * @param r The Rating we want to add
     */
    private static void updateAverageRating(Movie m, float r) {
        final AtomicInteger oldRating = new AtomicInteger();
        final AtomicLong numRatings = new AtomicLong();
        Firebase tempRef = MOVIE_TABLE.child(m.getTitle()).child("averageRating");

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                oldRating.set(Float.floatToRawIntBits(dataSnapshot.getValue(Float.class)));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        tempRef = MOVIE_TABLE.child(m.getTitle()).child("ratings");

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numRatings.set(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        float aggregateRating = 0;
        float newRating = 0;

        if (numRatings.get() != 0) {
            aggregateRating = Float.intBitsToFloat(oldRating.get()) * (numRatings.get() - 1);
            newRating = (aggregateRating + r) / numRatings.get();
        }

        tempRef = MOVIE_TABLE.child(m.getTitle()).child("averageRating");

        tempRef.setValue(newRating, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.e(ERROR, "Data could not be saved. " + firebaseError.getMessage());
                } else {
                }
            }
        });
    }

    /**
     * Returns an ArrayList of all Ratings for a given Movie
     * @param m The Movie we're looking up the Ratings for
     * @return an ArrayList of all Ratings for a given Movie
     */
    private static List<Rating> getAllRatings(Movie m) {
        final ArrayList<Rating> temp = new ArrayList<>();
        final Firebase tempRef = MOVIE_TABLE.child(m.getTitle()).child("ratings");

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    List<String> holder = new ArrayList<String>();

                    for (DataSnapshot ratingShot : postSnapshot.getChildren()) {
                        holder.add(ratingShot.getValue(String.class));
                    }

                    temp.add(new Rating(Float.parseFloat(holder.get(1)),
                            holder.get(0), getUser(holder.get(2))));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        for (Rating r : temp) {
            System.out.println(r);
        }

        return temp;
    }

    /**
     * Get all users in table
     *
     * @return ArrayList<User> all users
     *
     */
    public static List<User> getAllUsers() {
        final List<User> result = new ArrayList<User>();

        USER_TABLE.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    result.add(postSnapshot.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        return result;
    }

    /**
     * Returns the User object associated with a given username
     * @param username The username of the User object we're looking for
     * @return the User object associated with a given username
     */
    private static User getUser(String username) {
        final User result = new User(null, null, null, 0);
        final Firebase tempRef = USER_TABLE.child(username);

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> container = new ArrayList<String>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    container.add(postSnapshot.getValue(String.class));
                }

                result.setName(container.get(3));
                result.setEmail(container.get(1));
                result.setUsername(container.get(6));
                result.setPasswordHash(Integer.parseInt(container.get(4)));
                result.setStatus(container.get(5));

                if ("".equals(container.get(2)) || "".equals(container.get(0))) {
                    result.setProfile(null);
                } else {
                    result.setProfile(new Profile(container.get(2), container.get(0)));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        return result;
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
        final StringBuffer result = new StringBuffer();
        final Firebase tempRef = USER_TABLE.child(user).child("email");

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                result.append(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        return result.toString();
    }

    /**
     * Get a user's name
     *
     * @param user user
     * @return user's name
     */
    public static String getName(String user) {
        final StringBuffer result = new StringBuffer();
        final Firebase tempRef = USER_TABLE.child(user).child("name");

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                result.append(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        return result.toString();
    }

    /**
     * Get a user's status
     *
     * @param user user
     * @return user's status
     */
    public static String getStatus(String user) {
        final StringBuffer result = new StringBuffer();
        final Firebase tempRef = USER_TABLE.child(user).child("status");

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                result.append(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        return result.toString();
    }

    /**
     * Get a user's major
     *
     * @param user user
     * @return user's major
     */
    public static String getMajor(String user) {
        final StringBuffer result = new StringBuffer();
        final Firebase tempRef = USER_TABLE.child(user).child("major");

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                result.append(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        return result.toString();
    }

    /**
     * Get a user's description
     *
     * @param user user
     * @return user's description
     */
    public static String getDescription(String user) {
        final StringBuffer result = new StringBuffer();
        final Firebase tempRef = USER_TABLE.child(user).child("description");

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                result.append(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        return result.toString();
    }

    /**
     * Get a user's password as hash
     *
     * @param user user
     * @return user's password as hash
     */
    public static int getPassHash(String user) {
        final AtomicInteger result = new AtomicInteger();
        final Firebase tempRef = USER_TABLE.child(user).child("passwordHash");

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                result.set(dataSnapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        return result.get();
    }
}
