package edu.gatech.movierecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void buttonOnClick(View v) {
        EditText usernameBox = (EditText) findViewById(R.id.usernameTextBox);
        EditText passwordBox = (EditText) findViewById(R.id.passwordTextBox);
        String username = usernameBox.getText().toString();
        String password = passwordBox.getText().toString();

        if (World.accountHash.containsKey(username) &&
                World.accountHash.get(username).getPassword().equals(password)) {
            World.currentUser = World.accountHash.get(username);
            Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(dashboardIntent);
        } else {
            Toast.makeText(this, "Incorrect username or password.", Toast.LENGTH_LONG).show();
            usernameBox.setText("");
            passwordBox.setText("");
        }
    }

}
