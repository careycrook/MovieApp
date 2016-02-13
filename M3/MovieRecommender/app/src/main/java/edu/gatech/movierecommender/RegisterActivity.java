package edu.gatech.movierecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void buttonOnClick(View v) {

        EditText nameBox = (EditText) findViewById(R.id.nameRegisterTextBox);
        EditText emailBox = (EditText) findViewById(R.id.emailRegisterTextBox);
        EditText usernameBox = (EditText) findViewById(R.id.usernameRegisterTextBox);
        EditText passwordBox = (EditText) findViewById(R.id.passwordRegisterTextBox);
        EditText passwordBox2 = (EditText) findViewById(R.id.password2RegisterTextBox);

        String name = nameBox.getText().toString();
        String email = emailBox.getText().toString();
        String username = usernameBox.getText().toString();
        String password1 = passwordBox.getText().toString();
        String password2 = passwordBox2.getText().toString();

        User u = new User(name, email, username, password1);

        if (!password1.equals(password2)) {
            Toast.makeText(this, "Passwords don't match.", Toast.LENGTH_LONG).show();
            passwordBox.setText("");
            passwordBox2.setText("");
            return;
        }

        if (username.equals("") || password1.equals("") || password2.equals("")
                || name.equals("") || email.equals("")) {
            Toast.makeText(this, "You cannot have a blank field.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        World.accountHash.put(username, u);
        Toast.makeText(this, "Account registered!", Toast.LENGTH_LONG).show();
        World.currentUser = u;
        Intent dashboardIntent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(dashboardIntent);
    }

}
