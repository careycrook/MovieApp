package edu.gatech.movierecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Spinner spinner = (Spinner) findViewById(R.id.majorSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        TextView usernameLabel = (TextView) findViewById(R.id.usernameProfileLabel);
        TextView nameLabel = (TextView) findViewById(R.id.nameProfileLabel);
        TextView emailLabel = (TextView) findViewById(R.id.emailProfileLabel);

        usernameLabel.setText("Username: " + World.currentUser.getUsername());
        nameLabel.setText("Name: " + World.currentUser.getName());
        emailLabel.setText("Email: " + World.currentUser.getEmail());
        EditText et = (EditText) findViewById(R.id.descTextBox);
        et.setText(World.currentUser.getProfile().getDesc());
        spinner.setSelection(adapter.getPosition(World.currentUser.getProfile().getMajor()));
    }

    public void finishProfile(View v) {
        Spinner spinner = (Spinner) findViewById(R.id.majorSpinner);
        World.currentUser.getProfile().setMajor(spinner.getSelectedItem().toString());
        EditText et = (EditText) findViewById(R.id.descTextBox);
        World.currentUser.getProfile().setDesc(et.getText().toString());
        Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(dashboardIntent);
    }

}
