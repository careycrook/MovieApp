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
     * @param savedInstanceState default arguments
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
    @SuppressWarnings("unused")
    public void buttonOnClick(View v) {
        //Get references to all controls and text data.
        final EditText usernameBox = (EditText) findViewById(R.id.usernameTextBox);
        final EditText passwordBox = (EditText) findViewById(R.id.passwordTextBox);
        final String username = usernameBox.getText().toString();
        final String password = passwordBox.getText().toString();

        //hard-coded admin log in
        if (("admin").equals(username) && ("admin").equals(password)) {
            World.setCurrentUser(new User("admin", "admin@google.com", "admin", "admin"));
            final Intent dashboardIntent = new Intent(getApplicationContext(), AdminDashboard.class);
            startActivity(dashboardIntent);
        } else {
            //Access db to see if login is valid
            if (checkIfInDB("users", "username", username)) {
                final int ourHash = password.hashCode();
                final int theirHash = getPassHash(username);
                final String status = getStatus(username);
                //Block and alert banned users on login attempt
                switch (status) {
                    case "Banned":
                        Toast.makeText(this, "This account has been banned.", Toast.LENGTH_LONG).show();
                        passwordBox.setText("");
                        //Block and alert locked users on login attempt
                        break;
                    case "Locked":
                        Toast.makeText(this, "This account is locked until an admin unlocks it.", Toast.LENGTH_LONG).show();
                        passwordBox.setText("");
                        //Success case
                        break;
                    default:
                        //Set currentUser
                        if (ourHash == theirHash) {
                            Toast.makeText(this, "Login successful.", Toast.LENGTH_LONG).show();
                            PASSWORD_ATTEMPTS = 0;
                            World.setCurrentUser(new User(getName(username), getEmail(username), username, password));

                            if (!("NONE").equals(getMajor(username))) {
                                World.getCurrentUser().setProfile(new Profile(getMajor(username), getDescription(username)));
                            }

                            final Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(dashboardIntent);
                            //Lock the user
                        } else {
                            final int NUM_ATTEMPTS = 3;
                            if (PASSWORD_ATTEMPTS >= NUM_ATTEMPTS) {
                                Toast.makeText(this, "This account is now locked until an admin unlocks it.", Toast.LENGTH_LONG).show();
                                lockUser(username);
                                passwordBox.setText("");
                            } else {
                                Toast.makeText(this, "Incorrect password.", Toast.LENGTH_LONG).show();
                                passwordBox.setText("");
                                PASSWORD_ATTEMPTS++;
                            }
                        }
                        break;
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
