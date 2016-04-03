package edu.gatech.movierecommender;

public class User {

    /**
     * Name
     */
    private final String name;
    /**
     * Email
     */
    private final String email;
    /**
     * Username
     */
    private final String username;
    /**
     * Password
     */
    private final String password;
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
     * @param name the person's name
     * @param email the person's email
     * @param username the person's username
     * @param password the person's passwords
     */
    public User(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
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
     * Return the email for this user
     *
     * @return the person's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Return the username for this user
     *
     * @return the person's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return the password for this user
     *
     * @return the person's password
     */
    public String getPassword() {
        return password;
    }

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
     * @param profile profile of user
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
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
     * @param status status of user
     */
    public void setStatus(String status) { this.status = status; }
}
