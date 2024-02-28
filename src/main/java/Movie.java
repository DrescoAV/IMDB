package org.example;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"Duplicates"})

public class Movie extends Production {
    private int duration;
    private int releaseYear;

    public Movie(String title, List<String> directors, List<String> cast, List<Genre> genres, String description, List<Rating> ratings,
                 double averageRating, int duration, int releaseYear) {
        super(title, directors, cast, genres, description, ratings, averageRating);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public Movie(String title, List<String> directors, List<String> cast, List<Genre> genres, String description, int duration, int releaseYear) {
        super(title, directors, cast, genres, description, new ArrayList<>(), 0.0);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public Movie(String title) {
        super(title, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, new ArrayList<>(), 0.0);
        duration = 0;
        releaseYear = 0;
    }


    @Override
    public void displayInfo() {
        if (getTitle() != null)
            System.out.println("Title: " + getTitle());
        if (getDirectors() != null)
            System.out.println("Directors: " + getDirectors());
        if (getCast() != null)
            System.out.println("Cast: " + getCast());
        if (getGenres() != null)
            System.out.println("Genres: " + getGenres());
        if (getDescription() != null)
            System.out.println("Description: " + getDescription());
        if (getRatings() != null)
            System.out.println("Ratings: ");
        for (Rating rating : getRatings()) {
            System.out.println("    User: " + rating.getUsername() + ", Rating: " + rating.getRatingValue() + ", Comment: " + rating.getComment());
        }
        if (getAverageRating() > 0.0)
            System.out.println("Average Rating: " + getAverageRating());
        if (getReleaseYear() > 0)
            System.out.printf("Release Year: " + getReleaseYear());
        if (getDuration() > 0)
            System.out.println("Duration: " + getDuration());
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
}
