package edu.gatech.movierecommender;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (World.DB == null) {
            World.DB = openOrCreateDatabase("movieapp.db", Context.MODE_PRIVATE, null);

            World.DB.execSQL("CREATE TABLE IF NOT EXISTS users (_id INTEGER PRIMARY KEY "
                    + "AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, username TEXT NOT NULL," +
                    " password INTEGER NOT NULL DEFAULT '0', status TEXT NOT NULL, " +
                    "major TEXT NOT NULL, description TEXT NOT NULL)");
        }
    }

    /**
     * Opens login page when activated.

     * @param  v  View for layout
     */
    public void buttonLoginClick(View v) {
        //Open login activity.
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);

    }

    /**
     * Opens register page when activated.

     * @param  v  View for layout
     */
    public void buttonRegisterClick(View v) {
        //Open register activity.
        Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(registerIntent);
    }


}
