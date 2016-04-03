package edu.gatech.movierecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import static edu.gatech.movierecommender.DBHelper.setDescription;
import static edu.gatech.movierecommender.DBHelper.setMajor;

public class ProfileActivity extends AppCompatActivity {

    /**
     * Runs on inception of activity
     *
     * @param savedInstanceState default argument
     */
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
        usernameLabel.setText(getString(R.string.usernameLabel, World.getCurrentUser().getUsername()));
        nameLabel.setText(getString(R.string.nameLabel, World.getCurrentUser().getName()));
        emailLabel.setText(getString(R.string.emailLabel, World.getCurrentUser().getEmail()));
        et.setText(World.getCurrentUser().getProfile().getDesc());
        spinner.setSelection(adapter.getPosition(World.getCurrentUser().getProfile().getMajor()));
    }

    /**
     * Saves user profile and exits to dashboard.
     *
     * @param  v  View for layout
     */
    @SuppressWarnings("unused")
    public void finishProfile(View v) {
        //Get references to controls.
        Spinner spinner = (Spinner) findViewById(R.id.majorSpinner);
        EditText et = (EditText) findViewById(R.id.descTextBox);

        //Set values in singleton.
        World.getCurrentUser().getProfile().setMajor(spinner.getSelectedItem().toString());
        World.getCurrentUser().getProfile().setDesc(et.getText().toString());

        setMajor(World.getCurrentUser().getUsername(), spinner.getSelectedItem().toString());
        setDescription(World.getCurrentUser().getUsername(), et.getText().toString());

        //Close profile, reopen dashboard.
        Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(dashboardIntent);
    }

}
