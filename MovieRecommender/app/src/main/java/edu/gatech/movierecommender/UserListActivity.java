package edu.gatech.movierecommender;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    private ArrayList<User> arrU;


    /**
     * Runs on inception of activity
     *
     * @param savedInstanceState default argument
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        //Get reference to ListView, set color.
        ListView populate = (ListView) findViewById(R.id.populate);
        populate.setBackgroundColor(Color.BLACK);

        //Fill ArrayList with list items.
        arrU = DBHelper.getAllUsers();
        ArrayList<String> l = new ArrayList<>();
        for (User u : arrU) {
            l.add(u.getUsername() + ": " + u.getStatus());
        }

        //Array Adapter set up.
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, l);
        populate.setAdapter(itemsAdapter);


        //Click listener.
        populate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                User u = arrU.get(position);
                //Launch dialog.
                dialog(u);
            }
        });
    }

    /**
     * Creates and launches dialog.
     *
     * @param u User to edit
     */
    private void dialog(User u) {
        final CharSequence[] items = { "Active", "Locked", "Banned"};
        final User u2 = u;
        //Create dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set user status.");
        //Set items
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Active")) {
                            u2.setStatus("Active");
                            /**
                             * Runs on inception of UserListActivity
                             *
                             * @param savedInstanceState
                             */                  DBHelper.setStatus(u2.getUsername(), "Active");
                        } else if (items[item].equals("Locked")) {
                            u2.setStatus("Locked");
                            DBHelper.setStatus(u2.getUsername(), "Locked");
                        } else if (items[item].equals("Banned")) {
                            u2.setStatus("Banned");
                            DBHelper.setStatus(u2.getUsername(), "Banned");
                        }
                        dialog.dismiss();

                        //Refresh entries
                        ArrayList<String> l = new ArrayList<>();
                        for (User u : arrU) {
                            l.add(u.getUsername() + ": " + u.getStatus());
                        }
                        ListView populate = (ListView) findViewById(R.id.populate);
                        ArrayAdapter<String> itemsAdapter =
                                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, l);
                        populate.setAdapter(itemsAdapter);
                    }
                });
        AlertDialog dialog = builder.create();
        //Show the dialog that was created.
        dialog.show();
    }
}
