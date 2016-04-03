package edu.gatech.movierecommender;

class Rating {

    private final float rating;
    private final String comment;
    private final User poster;

    /**
     * Contructor
     *
     * @param r the rating
     * @param c the the comment
     * @param p the poster
     */
    public Rating(float r, String c, User p) {
        rating = r;
        comment = c;
        poster = p;
    }

    /**
     * Return the rating for this Rating
     *
     * @return the rating
     */
    public float getRating(){
        return rating;
    }

    /**
     * Return the comment for this Rating file
     *
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Return the poster for this profile
     *
     * @return the poster
     */
    public User getPoster() {
        return poster;
    }

}
