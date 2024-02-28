package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"Duplicates"})

public class Series extends Production {
    private int releaseYear;
    private int numberOfSeasons;
    private Map<String, List<Episode>> seasons;

    public Series(String title, List<String> directors, List<String> cast, List<Genre> genres, String description, List<Rating> ratings,
                  double averageRating, int releaseYear, int numberOfSeasons, Map<String, List<Episode>> seasons) {
        super(title, directors, cast, genres, description, ratings, averageRating);
        this.releaseYear = releaseYear;
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public Series(String title, List<String> directors, List<String> cast, List<Genre> genres, String description, int releaseYear) {
        super(title, directors, cast, genres, description, new ArrayList<>(), 0.0);
        this.releaseYear = releaseYear;
        this.numberOfSeasons = 0;
        this.seasons = new HashMap<>();
    }
    public Series(String title) {
        super(title, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, new ArrayList<>(), 0.0);
        numberOfSeasons = 0;
        releaseYear = 0;
        seasons = new HashMap<>();
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
        System.out.println();
        if (getNumberOfSeasons() > 0) {
            System.out.println("Number of seasons:" + getNumberOfSeasons());
            System.out.println("Seasons: ");
            for (Map.Entry<String, List<Episode>> seasonEntry : getSeasons().entrySet()) {
                System.out.println("  " + seasonEntry.getKey() + ":");
                for (Episode episode : seasonEntry.getValue()) {
                    System.out.println("    Episode: " + episode.getName() + ", Duration: " + episode.getDuration() + " minutes");
                }
            }
        }
    }


    // Getters and setters
    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public Map<String, List<Episode>> getSeasons() {
        return seasons;
    }

    public void setSeasons(Map<String, List<Episode>> seasons) {
        this.seasons = seasons;
    }

}
