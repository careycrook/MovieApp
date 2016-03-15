package edu.gatech.movierecommender;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class UserDBHelper {

    // Increment database version when updating schema
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movieapp.db";

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
     * Create table for movies
     */
    public static void initMovieTable() {
        World.DB.execSQL("CREATE TABLE IF NOT EXISTS movies (_id INTEGER PRIMARY KEY "
        + "AUTOINCREMENT, title TEXT NOT NULL, averageRating REAL NOT NULL DEFAULT '0', " +
                "imgURL TEXT NOT NULL)");
    }

    /**
     * Get all users in table
     *
     * @return ArrayList<User> all users
     *
     */
    public static ArrayList<User> getAllUsers() {
        ArrayList<User> temp = new ArrayList<User>();
        String query = "SELECT * FROM users";

        Cursor cursor = World.DB.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String name = "";
            String email = "";
            String username = "";
            int password = 0;
            String status = "";

            name = cursor.getString(cursor.getColumnIndex("name"));
            email = cursor.getString(cursor.getColumnIndex("email"));
            username = cursor.getString(cursor.getColumnIndex("username"));
            password = cursor.getInt(cursor.getColumnIndex("password"));
            status = cursor.getString(cursor.getColumnIndex("status"));

            User u = new User(name, email, username, String.valueOf(password));
            u.setStatus(status);

            temp.add(u);
        }

        return temp;
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
        String query = "UPDATE users SET status = \'Locked\' WHERE username = \'" + user + "\'";

        World.DB.execSQL(query);
    }

    /**
     * Set a user's status to banned
     *
     * @param user to ban
     */
    public static void banUser(String user) {
        String query = "UPDATE users SET status = \'Banned\' WHERE username = \'" + user + "\'";

        World.DB.execSQL(query);
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
            cursor.close();
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
            cursor.close();
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
            cursor.close();
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
            cursor.close();
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
            cursor.close();
        }

        return desc;
    }

    /**
     * Get a user's password as hash
     *
     * @return user's password as has
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
            cursor.close();
        }

        return passHash;
    }
}
