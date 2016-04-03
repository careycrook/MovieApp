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
import java.util.List;

import static edu.gatech.movierecommender.DBHelper.getAllMovies;


public class SearchActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_scrolling);

        final Intent intent = getIntent();
        final boolean isSearch = intent.getBooleanExtra("TYPE", true);
        final ListView populate = (ListView) findViewById(R.id.populate);
        final String query = intent.getStringExtra("QUERY");

        if (isSearch) {
            RequestQueue mRequestQueue;

            final int base = 1024;
            // Instantiate the cache
            final Cache cache = new DiskBasedCache(getCacheDir(), base * base); // 1MB cap

            // Set up the network to use HttpURLConnection as the HTTP client.
            final Network network = new BasicNetwork(new HurlStack());

            // Instantiate the RequestQueue with the cache and network.
            mRequestQueue = new RequestQueue(cache, network);

            // Start the queue
            mRequestQueue.start();
            final String url = "http://www.omdbapi.com/?s=" + query;

            final ArrayList<String> titleArr = new ArrayList<>();
            final ArrayList<String> imgArr = new ArrayList<>();

            // Formulate the request and handle the response.
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
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
                                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, titles);
                                populate.setAdapter(itemsAdapter);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });

            //Click listener
            populate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    final Intent movieProfile = new Intent(v.getContext(), MovieProfile.class);
                    movieProfile.putExtra("title", titleArr.get(position));
                    movieProfile.putExtra("url", imgArr.get(position));

                    startActivity(movieProfile);
                }
            });
            mRequestQueue.add(stringRequest);

        //If clicked top recommendations
        } else if (("TOP").equals(query)){

            //arrays
            final List<Movie> movieList = getAllMovies();
            Collections.sort(movieList);
            final ArrayList<String> arr = new ArrayList<>();
            final ArrayList<String> img = new ArrayList<>();
            final ArrayList<String> title = new ArrayList<>();

            //Fill arrays
            for (final Movie m : movieList) {
                arr.add(m.toString());
                title.add(m.getTitle());
                img.add(m.getURL());
            }

            final ArrayList<String> imgArr = img;
            final ArrayList<String> titles = title;

            //Make adapter for ListView
            final ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, arr);
            populate.setAdapter(itemsAdapter);

            //Click listener
            populate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    final Intent movieProfile = new Intent(v.getContext(), MovieProfile.class);
                    movieProfile.putExtra("title", titles.get(position));
                    movieProfile.putExtra("url", imgArr.get(position));

                    startActivity(movieProfile);
                }
            });

            //Major recommendation
        } else {

            //Arrays
            final List<Movie> movieList = getAllMovies();
            final ArrayList<Movie> majorMovieList = new ArrayList<>();
            final String major = World.getCurrentUser().getProfile().getMajor();

            //Fill arrays
            for (final Movie m : movieList) {
                if (m.getMajorRatings().containsKey(major)) {
                    majorMovieList.add(m);
                }
            }

            //Custom sort
            Collections.sort(majorMovieList, new Comparator<Movie>() {
                public int compare(Movie m1, Movie m2) {
                    final String compMajor = World.getCurrentUser().getProfile().getMajor();

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
            final ArrayList<String> arr = new ArrayList<>();
            final ArrayList<String> img = new ArrayList<>();
            final ArrayList<String> title = new ArrayList<>();

            //Fill them
            for (final Movie m : majorMovieList) {
                arr.add(m.toString());
                title.add(m.getTitle());
                img.add(m.getURL());
            }

            final ArrayList<String> imgArr = img;
            final ArrayList<String> titles = title;

            //ListView adapter
            final ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, arr);
            populate.setAdapter(itemsAdapter);

            //Click listener
            populate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    final Intent movieProfile = new Intent(v.getContext(), MovieProfile.class);
                    movieProfile.putExtra("title", titles.get(position));
                    movieProfile.putExtra("url", imgArr.get(position));

                    startActivity(movieProfile);
                }
            });

        }
    }
}
