package edu.gatech.movierecommender;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

class DBHelper {

    // Increment database version when updating schema
    public static final int DATABASE_VERSION = 36;
    public static final String DATABASE_NAME = "datab.db";

    /**
     * Create table for users
     */
    public static void initUserTable() {
        World.DB.execSQL("CREATE TABLE IF NOT EXISTS users (_id INTEGER PRIMARY KEY "
                + "AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, username TEXT NOT NULL," +
                " password INTEGER NOT NULL DEFAULT '0', status TEXT NOT NULL, " +
                "major TEXT NOT NULL, description TEXT NOT NULL)");
    }

    /**
     * Adds a user to the user database
     * @param u The User object we want to add
     * @return true if the User was successfully added
     */
    public static boolean addUser(User u) {
        ContentValues userInfo = new ContentValues();
        userInfo.put("name", u.getName());
        userInfo.put("email", u.getEmail());
        userInfo.put("username", u.getUsername());
        userInfo.put("password", u.getPassword().hashCode());
        userInfo.put("status", "Active");
        userInfo.put("major", "NONE");
        userInfo.put("description", "NONE");

        long check = World.DB.insert("users", null, userInfo);

        return (check != 0);
    }

    /**
     * Create table for movies
     */
    public static void initMovieTable() {
        World.DB.execSQL("CREATE TABLE IF NOT EXISTS movies (_id INTEGER PRIMARY KEY "
        + "AUTOINCREMENT, title TEXT NOT NULL, averageRating REAL NOT NULL DEFAULT '0', " +
                "imgURL TEXT NOT NULL)");
    }

    /**
     * Adds a new movie with no ratings to the table of movies
     * @param m the Movie object we want to add
     */
    public static boolean addNewMovie(Movie m) {
        ContentValues movieInfo = new ContentValues();
        movieInfo.put("title", m.getTitle());
        movieInfo.put("averageRating", m.getAverageRating());
        movieInfo.put("imgURL", m.getURL());

        long check = World.DB.insert("movies", null, movieInfo);

        return (check != 0);
    }

    /**
     * Returns an ArrayList of all of the Movies in the database
     * @return an ArrayList of all of the Movies in the database
     */
    public static ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> temp = new ArrayList<>();
        String query = "SELECT * FROM movies";

        Cursor cursor = World.DB.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            float averageRating = cursor.getFloat(cursor.getColumnIndex("averageRating"));
            String imgURL = cursor.getString(cursor.getColumnIndex("imgURL"));

            Movie m = new Movie(title);
            m.setUrl(imgURL);
            m.setAverageRating(averageRating);

            ArrayList<Rating> tempR = getAllRatings(m);

            for (Rating r : tempR) {
                m.addRating(r);
            }

            temp.add(m);
        }

        cursor.close();

        return temp;
    }

    /**
     * Returns true if a Movie with that title is contained in the database
     * @param title The title of the Movie
     * @return true if a Movie with that title is contained in the database
     */
    public static boolean isMovie(String title) {
        return checkIfInDB("movies", "title", title);
    }

    /**
     * Returns the Movie with the corresponding given title
     * @param title The title of the Movie
     * @return The Movie object associated with the title
     */
    public static Movie getMovie(String title) {
        Cursor cursor = null;
        Movie m = null;

        String query = "SELECT * FROM movies WHERE title = \'" + title + "\'";

        try {
            cursor = World.DB.rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();

                float averageRating = cursor.getFloat(cursor.getColumnIndex("averageRating"));
                String imgURL = cursor.getString(cursor.getColumnIndex("imgURL"));

                m = new Movie(title);
                m.setUrl(imgURL);
                m.setAverageRating(averageRating);

                ArrayList<Rating> tempR = getAllRatings(m);

                for (Rating r : tempR) {
                    m.addRating(r);
                }
            }
        } catch (SQLiteException e) {
            System.out.println("Fatal DB error");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return m;
    }

    /**
     * Returns true if the Rating is successfully added for a Movie
     * @param m The Movie we want to add a Rating to
     * @param r The Rating object we want to add
     */
    public static void addRating(Movie m, Rating r) {
        String tempTitle = m.getTitle().replaceAll(" ", "_").trim();

        World.DB.execSQL("CREATE TABLE IF NOT EXISTS " + "\'" + tempTitle + "\'"
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, rating REAL NOT NULL DEFAULT '0', " +
                "comment TEXT NOT NULL, user TEXT NOT NULL)");

        ContentValues ratingInfo = new ContentValues();
        ratingInfo.put("rating", r.getRating());
        ratingInfo.put("comment", r.getComment());
        ratingInfo.put("user", r.getPoster().getUsername());

        long check = World.DB.insert("\'" + tempTitle + "\'", null, ratingInfo);

        if (check != 0) {
            updateAverageRating(m, r.getRating());
        }
    }

    /**
     * Updates the average rating for a Movie with a new Rating value
     * @param m The Movie we want to update the Rating for
     * @param r The Rating we want to add
     */
    private static void updateAverageRating(Movie m, float r) {
        Cursor cursor = null;
        float originalRating = 0;

        String query = "SELECT * FROM movies WHERE title = \'" + m.getTitle() + "\'";

        try {
            cursor = World.DB.rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                originalRating = cursor.getFloat(cursor.getColumnIndex("averageRating"));
            }
        } catch (SQLiteException e) {
            System.out.println("Fatal DB error");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        String formattedTitle = m.getTitle().replaceAll(" ", "_").trim();
        query = "SELECT * FROM " + "\'" + formattedTitle + "\'";
        int numRatings = 0;

        try {
            cursor = World.DB.rawQuery(query, null);
            numRatings = cursor.getCount();
        } catch (SQLiteException e) {
            System.out.println("Fatal DB error");
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

        World.DB.execSQL(query);
    }

    /**
     * Returns an ArrayList of all Ratings for a given Movie
     * @param m The Movie we're looking up the Ratings for
     * @return an ArrayList of all Ratings for a given Movie
     */
    private static ArrayList<Rating> getAllRatings(Movie m) {
        ArrayList<Rating> temp = new ArrayList<>();
        String formattedTitle = m.getTitle().replaceAll(" ", "_").trim();
        String query = "SELECT * FROM " + "\'" + formattedTitle + "\'";

        Cursor cursor = World.DB.rawQuery(query, null);

        while (cursor.moveToNext()) {
            float rating = cursor.getFloat(cursor.getColumnIndex("rating"));
            String comment = cursor.getString(cursor.getColumnIndex("comment"));
            String username = cursor.getString(cursor.getColumnIndex("user"));

            User u = getUser(username);

            temp.add(new Rating(rating, comment, u));
        }

            cursor.close();

        return temp;
    }

    /**
     * Get all users in table
     *
     * @return ArrayList<User> all users
     *
     */
    public static ArrayList<User> getAllUsers() {
        ArrayList<User> temp = new ArrayList<>();
        String query = "SELECT * FROM users";

        Cursor cursor = World.DB.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String name;
            String email;
            String username;
            int password;
            String status;

            name = cursor.getString(cursor.getColumnIndex("name"));
            email = cursor.getString(cursor.getColumnIndex("email"));
            username = cursor.getString(cursor.getColumnIndex("username"));
            password = cursor.getInt(cursor.getColumnIndex("password"));
            status = cursor.getString(cursor.getColumnIndex("status"));

            User u = new User(name, email, username, String.valueOf(password));
            u.setStatus(status);

            temp.add(u);
        }

            cursor.close();

        return temp;
    }

    /**
     * Returns the User object associated with a given username
     * @param username The username of the User object we're looking for
     * @return the User object associated with a given username
     */
    private static User getUser(String username) {
        Cursor cursor = null;
        User u = null;

        String query = "SELECT * FROM users WHERE username = \'" + username + "\'";

        try {
            cursor = World.DB.rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                u = new User(cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("email")),
                        cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("password")));
                u.setProfile(new Profile(cursor.getString(cursor.getColumnIndex("major")),
                        cursor.getString(cursor.getColumnIndex("description"))));
            }
        } catch (SQLiteException e) {
            System.out.println("Fatal DB error");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return u;
    }

    /**
     * Check if entry is in DB
     *
     * @return true if in database
     */
    public static boolean checkIfInDB(String tableName, String dbField, String fieldValue) {
        String query = "Select 1 from " + tableName + " where " + dbField + " = \'" + fieldValue + "\' LIMIT 1";
        Cursor cursor = World.DB.rawQuery(query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }
        cursor.close();
        return true;
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
        String query = "UPDATE users SET major = \'" + major + "\' WHERE username = \'" + user + "\'";

        World.DB.execSQL(query);
    }

    /**
     * Set a user's description
     *
     * @param user user in question
     * @param description to set
     */
    public static void setDescription(String user, String description) {
        String query = "UPDATE users SET description = \'" + description + "\' WHERE username = \'" + user + "\'";

        World.DB.execSQL(query);
    }

    /**
     * Set a user's status
     *
     * @param user user in question
     * @param status to set
     */
    public static void setStatus(String user, String status) {
        String query = "UPDATE users SET status = \'" + status + "\' WHERE username = \'" + user + "\'";

        World.DB.execSQL(query);
    }

    /**
     * Get a user's description
     *
     * @return user's email
     */
    public static String getEmail(String user) {
        Cursor cursor = null;
        String email = "";

        String query = "SELECT email FROM users WHERE username = \'" + user + "\'";

        try {
            cursor = World.DB.rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                email = cursor.getString(cursor.getColumnIndex("email"));
            }
        } catch (SQLiteException e) {
            System.out.println("Fatal DB error");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return email;
    }

    /**
     * Get a user's name
     *
     * @return user's name
     */
    public static String getName(String user) {
        Cursor cursor = null;
        String name = "";

        String query = "SELECT name FROM users WHERE username = \'" + user + "\'";

        try {
            cursor = World.DB.rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                name = cursor.getString(cursor.getColumnIndex("name"));
            }
        } catch (SQLiteException e) {
            System.out.println("Fatal DB error");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return name;
    }

    /**
     * Get a user's status
     *
     * @return user's status
     */
    public static String getStatus(String user) {
        Cursor cursor = null;
        String status = "";

        String query = "SELECT status FROM users WHERE username = \'" + user + "\'";

        try {
            cursor = World.DB.rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                status = cursor.getString(cursor.getColumnIndex("status"));
            }
        } catch (SQLiteException e) {
            System.out.println("Fatal DB error");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return status;
    }

    /**
     * Get a user's major
     *
     * @return user's major
     */
    public static String getMajor(String user) {
        Cursor cursor = null;
        String major = "";

        String query = "SELECT major FROM users WHERE username = \'" + user + "\'";

        try {
            cursor = World.DB.rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                major = cursor.getString(cursor.getColumnIndex("major"));
            }
        } catch (SQLiteException e) {
            System.out.println("Fatal DB error");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return major;
    }

    /**
     * Get a user's description
     *
     * @return user's description
     */
    public static String getDescription(String user) {
        Cursor cursor = null;
        String desc = "";

        String query = "SELECT description FROM users WHERE username = \'" + user + "\'";

        try {
            cursor = World.DB.rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                desc = cursor.getString(cursor.getColumnIndex("description"));
            }
        } catch (SQLiteException e) {
            System.out.println("Fatal DB error");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return desc;
    }

    /**
     * Get a user's password as hash
     *
     * @return user's password as hash
     */
    public static int getPassHash(String user) {
        Cursor cursor = null;
        int passHash = 0;

        String query = "SELECT password FROM users WHERE username = \'" + user + "\'";

        try {
            cursor = World.DB.rawQuery(query, null);

            if (cursor.getCount() > 0)  {
                cursor.moveToFirst();
                passHash = cursor.getInt(cursor.getColumnIndex("password"));
            }
        } catch (SQLiteException e) {
            System.out.println("Fatal DB error");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return passHash;
    }
}
