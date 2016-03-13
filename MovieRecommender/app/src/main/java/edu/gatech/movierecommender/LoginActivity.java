package edu.gatech.movierecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static edu.gatech.movierecommender.UserDBHelper.*;

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

        if (username.equals("admin") && password.equals("admin")) {
            User admin = new User("admin", "admin@google.com", "admin", "admin");
            World.currentUser = admin;
            Intent dashboardIntent = new Intent(getApplicationContext(), AdminDashboard.class);
            startActivity(dashboardIntent);
        } else {
            if (checkIfInDB("users", "username", username)) {
                int ourHash = password.hashCode();
                int theirHash = getPassHash(username);
                String status = getStatus(username);

                if (status.equals("Banned")) {
                    Toast.makeText(this, "This account has been banned.", Toast.LENGTH_LONG).show();
                    passwordBox.setText("");
                } else if (status.equals("Locked")) {
                    Toast.makeText(this, "This account is locked until an admin unlocks it.", Toast.LENGTH_LONG).show();
                    passwordBox.setText("");
                } else {
                    if (ourHash == theirHash) {
                        Toast.makeText(this, "Login successful.", Toast.LENGTH_LONG).show();
                        PASSWORD_ATTEMPTS = 0;
                        World.currentUser = new User(getName(username), getEmail(username), username, password);

                        if (!getMajor(username).equals("NONE")) {
                            World.currentUser.setProfile(new Profile(getMajor(username), getDescription(username)));
                        }

                        Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
                        startActivity(dashboardIntent);
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
            } else {
                Toast.makeText(this, "There is no user by that name.", Toast.LENGTH_LONG).show();
                usernameBox.setText("");
                passwordBox.setText("");
            }
        }

        /*//If hash map contains this user/pass combo, log in to dashboard.
        if (World.accountHash.containsKey(username) &&
                World.accountHash.get(username).getPassword().equals(password)) {
            World.currentUser = World.accountHash.get(username);
            Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(dashboardIntent);

        } else if (username.equals("admin") && password.equals("admin")) { //Added a persistent admin account
            User admin = new User("admin", "admin@google.com", "admin", "admin");
            World.currentUser = admin;
            Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(dashboardIntent);
        } else { //Otherwise notify the user of failed attempt and clear fields.
            Toast.makeText(this, "Incorrect username or password.", Toast.LENGTH_LONG).show();
            usernameBox.setText("");
            passwordBox.setText("");
        }*/
    }

}
