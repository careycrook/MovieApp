package edu.gatech.movierecommender;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static edu.gatech.movierecommender.UserDBHelper.checkIfInDB;

public class RegisterActivity extends AppCompatActivity {

    /**
     * Runs on inception of activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    /**
     * Handles user creation.
     *
     * @param  v  View for layout
     */
    public void buttonOnClick(View v) {

        //Get references to controls.
        EditText nameBox = (EditText) findViewById(R.id.nameRegisterTextBox);
        EditText emailBox = (EditText) findViewById(R.id.emailRegisterTextBox);
        EditText usernameBox = (EditText) findViewById(R.id.usernameRegisterTextBox);
        EditText passwordBox = (EditText) findViewById(R.id.passwordRegisterTextBox);
        EditText passwordBox2 = (EditText) findViewById(R.id.password2RegisterTextBox);

        //Get strings from controls.
        String name = nameBox.getText().toString();
        String email = emailBox.getText().toString();
        String username = usernameBox.getText().toString();
        String password1 = passwordBox.getText().toString();
        String password2 = passwordBox2.getText().toString();

        //Create user from control data.
        User u = new User(name, email, username, password1);

        //Check that passwords match.
        if (!password1.equals(password2)) {
            Toast.makeText(this, "Passwords don't match.", Toast.LENGTH_LONG).show();
            passwordBox.setText("");
            passwordBox2.setText("");
            return;
        }

        //Check that no field was left blank.
        if (username.equals("") || password1.equals("") || password2.equals("")
                || name.equals("") || email.equals("")) {
            Toast.makeText(this, "You cannot have a blank field.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (checkIfInDB("users", "username", username)) {
            Toast.makeText(this, "A user by that name already exists", Toast.LENGTH_LONG).show();
            return;
        } else {
            ContentValues userInfo = new ContentValues();
            userInfo.put("name", name);
            userInfo.put("email", email);
            userInfo.put("username", username);
            userInfo.put("password", password1.hashCode());
            userInfo.put("status", "Active");
            userInfo.put("major", "NONE");
            userInfo.put("description", "NONE");

            long check = World.DB.insert("users", null, userInfo);

            if (check != 0) {
                Toast.makeText(this, "Account registered!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Account registration failed!", Toast.LENGTH_LONG).show();
            }
        }

        //Set active user.
        World.currentUser = u;

        //Launch dashboard.
        Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(dashboardIntent);
    }

}
