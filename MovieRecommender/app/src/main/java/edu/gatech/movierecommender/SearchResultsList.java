package edu.gatech.movierecommender;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by daveysmith on 2/21/16.
 */
public class SearchResultsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresults);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String query = intent.getStringExtra("QUERY");
        if(query.equals("New Releases")) {
            // Do nothing right now
        }else if(query.equals("New to DVD")) {
            // Do nothing right now
        } else {
            encodedQuery = query.replaceAll(" ", "+");
            String url = "http://www.omdbapi.com/?s=" + encodedQuery;
        }
    }
}
