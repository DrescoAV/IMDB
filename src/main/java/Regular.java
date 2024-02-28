package org.example;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"Duplicates"})

public class Regular<T extends Comparable<T>> extends User<T> implements RequestsManager {

    IMDB imdb = IMDB.getInstance();
    private List<Request> personalRequests;

    public Regular(String username, AccountType accountType, Information info, Integer experience) {
        super(username, accountType, info, experience);
        personalRequests = new ArrayList<>();
    }

    @Override
    public void createRequest(Request request) {
        imdb.addRequestToDataBase(request);
        personalRequests.add(request);
        request.notifyObservers("New request arrived for you.");
        request.removeAllObservers();
        request.registerObserver(this);
    }

    @Override
    public void removeRequest(Request request) {
        imdb.removeRequestFromDataBase(request);
        personalRequests.remove(request);

        request.removeAllObservers();
        if (request.getType().equals(RequestsTypes.DELETE_ACCOUNT) || request.getType().equals(RequestsTypes.OTHERS)) {
            RequestsHolder.removeRequest(request);
            return;
        }
        String resolverUsername = request.getResolverUsername();
        User<?> resolverUser = null;
        for (User<?> item : imdb.getUsers()) {
            if (item.getUsername().equals(resolverUsername)) {
                resolverUser = item;
                break;
            }
        }
        request.removeAllObservers();
        request.registerObserver(resolverUser);
        request.notifyObservers("A request was deleted by it's creator.");
        assert resolverUser != null;
        ((Staff<?>) resolverUser).removeFromReceivedRequestList(request);

        request.removeAllObservers();
    }

    public boolean displayPersonalRequests() {
        int i = 1;
        if (personalRequests.isEmpty()) {
            System.out.println("You don't have any requests.");
            return false;
        }
        for (Request request : personalRequests) {
            System.out.println("No : " + i);
            request.displayInfo();
            i++;
        }
        return true;
    }

    public List<Request> getPersonalRequests() {
        return personalRequests;
    }

    public Request getPersonalRequestFromIndex(Integer i) {
        return personalRequests.get(i - 1);
    }

    public Integer getPersonalRequestsSize() {
        return personalRequests.size();
    }

    public void rateProduction(Production production, int ratingValue, String comment) {
        production.addOrUpdateRating(this.getUsername(), ratingValue, comment);
        setExperienceStrategy(new ReviewExperienceStrategy());
        updateExperience();
    }

    // Update a rating for a production
    public void updateRatingForProduction(Production production, int newRatingValue, String newComment) {
        production.addOrUpdateRating(this.getUsername(), newRatingValue, newComment);
    }

    public void removeRatingForProduction(Production production) {
        production.removeRating(this.getUsername());
    }

    @Override
    public void updateExperience() {
        if (getExperienceStrategy() != null) {
            setExperience(getExperience() + getExperienceStrategy().calculateExperience());
        }
    }
}
