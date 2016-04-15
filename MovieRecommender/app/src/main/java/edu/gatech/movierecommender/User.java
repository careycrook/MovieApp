package edu.gatech.movierecommender;

import java.util.HashMap;
import java.util.Map;

public class User {

    /**
     * Name
     */
    private String name;
    /**
     * Email
     */
    private String email;
    /**
     * Username
     */
    private String username;
    /**
     * Password
     */
    private int passwordHash;
    /**
     * Status
     */
    private String status;
    /**
     * Profile
     */
    private Profile profile;

    /**
     * Constructor
     *
     * @param myName the person's name
     * @param myEmail the person's email
     * @param myUsername the person's username
     * @param myPasswordHash the person's passwords
     */
    public User(String myName, String myEmail, String myUsername, int myPasswordHash) {
        this.name = myName;
        this.email = myEmail;
        this.username = myUsername;
        this.passwordHash = myPasswordHash;
        profile = null;
    }

    /**
     * Return the name for this user
     *
     * @return the person's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user to the specified String
     * @param nom The new name
     */
    public void setName(String nom) {this.name = nom;}

    /**
     * Return the email for this user
     *
     * @return the person's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user to the specified String
     * @param email The new email
     */
    public void setEmail(String email) {this.email = email;}

    /**
     * Return the username for this user
     *
     * @return the person's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user to the specified String
     * @param username The new username
     */
    public void setUsername(String username) {this.username = username;}

    /**
     * Return the password for this user
     *
     * @return the person's password
     */
    public int getPasswordHash() {
        return passwordHash;
    }

    /**
     * Set the password hash to the specified int
     * @param passwordHash the new hash
     */
    public void setPasswordHash(int passwordHash) {this.passwordHash = passwordHash;}

    /**
     * Return the profile for this user
     *
     * @return the person's name
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Set the profile for this user
     *
     * @param myProfile profile of user
     */
    public void setProfile(Profile myProfile) {
        profile = myProfile;
    }

    /**
     * Return the status for this user
     *
     * @return the person's status
     */
    public String getStatus() { return status; }

    /**
     * Set the status for this user
     *
     * @param myStatus status of user
     */
    public void setStatus(String myStatus) { status = myStatus; }

    public Map<String, String> toMap() {
        Map<String, String> toMap = new HashMap<String, String>();

        toMap.put("name", name);
        toMap.put("email", email);
        toMap.put("username", username);
        toMap.put("passwordHash", String.valueOf(passwordHash));
        toMap.put("status", status);

        if (profile == null) {
            toMap.put("major", "");
            toMap.put("description", "");
        } else {
            toMap.put("major", profile.getMajor());
            toMap.put("description", profile.getDesc());
        }

        return toMap;
    }
}
