package edu.gatech.movierecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import static edu.gatech.movierecommender.UserDBHelper.setDescription;
import static edu.gatech.movierecommender.UserDBHelper.setMajor;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Get spinner and populate it from String resource file.
        Spinner spinner = (Spinner) findViewById(R.id.majorSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Get references to controls.
        TextView usernameLabel = (TextView) findViewById(R.id.usernameProfileLabel);
        TextView nameLabel = (TextView) findViewById(R.id.nameProfileLabel);
        TextView emailLabel = (TextView) findViewById(R.id.emailProfileLabel);
        EditText et = (EditText) findViewById(R.id.descTextBox);

        //Set text in controls
        usernameLabel.setText("Username: " + World.currentUser.getUsername());
        nameLabel.setText("Name: " + World.currentUser.getName());
        emailLabel.setText("Email: " + World.currentUser.getEmail());
        et.setText(World.currentUser.getProfile().getDesc());
        spinner.setSelection(adapter.getPosition(World.currentUser.getProfile().getMajor()));
    }

    /**
     * Saves user profile and exits to dashboard.
     *
     * @param  v  View for layout
     */
    public void finishProfile(View v) {
        //Get references to controls.
        Spinner spinner = (Spinner) findViewById(R.id.majorSpinner);
        EditText et = (EditText) findViewById(R.id.descTextBox);

        //Set values in singleton.
        World.currentUser.getProfile().setMajor(spinner.getSelectedItem().toString());
        World.currentUser.getProfile().setDesc(et.getText().toString());

        setMajor(World.currentUser.getUsername(), spinner.getSelectedItem().toString());
        setDescription(World.currentUser.getUsername(), et.getText().toString());

        //Close profile, reopen dashboard.
        Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(dashboardIntent);
    }

}
