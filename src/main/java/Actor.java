package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


public class Actor implements Comparable<Actor> {
    @Override
    public int compareTo(Actor other) {
        return this.name.compareTo(other.name);
    }

    private String name;
    private List<ActorPerformance> performances;
    private String biography;

    public Actor() {
        this.performances = new ArrayList<>();
    }

    public Actor(String name, String biography) {
        this.name = name;
        this.biography = biography;
        this.performances = new ArrayList<>();
    }

    @JsonCreator
    public Actor(@JsonProperty("name") String name,
                 @JsonProperty("productions") List<ActorPerformance> performances,
                 @JsonProperty("biography") String biography) {
        this.name = name;
        this.performances = performances;
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ActorPerformance> getPerformances() {
        return performances;
    }

    public void displayPerformances() {
        int index = 1;
        for (ActorPerformance performance : performances) {
            System.out.println(index + ") :");
            performance.displayInfo();
            index++;
        }
    }

    public void addPerformance(ActorPerformance performance) {
        performances.add(performance);
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void displayInfo() {
        System.out.println("Name: " + name);
        System.out.println("Biography: " + biography);
        System.out.println("Performances: ");
        for (ActorPerformance performance : performances)
            System.out.println(performance.getTitle() + " - " + performance.getType());
    }
}
