package edu.gatech.movierecommender;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by theon on 3/12/2016.
 */
public class UserDBHelper extends SQLiteOpenHelper {

    // Increment database version when updating schema
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movieapp.db";
    public Context context;

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

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

    public static void lockUser(String user) {
        Cursor cursor = null;

        String query = "UPDATE users SET status = \'Locked\' WHERE username = \'" + user + "\'";

        World.DB.execSQL(query);
    }

    public static void setMajor(String user, String major) {
        Cursor cursor = null;

        String query = "UPDATE users SET major = \'" + major + "\' WHERE username = \'" + user + "\'";

        World.DB.execSQL(query);
    }

    public static void setDescription(String user, String description) {
        Cursor cursor = null;

        String query = "UPDATE users SET description = \'" + description + "\' WHERE username = \'" + user + "\'";

        World.DB.execSQL(query);
    }

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

    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                        UserEntry._ID + " INTEGER PRIMARY KEY," +
                        UserEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        UserEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;
    }
}
