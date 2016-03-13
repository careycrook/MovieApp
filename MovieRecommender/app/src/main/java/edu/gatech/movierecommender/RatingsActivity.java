package edu.gatech.movierecommender;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class RatingsActivity extends AppCompatActivity {

    private Context c = this;

    /**
     * Runs on inception of activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        //Get listview control
        ListView lv = (ListView) findViewById(R.id.ratinglist);

        //Only run if this movie has ratings
        Movie m = World.videoHash.get(getIntent().getStringExtra("title"));
        if (m == null) {
            return;
        }
        //Build rating strings and put them in an arraylist
        ArrayList<String> arr = new ArrayList<String>();
        ArrayList<Rating> ratingArr = m.getRatings();
        for (Rating r : ratingArr) {
            String build = "";
            build += r.getPoster().getUsername() + " (";
            build += r.getPoster().getProfile().getMajor();
            build += ") " + (int) r.getRating() + "/5: " + r.getComment();
            arr.add(build);
        }

        //Create ArrayAdapter
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, arr);
        lv.setAdapter(itemsAdapter);
    }
}
