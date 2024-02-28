package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "Duplicates"})

public class Request implements Subject {
    private RequestsTypes type;
    private String creationDateString = null;

    private LocalDateTime creationDate = null;
    private String nameOrTitle = null;
    private String description = null;
    private String creatorUsername = null;
    private String resolverUsername = null;

    private String resolutionDetails = null;
    private List<Observer> observers = new ArrayList<>();

    private RequestStatus status = RequestStatus.ONGOING;

    @JsonCreator
    public Request(@JsonProperty("type") RequestsTypes type,
                   @JsonProperty("description") String description,
                   @JsonProperty("createdDate") String creationDateString,
                   @JsonProperty("username") String creatorUsername,
                   @JsonProperty("to") String resolverUsername,
                   @JsonProperty("actorName") String actorName,
                   @JsonProperty("movieTitle") String movieTitle) {
        this.type = type;
        this.creationDateString = creationDateString;
        convertStringToDateForCreationTime();
        this.description = description;
        this.creatorUsername = creatorUsername;
        this.resolverUsername = resolverUsername;
        this.nameOrTitle = (actorName != null) ? actorName : movieTitle;
    }

    public Request(RequestsTypes type, String nameOrTitle, String description, String creatorUsername, String resolverUsername) {
        this.type = type;
        this.nameOrTitle = nameOrTitle;
        this.description = description;
        this.creationDate = LocalDateTime.now();
        this.creatorUsername = creatorUsername;
        this.resolverUsername = resolverUsername;

    }

    public Request(RequestsTypes type, String description, String creatorUsername, String resolverUsername) {
        this.type = type;
        this.description = description;
        this.creationDate = LocalDateTime.now();
        this.creatorUsername = creatorUsername;
        this.resolverUsername = resolverUsername;

    }

    public static Request buildRequest(RequestsTypes type, String nameOrTitle, String description, String creatorUser) {
        IMDB imdb = IMDB.getInstance();
        if (type == RequestsTypes.DELETE_ACCOUNT || type == RequestsTypes.OTHERS) {
            Request request = new Request(type, description, creatorUser, "ADMIN");
            for (User user : imdb.getUsers()) {
                if (user instanceof Admin) {
                    request.registerObserver(user);
                }
            }
            return request;
        }

        User resolverUser = imdb.findResolverUser(nameOrTitle);

        Request request;
        if (resolverUser != null)
            request = new Request(type, nameOrTitle, description, creatorUser, resolverUser.getUsername());
        else
            request = new Request(type, nameOrTitle, description, creatorUser, "ADMIN TEAM");
        request.registerObserver(resolverUser);
        ((Staff) resolverUser).addRequestToReceivedRequestsList(request);
        return request;
    }

    public void convertStringToDateForCreationTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        this.creationDate = LocalDateTime.parse(this.creationDateString, formatter);
    }

    public void displayInfo() {
        System.out.println("type: " + type);
        System.out.println("createdDate: " + creationDate);
        System.out.println("username: " + creatorUsername);
        System.out.println("to: " + resolverUsername);
        if (nameOrTitle != null && type.equals(RequestsTypes.ACTOR_ISSUE))
            System.out.println("actorName: " + nameOrTitle);
        else if (nameOrTitle != null && type.equals(RequestsTypes.MOVIE_ISSUE))
            System.out.println("movieTitle: " + nameOrTitle);
        System.out.println("description: " + description);
        System.out.println("STATUS : " + getRequestStatusAsString(this));
    }


    public String getRequestStatusAsString(Request request) {
        return switch (request.status) {
            case SOLVED -> "SOLVED";
            case DENIED -> "DENIED";
            case ONGOING -> "ONGOING";
        };
    }

    @Override
    public String toString() {
        return "Request from " + this.getCreatorUsername() + ": " + this.getDescription();
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
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        String fullMessage = message.concat(" - ").concat(formattedDateTime);

        for (Observer observer : observers) {
            if (observer != null)
                observer.update(fullMessage);
        }
    }

    public User<?> getCreatorUser() {
        IMDB imdb = IMDB.getInstance();
        return imdb.searchUser(creatorUsername);
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setResolutionDetails(String resolutionDetails) {
        this.resolutionDetails = resolutionDetails;
    }

    public RequestsTypes getType() {
        return type;
    }

    public void setType(RequestsTypes type) {
        this.type = type;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getNameOrTitle() {
        return nameOrTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public String getResolverUsername() {
        return resolverUsername;
    }

}