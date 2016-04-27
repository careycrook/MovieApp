package edu.gatech.movierecommender;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static edu.gatech.movierecommender.DBHelper.getDescription;
import static edu.gatech.movierecommender.DBHelper.getEmail;
import static edu.gatech.movierecommender.DBHelper.getMajor;
import static edu.gatech.movierecommender.DBHelper.getName;
import static edu.gatech.movierecommender.DBHelper.getPassHash;
import static edu.gatech.movierecommender.DBHelper.getStatus;
import static edu.gatech.movierecommender.DBHelper.isUser;
import static edu.gatech.movierecommender.DBHelper.lockUser;

public class LoginActivity extends AppCompatActivity {
    /**
     * Total attempts so far
     **/
    private int passwordAttempts = 0;
    /**
     * Admin
     **/
    private static final String ADMIN = "admin";

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
        if ((ADMIN).equals(username) && (ADMIN).equals(password)) {
            World.setCurrentUser(new User(ADMIN, "admin@google.com", ADMIN, ADMIN));
            final Intent dashboardIntent = new Intent(getApplicationContext(), AdminDashboard.class);
            startActivity(dashboardIntent);
        } else {
            //Access db to see if login is valid

            if (isUser(username)) {
                final String ourHash = DBHelper.getDigest(password);
                final String theirHash = getPassHash(username);
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
                        if (ourHash.equals(theirHash)) {
                            Toast.makeText(this, "Login successful.", Toast.LENGTH_LONG).show();
                            passwordAttempts = 0;
                            World.setCurrentUser(World.getUsersMap().get(username));

                            final Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(dashboardIntent);
                            //Lock the user
                        } else {
                            final int attempts = 3;
                            if (passwordAttempts >= attempts) {
                                Toast.makeText(this, "This account is now locked until an admin unlocks it.", Toast.LENGTH_LONG).show();
                                lockUser(username);
                                passwordBox.setText("");
                            } else {
                                Toast.makeText(this, "Incorrect password.", Toast.LENGTH_LONG).show();
                                passwordBox.setText("");
                                passwordAttempts++;
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

    protected class LoginLoadingScreenActivity extends AppCompatActivity {
        private ProgressDialog progressDialog;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            LoadViewTask lv = new LoadViewTask();
            lv.userParams = getIntent().getStringArrayListExtra("userParams");
            lv.execute();
        }

        protected class LoadViewTask extends AsyncTask<List<String>, AtomicBoolean, AtomicBoolean> {

            public List<String> userParams;

            //Before running code in separate thread
            @Override
            protected void onPreExecute()
            {
                //Create a new progress dialog
                progressDialog = new ProgressDialog(LoginLoadingScreenActivity.this);
                //Set the progress dialog to display a horizontal progress bar
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                //Set the dialog title to 'Loading...'
                progressDialog.setTitle("Loading...");
                //Set the dialog message to 'Loading application View, please wait...'
                progressDialog.setMessage("Loading application View, please wait...");
                //This dialog can't be canceled by pressing the back key
                progressDialog.setCancelable(false);
                //This dialog isn't indeterminate
                progressDialog.setIndeterminate(false);
                //The maximum number of items is 100
                progressDialog.setMax(100);
                //Set the current progress to zero
                progressDialog.setProgress(0);
                //Display the progress dialog
                progressDialog.show();
            }

            @Override
            protected AtomicBoolean doInBackground(List<String>... params) {
                String username = userParams.get(0);
                int passHash = Integer.parseInt(userParams.get(1));

                AtomicBoolean returnB = new AtomicBoolean();

                if (isUser(username)) {

                } else {
                    returnB.set(false);
                }

                return returnB;
            }

            //Update the progress
            @Override
            protected void onProgressUpdate(AtomicBoolean... values)
            {
                //set the current progress of the progress dialog
                //progressDialog.setProgress(values[0]);
            }

            //after executing the code in the thread
            @Override
            protected void onPostExecute(AtomicBoolean result)
            {
                //close the progress dialog
                progressDialog.dismiss();
                //initialize the View
            }
        }
    }
}
