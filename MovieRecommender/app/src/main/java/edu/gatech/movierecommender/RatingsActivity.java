package edu.gatech.movierecommender;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static edu.gatech.movierecommender.DBHelper.getMovie;
import static edu.gatech.movierecommender.DBHelper.isMovie;

public class RatingsActivity extends AppCompatActivity {

    /**
     * Context
     */
    private final Context c = this;

    /**
     * Runs on inception of activity
     *
     * @param savedInstanceState default argument
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        //Get ListView control
        final ListView lv = (ListView) findViewById(R.id.ratinglist);

        //Only run if this movie has ratings
        if (!isMovie(getIntent().getStringExtra("title"))) {
            return;
        }

        final Movie m = getMovie(getIntent().getStringExtra("title"));

        //Build rating strings and put them in an ArrayList
        final ArrayList<String> arr = new ArrayList<>();
        final List<Rating> ratingArr = m.getRatings();
        for (final Rating r : ratingArr) {
            String build = "";
            build += r.getPoster().getUsername() + " (";
            build += r.getPoster().getProfile().getMajor();
            build += ") " + (int) r.getRating() + "/5: " + r.getComment();
            arr.add(build);
        }

        //Create ArrayAdapter
        final ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, arr);
        lv.setAdapter(itemsAdapter);
    }
}
