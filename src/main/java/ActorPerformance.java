package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActorPerformance {
    private String title;
    private PerformanceType type;

    @JsonCreator
    public ActorPerformance(@JsonProperty("title") String title,
                            @JsonProperty("type") String typeAsString) {
        this.title = title;
        if (typeAsString.equalsIgnoreCase("Movie")) {
            this.type = PerformanceType.MOVIE;
        } else {
            this.type = PerformanceType.SERIES;
        }
    }

    public ActorPerformance(String title) {
        this.title = title;
    }

    public void displayInfo() {
        System.out.println("Title: " + this.title);
        if (this.type == PerformanceType.MOVIE)
            System.out.println("Type: Movie");
        else
            System.out.println("Type: Series");
    }

    public String getTitle() {
        return title;
    }

    public PerformanceType getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(PerformanceType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return title + " (" + type + ")";
    }

}
