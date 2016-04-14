package edu.gatech.movierecommender;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
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

final class DBHelper {

    /*** *** DEPRECATED *** ***
     * DB
     */
    //public static final String DATABASE_NAME = "DB.db";

    /**
     * Name
     */
    private static final String NAME = "name";
    /**
     * Email
     */
    private static final String EMAIL = "email";
    /**
     * Username
     */
    private static final String USERNAME = "username";
    /**
     * Password
     */
    private static final String PASSWORD = "password";
    /**
     * Status
     */
    private static final String STATUS = "status";
    /**
     * Major
     */
    private static final String MAJOR = "major";
    /**
     * Description
     */
    private static final String DESCRIPTION = "description";

    /**
     * Title
     */
    private static final String TITLE = "title";
    /**
     * Average Rating
     */
    private static final String AVERAGE_RATING = "averageRating";
    /**
     * Image Url
     */
    private static final String IMG_URL = "imgURL";
    /**
     * Fatal error
     */
    private static final String FATAL_DB_ERROR = "Fatal DB error";
    /**
     * Error
     */
    private static final String ERROR = "ERROR";

    private static Firebase USER_TABLE;

    private static Firebase MOVIE_TABLE;

    /**
     * Constructor
     */
    private DBHelper(){}

    /**
     * Create table for users
     */
    public static void initUserTable() {
        // SQL is deprecated
        /*World.getDatabase().execSQL("CREATE TABLE IF NOT EXISTS users (_id INTEGER PRIMARY KEY "
                + "AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, username TEXT NOT NULL," +
                " password INTEGER NOT NULL DEFAULT '0', status TEXT NOT NULL, " +
                "major TEXT NOT NULL, description TEXT NOT NULL)");*/

        USER_TABLE = World.getDatabase().child("users");
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
        // *** DEPRECATED ***
        /*final ContentValues userInfo = new ContentValues();
        userInfo.put(NAME, u.getName());
        userInfo.put(EMAIL, u.getEmail());
        userInfo.put(USERNAME, u.getUsername());
        userInfo.put(PASSWORD, u.getPassword().hashCode());
        userInfo.put(STATUS, "Active");
        userInfo.put(MAJOR, "NONE");
        userInfo.put(DESCRIPTION, "NONE");

        final long check = World.getDatabase().insert("users", null, userInfo);

        return (check != 0);*/

        final AtomicBoolean result = new AtomicBoolean();

        USER_TABLE.child(u.getUsername()).setValue(u, new Firebase.CompletionListener() {
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
     * Create table for movies
     */
    public static void initMovieTable() {
        // SQL is deprecated
        /*World.getDatabase().execSQL("CREATE TABLE IF NOT EXISTS movies (_id INTEGER PRIMARY KEY "
                + "AUTOINCREMENT, title TEXT NOT NULL, averageRating REAL NOT NULL DEFAULT '0', " +
                "imgURL TEXT NOT NULL)");*/

        MOVIE_TABLE = World.getDatabase().child("movies");
    }

    /**
     * Adds a new movie with no ratings to the table of movies
     * @param m the Movie object we want to add
     * @return boolean worked
     */
    public static boolean addNewMovie(Movie m) {
        // *** DEPRECATED ***
        /*final ContentValues movieInfo = new ContentValues();
        movieInfo.put(TITLE, m.getTitle());
        movieInfo.put(AVERAGE_RATING, m.getAverageRating());
        movieInfo.put(IMG_URL, m.getURL());

        final long check = World.getDatabase().insert("movies", null, movieInfo);

        return (check != 0);*/

        final AtomicBoolean result = new AtomicBoolean();

        MOVIE_TABLE.child(m.getTitle()).setValue(m, new Firebase.CompletionListener() {
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
    /*public static List<Movie> getAllMovies() {
        final ArrayList<Movie> temp = new ArrayList<>();
        final String query = "SELECT * FROM movies";

        final Cursor cursor = World.getDatabase().rawQuery(query, null);

        while (cursor.moveToNext()) {
            final String title = cursor.getString(cursor.getColumnIndex(TITLE));
            final float averageMovieRating = cursor.getFloat(cursor.getColumnIndex(AVERAGE_RATING));
            final String imgURL = cursor.getString(cursor.getColumnIndex(IMG_URL));

            final Movie m = new Movie(title);
            m.setUrl(imgURL);
            m.setAverageRating(averageMovieRating);
            
            final List<Rating> tempR = getAllRatings(m);

            for (final Rating r : tempR) {
                m.addRating(r);
            }

            temp.add(m);
        }

        cursor.close();

        return temp;
    }*/

    /**
     * Returns true if a Movie with that title is contained in the database
     * @param title The title of the Movie
     * @return true if a Movie with that title is contained in the database
     */
    public static boolean isMovie(String title) {
        // *** DEPRECATED ***
        //return checkIfInDB("movies", TITLE, title);

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
        // *** DEPRECATED ***
        /*Cursor cursor = null;
        Movie m = null;

        final String query = "SELECT * FROM movies WHERE title = \'" + title + "\'";

        try {
            cursor = World.getDatabase().rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();

                final float averageMovieRating = cursor.getFloat(cursor.getColumnIndex(AVERAGE_RATING));
                final String imgURL = cursor.getString(cursor.getColumnIndex(IMG_URL));

                m = new Movie(title);
                m.setUrl(imgURL);
                m.setAverageRating(averageMovieRating);

                final List<Rating> tempR = getAllRatings(m);

                for (final Rating r : tempR) {
                    m.addRating(r);
                }
            }
        } catch (SQLiteException e) {
            Log.d(ERROR, FATAL_DB_ERROR);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return m;*/

        final Movie result = new Movie(title);
        final Firebase tempRef = MOVIE_TABLE.child(title);

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Movie tempM = dataSnapshot.getValue(Movie.class);

                result.setAverageRating(tempM.getAverageRating());
                result.setUrl(tempM.getURL());
                result.setRatings(tempM.getRatings());
                result.setMajorRatings(tempM.getMajorRatings());
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
        // *** DEPRECATED ***
        /*final String tempTitle = m.getTitle().replaceAll(" ", "_").trim();

        World.getDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + "\'" + tempTitle + "\'"
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, rating REAL NOT NULL DEFAULT '0', " +
                "comment TEXT NOT NULL, user TEXT NOT NULL)");

        final ContentValues ratingInfo = new ContentValues();
        ratingInfo.put("rating", r.getRating());
        ratingInfo.put("comment", r.getComment());
        ratingInfo.put("user", r.getPoster().getUsername());

        final long check = World.getDatabase().insert("\'" + tempTitle + "\'", null, ratingInfo);

        if (check != 0) {
            updateAverageRating(m, r.getRating());
        }*/

        Firebase tempRef = MOVIE_TABLE.child(m.getTitle());
        Map<Rating> ratings = new HashMap<Rating>();
    }

    /**
     * Updates the average rating for a Movie with a new Rating value
     * @param m The Movie we want to update the Rating for
     * @param r The Rating we want to add
     */
    /*private static void updateAverageRating(Movie m, float r) {
        Cursor cursor = null;
        float originalRating = 0;

        String query = "SELECT * FROM movies WHERE title = \'" + m.getTitle() + "\'";

        try {
            cursor = World.getDatabase().rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                originalRating = cursor.getFloat(cursor.getColumnIndex(AVERAGE_RATING));
            }
        } catch (SQLiteException e) {
            Log.d(ERROR, FATAL_DB_ERROR);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        final String formattedTitle = m.getTitle().replaceAll(" ", "_").trim();
        query = "SELECT * FROM " + "\'" + formattedTitle + "\'";
        int numRatings = 0;

        try {
            cursor = World.getDatabase().rawQuery(query, null);
            numRatings = cursor.getCount();
        } catch (SQLiteException e) {
            Log.d(ERROR, FATAL_DB_ERROR);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        float aggregateRating;
        float newRating = 0;

        if (numRatings != 0) {
            aggregateRating = originalRating * (numRatings - 1);
            newRating = (aggregateRating + r) / numRatings;
        }

        query = "UPDATE movies SET averageRating = " + newRating + " WHERE title = \'" + m.getTitle() + "\'";

        World.getDatabase().execSQL(query);
    }*/

    /**
     * Returns an ArrayList of all Ratings for a given Movie
     * @param m The Movie we're looking up the Ratings for
     * @return an ArrayList of all Ratings for a given Movie
     */
    /*private static List<Rating> getAllRatings(Movie m) {
        final ArrayList<Rating> temp = new ArrayList<>();
        final String formattedTitle = m.getTitle().replaceAll(" ", "_").trim();
        final String query = "SELECT * FROM " + "\'" + formattedTitle + "\'";

        final Cursor cursor = World.getDatabase().rawQuery(query, null);

        while (cursor.moveToNext()) {
            final float rating = cursor.getFloat(cursor.getColumnIndex("rating"));
            final String comment = cursor.getString(cursor.getColumnIndex("comment"));
            final String username = cursor.getString(cursor.getColumnIndex("user"));

            final User u = getUser(username);

            temp.add(new Rating(rating, comment, u));
        }

        cursor.close();

        return temp;
    }*/

    /**
     * Get all users in table
     *
     * @return ArrayList<User> all users
     *
     */
    public static List<User> getAllUsers() {
        // *** DEPRECATED ***
        /*final ArrayList<User> temp = new ArrayList<>();
        final String query = "SELECT * FROM users";

        final Cursor cursor = World.getDatabase().rawQuery(query, null);

        while (cursor.moveToNext()) {
            String name;
            String email;
            String username;
            int password;
            String status;

            name = cursor.getString(cursor.getColumnIndex(NAME));
            email = cursor.getString(cursor.getColumnIndex(EMAIL));
            username = cursor.getString(cursor.getColumnIndex(USERNAME));
            password = cursor.getInt(cursor.getColumnIndex(PASSWORD));
            status = cursor.getString(cursor.getColumnIndex(STATUS));

            final User u = new User(name, email, username, String.valueOf(password));
            u.setStatus(status);

            temp.add(u);
        }

        cursor.close();

        return temp;*/

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
        // *** DEPRECATED ***
        /*Cursor cursor = null;
        User u = null;

        final String query = "SELECT * FROM users WHERE username = \'" + username + "\'";

        try {
            cursor = World.getDatabase().rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                u = new User(cursor.getString(cursor.getColumnIndex(NAME)),
                        cursor.getString(cursor.getColumnIndex(EMAIL)),
                        cursor.getString(cursor.getColumnIndex(USERNAME)),
                        cursor.getString(cursor.getColumnIndex(PASSWORD)));
                u.setProfile(new Profile(cursor.getString(cursor.getColumnIndex(MAJOR)),
                        cursor.getString(cursor.getColumnIndex(DESCRIPTION))));
            }
        } catch (SQLiteException e) {
            Log.d(ERROR, FATAL_DB_ERROR);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return u;*/

        final User result = new User(null, null, null, 0);
        final Firebase tempRef = USER_TABLE.child(username);

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User tempU = dataSnapshot.getValue(User.class);

                result.setName(tempU.getName());
                result.setEmail(tempU.getEmail());
                result.setUsername(tempU.getUsername());
                result.setPasswordHash(tempU.getPasswordHash());
                result.setStatus(tempU.getStatus());
                result.setProfile(tempU.getProfile());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        return result;
    }

    /*/**
     * Check if entry is in DB
     *
     * @param dbField database
     * @param fieldValue field
     * @param tableName table
     * @return true if in database
     */
    //public static boolean checkIfInDB(String tableName, String dbField, String fieldValue) {
        // *** DEPRECATED ***
        /*final String query = "Select 1 from " + tableName + " where " + dbField + " = \'" + fieldValue + "\' LIMIT 1";
        final Cursor cursor = World.getDatabase().rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

        final AtomicBoolean result = new AtomicBoolean();
        final String tempFV = fieldValue;
        final Firebase tempRef = World.getDatabase().child(tableName).child(dbField);

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                result.set(tempFV.equals(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(ERROR, "The read failed: " + firebaseError.getMessage());
            }
        });

        return result.get();
    }*/

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
        // *** DEPRECATED ***
        /*final String query = "UPDATE users SET major = \'" + major + "\' WHERE username = \'" + user + "\'";

        World.getDatabase().execSQL(query);*/

        USER_TABLE.child(user).child("major").setValue(major);
    }

    /**
     * Set a user's description
     *
     * @param user user in question
     * @param description to set
     */
    public static void setDescription(String user, String description) {
        // *** DEPRECATED ***
        /*final String query = "UPDATE users SET description = \'" + description + "\' WHERE username = \'" + user + "\'";

        World.getDatabase().execSQL(query);*/

        USER_TABLE.child(user).child("description").setValue(description);
    }

    /**
     * Set a user's status
     *
     * @param user user in question
     * @param status to set
     */
    public static void setStatus(String user, String status) {
        // *** DEPRECATED ***
        /*final String query = "UPDATE users SET status = \'" + status + "\' WHERE username = \'" + user + "\'";

        World.getDatabase().execSQL(query);*/

        USER_TABLE.child(user).child("status").setValue(status);
    }

    /**
     * Get a user's description
     *
     * @param user user
     * @return user's email
     */
    public static String getEmail(String user) {
        // *** DEPRECATED ***
        /*Cursor cursor = null;
        String email = "";

        final String query = "SELECT email FROM users WHERE username = \'" + user + "\'";

        try {
            cursor = World.getDatabase().rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                email = cursor.getString(cursor.getColumnIndex(EMAIL));
            }
        } catch (SQLiteException e) {
            Log.d(ERROR, FATAL_DB_ERROR);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return email;*/

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
        // *** DEPRECATED ***
        /*Cursor cursor = null;
        String name = "";

        final String query = "SELECT name FROM users WHERE username = \'" + user + "\'";

        try {
            cursor = World.getDatabase().rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                name = cursor.getString(cursor.getColumnIndex(NAME));
            }
        } catch (SQLiteException e) {
            Log.d(ERROR, FATAL_DB_ERROR);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return name;*/

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
        // *** DEPRECATED ***
        /*Cursor cursor = null;
        String status = "";

        final String query = "SELECT status FROM users WHERE username = \'" + user + "\'";

        try {
            cursor = World.getDatabase().rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                status = cursor.getString(cursor.getColumnIndex(STATUS));
            }
        } catch (SQLiteException e) {
            Log.d(ERROR, FATAL_DB_ERROR);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return status;*/

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
        // *** DEPRECATED ***
        /*Cursor cursor = null;
        String major = "";

        final String query = "SELECT major FROM users WHERE username = \'" + user + "\'";

        try {
            cursor = World.getDatabase().rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                major = cursor.getString(cursor.getColumnIndex(MAJOR));
            }
        } catch (SQLiteException e) {
            Log.d(ERROR, FATAL_DB_ERROR);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return major;*/

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
        // *** DEPRECATED ***
        /*Cursor cursor = null;
        String desc = "";

        final String query = "SELECT description FROM users WHERE username = \'" + user + "\'";

        try {
            cursor = World.getDatabase().rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                desc = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
            }
        } catch (SQLiteException e) {
            Log.d(ERROR, FATAL_DB_ERROR);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return desc;*/

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
        // *** DEPRECATED ***
        /*Cursor cursor = null;
        int passHash = 0;

        final String query = "SELECT password FROM users WHERE username = \'" + user + "\'";

        try {
            cursor = World.getDatabase().rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                passHash = cursor.getInt(cursor.getColumnIndex(PASSWORD));
            }
        } catch (SQLiteException e) {
            Log.d(ERROR, FATAL_DB_ERROR);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return passHash;*/

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
