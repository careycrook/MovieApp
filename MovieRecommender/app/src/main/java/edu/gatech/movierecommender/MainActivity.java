package edu.gatech.movierecommender;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {

    /**
     * Runs on inception of activity
     *
     * @param savedInstanceState default arguments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (World.getDatabase() == null) {
            Firebase.setAndroidContext(getApplicationContext());
            World.setDatabase(new Firebase("https://movierecommender.firebaseio.com/"));
            World.setDbHelper(new DBHelper());

            World.getDbHelper().initUserTable();
            World.getDbHelper().initMovieTable();
        }

        
    }

    /**
     * Opens login page when activated.

     * @param  v  View for layout
     */
    @SuppressWarnings("unused")
    public void buttonLoginClick(View v) {
        //Open login activity.
        final Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);

    }

    /**
     * Opens register page when activated.

     * @param  v  View for layout
     */
    @SuppressWarnings("unused")
    public void buttonRegisterClick(View v) {
        //Open register activity.
        final Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(registerIntent);
    }


}
