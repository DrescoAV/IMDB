package org.example;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"Duplicates"})

public class Contributor<T extends Comparable<T>> extends Staff<T> implements RequestsManager {

    private List<Request> personalRequests;

    public List<Request> getPersonalRequests() {
        return personalRequests;
    }

    public Contributor(String username, AccountType accountType, Information info, Integer experience) {
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

    // Implement method to remove a request
    @Override
    public void removeRequest(Request request) {
        imdb.removeRequestFromDataBase(request);
        personalRequests.remove(request);
        if (request.getType().equals(RequestsTypes.DELETE_ACCOUNT) || request.getType().equals(RequestsTypes.OTHERS)) {
            RequestsHolder.removeRequest(request);
        }
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

    public Request getPersonalRequestFromIndex(Integer i) {
        return personalRequests.get(i - 1);
    }

    public Integer getPersonalRequestsSize() {
        return personalRequests.size();
    }

    @Override
    public void updateExperience() {
        if (getExperienceStrategy() != null) {
            setExperience(getExperience() + getExperienceStrategy().calculateExperience());
        }
    }

}
