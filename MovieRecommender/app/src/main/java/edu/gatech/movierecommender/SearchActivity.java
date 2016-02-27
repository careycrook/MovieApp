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


public class SearchActivity extends AppCompatActivity {

    private Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Intent intent = getIntent();
        final String query = intent.getStringExtra("QUERY");
        final ListView populate = (ListView) findViewById(R.id.populate);
        RequestQueue mRequestQueue;

            // Instantiate the cache
            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = new BasicNetwork(new HurlStack());

            // Instantiate the RequestQueue with the cache and network.
            mRequestQueue = new RequestQueue(cache, network);

            // Start the queue
            mRequestQueue.start();
            String url ="http://www.omdbapi.com/?s=" + query;

            final ArrayList<String> titleArr = new ArrayList<String>();
            final ArrayList<String> imgArr = new ArrayList<String>();

            // Formulate the request and handle the response.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                JSONArray s = jsonObj.getJSONArray("Search");
                                ArrayList<String> titles = new ArrayList<String>();
                                for (int i = 0; i < s.length(); i++) {
                                    JSONObject c = s.getJSONObject(i);
                                    titles.add(c.getString("Title"));
                                    titleArr.add(c.getString("Title"));
                                    imgArr.add(c.getString("Poster"));
                                }
                                ArrayAdapter<String> itemsAdapter =
                                        new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, titles);
                                populate.setAdapter(itemsAdapter);
                                } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                        }
                    });
            // Add the request to the RequestQueue.

        populate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Intent movieProfile = new Intent(v.getContext(), MovieProfile.class);
                movieProfile.putExtra("title", titleArr.get(position));
                movieProfile.putExtra("url", imgArr.get(position));

                startActivity(movieProfile);
            }
        });
            mRequestQueue.add(stringRequest);
    }
}
