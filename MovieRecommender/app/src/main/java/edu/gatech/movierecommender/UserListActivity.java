package edu.gatech.movierecommender;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ListView populate = (ListView) findViewById(R.id.populate);
        //ArrayList<User> arr = UserDBHelper.getAllUsers();
        ArrayList<User> arr = new ArrayList<>();
        arr.add(new User("u","u","u","u"));
        ArrayList<String> l = new ArrayList<>();
        for (User u : arr) {
            l.add(u.getUsername() + ": " + UserDBHelper.getStatus(u.getUsername()));
        }
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, l);
        populate.setAdapter(itemsAdapter);
    }
}
