package edu.gatech.movierecommender;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static edu.gatech.movierecommender.DBHelper.addNewMovie;
import static edu.gatech.movierecommender.DBHelper.addRating;
import static edu.gatech.movierecommender.DBHelper.getMovie;
import static edu.gatech.movierecommender.DBHelper.isMovie;

public class MovieProfile extends AppCompatActivity {

    /**
     * Image from movie
     */
    private Bitmap bitmap;
    /**
     * The image in memory
     */
    private ImageView img = null;

    /**
     * Title
     */
    private static final String TITLE = "title";

    /**
     * Runs on inception of activity
     *
     * @param savedInstanceState default argument
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Standard stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_profile);

        //Derive title of movie from extra
        final String title = getIntent().getStringExtra(TITLE);

        //Fill ImageView
        img =  (ImageView) findViewById(R.id.img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        final String imgURL = getIntent().getStringExtra("url");
        new LoadImage().execute(imgURL);

        //Change activity header
        final TextView tv = (TextView) findViewById(R.id.movieTitle);
        tv.setText(title);

    }

    /**
     * Saves rating information to a movie
     *
     * @param  v  View for layout
     */
    @SuppressWarnings("unused")
    public void onPost(View v) {
        //If user doesn't have a profile, fail
        if (World.getCurrentUser().getProfile() == null) {
            Toast.makeText(this, "You need to make an account before you can post.", Toast.LENGTH_LONG).show();
        } else {
            //Get controls
            final RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
            final EditText et = (EditText) findViewById(R.id.commentbox);
            final String title = getIntent().getStringExtra(TITLE);

            //Make rating and movie
            final Rating r = new Rating(rb.getRating(), et.getText().toString(), World.getCurrentUser());
            Movie m;

            if (isMovie(title)) {
                m = getMovie(title);
            } else {
                m = new Movie(title);
                m.setUrl(getIntent().getStringExtra("url"));

                if (!addNewMovie(m)) {
                    Log.d("WARNING", "Fatal DB error");
                }
            }

            m.addRating(r);
            addRating(m, r);

            //Clear controls and notify
            rb.setRating(0);
            et.setText("");
            Toast.makeText(this, "Posted!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Launches RatingsActivity activity
     *
     * @param  v  View for layout
     */
    @SuppressWarnings("unused")
    public void onSeeRatings(View v) {
        final Intent seeRatings = new Intent(getBaseContext(), RatingsActivity.class);
        seeRatings.putExtra(TITLE, getIntent().getStringExtra(TITLE));
        startActivity(seeRatings);
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {

        /**
         * Downloads a bitmap async
         *
         * @param  args  Required by implementation
         * @return the acquired bitmap
         */
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (MalformedURLException e1) {
                Log.e("ERROR", "exception", e1);
            } catch (IOException e2) {
                Log.e("ERROR", "exception", e2);
            }
            return bitmap;
        }

        /**
         * Post completion, set ImageView to have this bitmap
         *
         * @param  image results of download
         */
        protected void onPostExecute(Bitmap image) {

            if(image != null) {
                img.setImageBitmap(image);
            }
        }
    }
}
