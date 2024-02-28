package org.example;

@SuppressWarnings({"Duplicates"})

public class Rating {
    private String username;
    private int ratingValue;
    private String comment;

    public Rating(String username, int ratingValue, String comment) {
        this.username = username;
        this.ratingValue = ratingValue;
        this.comment = comment;
    }

    public Integer getUserExperience() {
        User<?> user = IMDB.getInstance().searchUser(this.username);
        if (user != null) {
            return user.getExperience();
        } else {
            return 0;
        }
    }

    public String getUsername() {
        return username;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public String getComment() {
        return comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
