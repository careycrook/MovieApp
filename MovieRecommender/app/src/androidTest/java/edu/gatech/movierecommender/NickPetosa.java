package edu.gatech.movierecommender;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class NickPetosa extends AndroidTestCase {

    @Before
    public void setUp() {
        this.getContext().deleteDatabase("testDB1.db");
        World.setDatabase(this.getContext().openOrCreateDatabase("testDB1.db", Context.MODE_PRIVATE, null));
        DBHelper.initUserTable();

        User bob = new User("Bob", "bob@gmail.com", "bwaters", "pass");
        User jane = new User("Jane", "jane@gmail.com", "fluffy", "france");
        User chuck = new User("Chuck", "chuckecheese@gmail.com", "cheese", "pizza");

        DBHelper.addUser(bob);
        DBHelper.addUser(jane);
        DBHelper.addUser(chuck);
    }
    @Test
    public void getAllUsersSize() {
        List<User> arr = DBHelper.getAllUsers();
        assertTrue(arr.size() == 3);
    }

    @Test
    public void getAllUsersContains() {
        List<User> arr = DBHelper.getAllUsers();
        boolean foundBob = false;
        boolean foundJane = false;
        boolean foundChuck = false;
        for (User u : arr) {
            String name = u.getName();
            switch(name){
                case "Bob":
                    foundBob = true;
                    break;
                case "Jane":
                    foundJane = true;
                    break;
                case "Chuck":
                    foundChuck = true;
                    break;
            }
        }
        assertTrue(foundBob);
        assertTrue(foundJane);
        assertTrue(foundChuck);
    }

    @Test
    public void getAllUsersOrder() {
        List<User> arr = DBHelper.getAllUsers();
        boolean bobFirst = arr.get(0).getName().equals("Bob");
        boolean janeSecond = arr.get(1).getName().equals("Jane");
        boolean chuckThird = arr.get(2).getName().equals("Chuck");

        assertTrue(bobFirst && janeSecond && chuckThird);
    }

    @Test
    public void getAllUsersMeta() {
        List<User> arr = DBHelper.getAllUsers();
        for (int i = 0; i < 3; i++) {
            switch(i) {
                case 0:
                    assertTrue(arr.get(i).getName().equals("Bob"));
                    assertTrue(arr.get(i).getEmail().equals("bob@gmail.com"));
                    assertTrue(arr.get(i).getUsername().equals("bwaters"));
                    break;
                case 1:
                    assertTrue(arr.get(i).getName().equals("Jane"));
                    assertTrue(arr.get(i).getEmail().equals("jane@gmail.com"));
                    assertTrue(arr.get(i).getUsername().equals("fluffy"));
                    break;
                case 2:
                    assertTrue(arr.get(i).getName().equals("Chuck"));
                    assertTrue(arr.get(i).getEmail().equals("chuckecheese@gmail.com"));
                    assertTrue(arr.get(i).getUsername().equals("cheese"));
                    break;
            }
        }
    }
}
