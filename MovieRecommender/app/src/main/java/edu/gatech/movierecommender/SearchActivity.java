package edu.gatech.movierecommender;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static edu.gatech.movierecommender.DBHelper.getAllMovies;


public class SearchActivity extends AppCompatActivity {

    private final Context c = this;

    /**
     * Runs on inception of activity
     *
     * @param savedInstanceState default argument
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Intent intent = getIntent();
        final boolean isSearch = intent.getBooleanExtra("TYPE", true);
        final ListView populate = (ListView) findViewById(R.id.populate);
        final String query = intent.getStringExtra("QUERY");

        if (isSearch) {
            RequestQueue mRequestQueue;

            int BASE_SIZE = 1024;
            // Instantiate the cache
            Cache cache = new DiskBasedCache(getCacheDir(), BASE_SIZE * BASE_SIZE); // 1MB cap

            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = new BasicNetwork(new HurlStack());

            // Instantiate the RequestQueue with the cache and network.
            mRequestQueue = new RequestQueue(cache, network);

            // Start the queue
            mRequestQueue.start();
            String url = "http://www.omdbapi.com/?s=" + query;

            final ArrayList<String> titleArr = new ArrayList<>();
            final ArrayList<String> imgArr = new ArrayList<>();

            // Formulate the request and handle the response.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                JSONArray s = jsonObj.getJSONArray("Search");
                                ArrayList<String> titles = new ArrayList<>();
                                for (int i = 0; i < s.length(); i++) {
                                    JSONObject c = s.getJSONObject(i);
                                    titles.add(c.getString("Title") + " (" + c.getString("Year").replaceAll(" ", "") + ")");
                                    titleArr.add(c.getString("Title") + " (" + c.getString("Year").replaceAll(" ", "") + ")");
                                    imgArr.add(c.getString("Poster"));
                                }
                                ArrayAdapter<String> itemsAdapter =
                                        new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, titles);
                                populate.setAdapter(itemsAdapter);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });

            //Click listener
            populate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    Intent movieProfile = new Intent(v.getContext(), MovieProfile.class);
                    movieProfile.putExtra("title", titleArr.get(position));
                    movieProfile.putExtra("url", imgArr.get(position));

                    startActivity(movieProfile);
                }
            });
            mRequestQueue.add(stringRequest);

        //If clicked top recommendations
        } else if (("TOP").equals(query)){

            //arrays
            ArrayList<Movie> movieList = getAllMovies();
            Collections.sort(movieList);
            ArrayList<String> arr = new ArrayList<>();
            ArrayList<String> img = new ArrayList<>();
            ArrayList<String> title = new ArrayList<>();

            //Fill arrays
            for (Movie m : movieList) {
                arr.add(m.toString());
                title.add(m.getTitle());
                img.add(m.getURL());
            }

            final ArrayList<String> imgArr = img;
            final ArrayList<String> titles = title;

            //Make adapter for ListView
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, arr);
            populate.setAdapter(itemsAdapter);

            //Click listener
            populate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    Intent movieProfile = new Intent(v.getContext(), MovieProfile.class);
                    movieProfile.putExtra("title", titles.get(position));
                    movieProfile.putExtra("url", imgArr.get(position));

                    startActivity(movieProfile);
                }
            });

            //Major recommendation
        } else {

            //Arrays
            ArrayList<Movie> movieList = getAllMovies();
            ArrayList<Movie> majorMovieList = new ArrayList<>();
            String major = World.getCurrentUser().getProfile().getMajor();

            //Fill arrays
            for (Movie m : movieList) {
                if (m.getMajorRatings().containsKey(major)) {
                    majorMovieList.add(m);
                }
            }

            //Custom sort
            Collections.sort(majorMovieList, new Comparator<Movie>() {
                public int compare(Movie m1, Movie m2) {
                    String compMajor = World.getCurrentUser().getProfile().getMajor();

                    if (m1.getAverageMajorRating(compMajor) > m2.getAverageMajorRating(compMajor)) {
                        return -1;
                    } else if (m1.getAverageMajorRating(compMajor) == m2.getAverageMajorRating(compMajor)) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });

            //Arrays
            ArrayList<String> arr = new ArrayList<>();
            ArrayList<String> img = new ArrayList<>();
            ArrayList<String> title = new ArrayList<>();

            //Fill them
            for (Movie m : majorMovieList) {
                arr.add(m.toString());
                title.add(m.getTitle());
                img.add(m.getURL());
            }

            final ArrayList<String> imgArr = img;
            final ArrayList<String> titles = title;

            //ListView adapter
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, arr);
            populate.setAdapter(itemsAdapter);

            //Click listener
            populate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    Intent movieProfile = new Intent(v.getContext(), MovieProfile.class);
                    movieProfile.putExtra("title", titles.get(position));
                    movieProfile.putExtra("url", imgArr.get(position));

                    startActivity(movieProfile);
                }
            });

        }
    }
}
