package edu.gatech.movierecommender;

public class User {

    private String name;
    private String email;
    private String username;
    private String password;
    private String status;
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
     * Set the name for this user
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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
     * Set the email for this user
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
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
     * Set the username for this user
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
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
     * Set the password for this user
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
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
     * @param profile
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
     * @param status
     */
    public void setStatus(String status) { this.status = status; }
}
