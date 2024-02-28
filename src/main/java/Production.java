package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"Duplicates"})

public abstract class Production implements Comparable<Production>, Subject {
    private String title;
    private List<String> directors;
    private List<String> cast;
    private List<Genre> genres;
    private List<Rating> ratings;
    private String description;
    private Double averageRating = 0.0;
    private List<Observer> observers = new ArrayList<>();

    public Production(String title, List<String> directors, List<String> cast, List<Genre> genres, String description, List<Rating> ratings, double averageRating) {
        this.title = title;
        this.directors = directors;
        this.cast = cast;
        this.genres = genres;
        this.description = description;
        this.ratings = ratings;
        if (this.ratings != null)
            this.ratings.sort(Comparator.comparingInt(Rating::getUserExperience));
        this.averageRating = averageRating;
    }

    public Production(String title) {
        this.title = title;
    }

    public void updateRatingSorting() {
        this.ratings.sort(Comparator.comparingInt(Rating::getUserExperience).reversed());
    }

    public void addCastToList(String actorName) {
        if (actorName == null)
            return;
        cast.add(actorName);
    }

    public boolean addGenreAsStringToList(String genre) {
        return switch (genre.toUpperCase()) {
            case "ACTION" -> {
                genres.add(Genre.ACTION);
                yield true;
            }
            case "ADVENTURE" -> {
                genres.add(Genre.ADVENTURE);
                yield true;
            }
            case "COOKING" -> {
                genres.add(Genre.COOKING);
                yield true;
            }
            case "COMEDY" -> {
                genres.add(Genre.COMEDY);
                yield true;
            }
            case "DRAMA" -> {
                genres.add(Genre.DRAMA);
                yield true;
            }
            case "HORROR" -> {
                genres.add(Genre.HORROR);
                yield true;
            }
            case "SF" -> {
                genres.add(Genre.SF);
                yield true;
            }
            case "FANTASY" -> {
                genres.add(Genre.FANTASY);
                yield true;
            }
            case "ROMANCE" -> {
                genres.add(Genre.ROMANCE);
                yield true;
            }
            case "MYSTERY" -> {
                genres.add(Genre.MYSTERY);
                yield true;
            }
            case "THRILLER" -> {
                genres.add(Genre.THRILLER);
                yield true;
            }
            case "CRIME" -> {
                genres.add(Genre.CRIME);
                yield true;
            }
            case "BIOGRAPHY" -> {
                genres.add(Genre.BIOGRAPHY);
                yield true;
            }
            case "WAR" -> {
                genres.add(Genre.WAR);
                yield true;
            }
            default -> {
                System.out.println("Genre not recognized: " + genre);
                yield false;
            }
        };
    }

    public void addDirectorToList(String director) {
        if (director == null)
            return;
        directors.add(director);
    }

    public void displayGenres() {
        int index = 0;
        for (Genre genre : genres) {
            System.out.println(index + ") " + genre);
        }
    }

    @Override
    public int compareTo(Production other) {
        return this.title.compareTo(other.title);
    }

    public abstract void displayInfo();

    public String getTitle() {
        return title;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getCast() {
        return cast;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getGenresString() {
        if (genres != null && !genres.isEmpty()) {
            return genres.stream()
                    .map(Genre::toString)
                    .collect(Collectors.joining(", "));
        } else {
            return "N/A";
        }
    }

    public String getDescription() {
        return description;
    }

    public Double getAverageRating() {
        updateAverageRating();
        return averageRating;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public boolean findActorInCast(String actorName) {
        for (String actor : cast) {
            if (actor.equalsIgnoreCase(actorName))
                return true;
        }
        return false;
    }

    public String getRatingAsString() {
        if (ratings == null) {
            return "No ratings available.";
        }

        StringBuilder ratingAsStringBuilder = new StringBuilder();
        for (Rating rating : ratings) {
            if (rating != null) {
                ratingAsStringBuilder
                        .append("User: ")
                        .append(rating.getUsername())
                        .append(" | Rating: ")
                        .append(rating.getRatingValue())
                        .append(" | Comment: ")
                        .append(rating.getComment())
                        .append("\n");
            }
        }
        return ratingAsStringBuilder.toString();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    private void updateAverageRating() {
        if (!ratings.isEmpty()) {
            double sum = 0.0;
            for (Rating rating : ratings) {
                sum += rating.getRatingValue();
            }
            averageRating = sum / ratings.size();
        }
    }

    public void addOrUpdateRating(String username, int ratingValue, String comment) {
        Rating existingRating = findRatingByUsername(username);
        if (existingRating != null) {
            existingRating.setRatingValue(ratingValue);
            existingRating.setComment(comment);
        } else {
            notifyObservers("New rating has been created for a production(" + title + ") that you rated/contributed to.");
            registerObserver(IMDB.getCurrentUser());

            Rating newRating = new Rating(username, ratingValue, comment);
            ratings.add(newRating);
            registerObserver(IMDB.getCurrentUser());
        }
        updateAverageRating();
    }

    public void removeRating(String username) {
        Rating rating = findRatingByUsername(username);
        if (rating != null) {
            removeObserver(IMDB.getCurrentUser());
            ratings.remove(rating);
            updateAverageRating();
        }
    }

    public Rating findRatingByUsername(String username) {
        for (Rating rating : ratings) {
            if (rating.getUsername().equals(username)) {
                return rating;
            }
        }
        return null;
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void removeAllObservers() {
        for (int i = 0; i < observers.size(); i++)
            observers.remove(i);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void addActorToCast(String name) {
        cast.add(name);
    }

    public void removeActorFromCast(String name) {
        cast.remove(name);
    }

    public void displayCast() {
        int index = 1;
        for (String name : cast) {
            System.out.println(index + ") " + name);
        }
    }

    public void displayDirectors() {
        int index = 1;
        for (String name : directors) {
            System.out.println(index + ") " + name);
        }
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public void setCast(List<String> cast) {
        this.cast = cast;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
