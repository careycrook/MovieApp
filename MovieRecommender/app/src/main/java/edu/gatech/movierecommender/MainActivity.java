package edu.gatech.movierecommender;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

import static edu.gatech.movierecommender.UserDBHelper.initMovieTable;
import static edu.gatech.movierecommender.UserDBHelper.initUserTable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (World.DB == null) {
            World.DB = openOrCreateDatabase("movieapp.db", Context.MODE_PRIVATE, null);

            initUserTable();
            initMovieTable();
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
