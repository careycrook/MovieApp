package edu.gatech.movierecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static edu.gatech.movierecommender.DBHelper.*;

public class LoginActivity extends AppCompatActivity {

    private int PASSWORD_ATTEMPTS = 0;

    /**
     * Runs on inception of activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Login control.
     *
     * @param  v  View for layout
     */
    public void buttonOnClick(View v) {
        //Get references to all controls and text data.
        EditText usernameBox = (EditText) findViewById(R.id.usernameTextBox);
        EditText passwordBox = (EditText) findViewById(R.id.passwordTextBox);
        String username = usernameBox.getText().toString();
        String password = passwordBox.getText().toString();

        //hard-coded admin log in
        if (username.equals("admin") && password.equals("admin")) {
            User admin = new User("admin", "admin@google.com", "admin", "admin");
            World.currentUser = admin;
            Intent dashboardIntent = new Intent(getApplicationContext(), AdminDashboard.class);
            startActivity(dashboardIntent);
        } else {
            //Access db to see if login is valid
            if (checkIfInDB("users", "username", username)) {
                int ourHash = password.hashCode();
                int theirHash = getPassHash(username);
                String status = getStatus(username);
                //Block and alert banned users on login attempt
                if (status.equals("Banned")) {
                    Toast.makeText(this, "This account has been banned.", Toast.LENGTH_LONG).show();
                    passwordBox.setText("");
                //Block and alert locked users on login attempt
                } else if (status.equals("Locked")) {
                    Toast.makeText(this, "This account is locked until an admin unlocks it.", Toast.LENGTH_LONG).show();
                    passwordBox.setText("");
                //Success case
                } else {
                    //Set currentUser
                    if (ourHash == theirHash) {
                        Toast.makeText(this, "Login successful.", Toast.LENGTH_LONG).show();
                        PASSWORD_ATTEMPTS = 0;
                        World.currentUser = new User(getName(username), getEmail(username), username, password);

                        if (!getMajor(username).equals("NONE")) {
                            World.currentUser.setProfile(new Profile(getMajor(username), getDescription(username)));
                        }

                        Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
                        startActivity(dashboardIntent);
                    //Lock the user
                    } else {
                        if (PASSWORD_ATTEMPTS >= 3) {
                            Toast.makeText(this, "This account is now locked until an admin unlocks it.", Toast.LENGTH_LONG).show();
                            lockUser(username);
                            passwordBox.setText("");
                        } else {
                            Toast.makeText(this, "Incorrect password.", Toast.LENGTH_LONG).show();
                            passwordBox.setText("");
                            PASSWORD_ATTEMPTS++;
                        }
                    }
                }
            //Name not found
            } else {
                Toast.makeText(this, "There is no user by that name.", Toast.LENGTH_LONG).show();
                usernameBox.setText("");
                passwordBox.setText("");
            }
        }
    }

}
