package org.example;

import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})

public abstract class Staff<T extends Comparable<T>> extends User<T> implements StaffInterface {
    private List<Request> receivedRequests;
    private SortedSet<T> personalContributions;

    IMDB imdb = IMDB.getInstance();

    public Staff(String username, AccountType accountType, Information info, Integer experience) {
        super(username, accountType, info, experience);
        this.personalContributions = new TreeSet<>(new SortedComparator());
        this.receivedRequests = new ArrayList<>();
    }

    public void removeFromReceivedRequestList(Request request) {
        if (request == null)
            return;
        receivedRequests.remove(request);
    }

    public boolean displayReceivedRequests() {
        if (receivedRequests.isEmpty()) {
            System.out.println("You currently don't have any requests to take care of.");
            return false;
        }
        System.out.println("There are your received requests: ");
        for (Request request : receivedRequests) {
            System.out.println("Request no: " + (receivedRequests.indexOf(request) + 1));
            request.displayInfo();
            System.out.println();
        }
        return true;
    }

    public Request getReceivedRequestWithIndex(Integer index) {
        return receivedRequests.get(index);
    }

    public void addRequestToReceivedRequestsList(Request request) {
        if (request == null)
            return;
        receivedRequests.add(request);
    }

    public boolean displayContributionsSimple() {
        if (personalContributions.isEmpty()) {
            System.out.println("You don't have any contributions!");
            return false;
        }

        System.out.println("Contribution for user " + super.getUsername() + ":");
        for (Object item : personalContributions) {
            if (item instanceof Movie movie) {
                System.out.printf("Movie - " + movie.getTitle());
            } else if (item instanceof Series series) {
                System.out.printf("Series - " + series.getTitle());
            } else if (item instanceof Actor actor) {
                System.out.printf("Actor - " + actor.getName());
            } else {
                System.out.println(item.toString());
            }
        }

        return true;
    }

    public boolean findInContributions(String nameOrTitle) {
        for (Object item : personalContributions) {
            if (item instanceof Movie movie) {
                if (movie.getTitle().equals(nameOrTitle)) {
                    return true;
                }
            } else if (item instanceof Series series) {
                if (series.getTitle().equals(nameOrTitle)) {
                    return true;
                }
            } else if (item instanceof Actor actor) {
                if (actor.getName().equals(nameOrTitle)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addToAddedItems(T item) {
        if (item != null)
            personalContributions.add(item);
    }

    public void removeActorFromAddedItems(Actor a) {
        if (a != null) {
            Iterator<?> iterator = personalContributions.iterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                if (item instanceof Actor actor && actor.getName().equals(a.getName())) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    public void removeProductionFromAddedItems(Production p) {
        if (p != null) {
            Iterator<?> iterator = personalContributions.iterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                if (item instanceof Production && ((Production) item).getTitle().equals(p.getTitle())) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    public boolean searchInAddedItems(String nameOrTitle) {
        for (T item : personalContributions) {
            if (item instanceof Movie && ((Movie) item).getTitle().equals(nameOrTitle)) {
                return true;
            } else if (item instanceof Series && ((Series) item).getTitle().equals(nameOrTitle)) {
                return true;
            } else if (item instanceof Actor && ((Actor) item).getName().equals(nameOrTitle)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void addProductionSystem(Production p) {
        if (p == null) {
            throw new IllegalArgumentException("Production cannot be null");
        }

        if (this instanceof Contributor<T>) {
            setExperienceStrategy(new AddProductionExperienceStrategy());
            updateExperience();
        }

        p.registerObserver(this);

        this.addToAddedItems((T) p);

        List<Production> allProductions = imdb.getProductions();
        if (!allProductions.contains(p)) {
            allProductions.add(p);
            p.registerObserver(this);
        }
    }

    @Override
    public void addActorSystem(Actor a) {
        if (a == null) {
            throw new IllegalArgumentException("Actor cannot be null");
        }

        if (this instanceof Contributor<T>) {
            setExperienceStrategy(new AddProductionExperienceStrategy());
            updateExperience();
        }

        this.addToAddedItems((T) a);

        List<Actor> allActors = imdb.getActors();
        if (!allActors.contains(a)) {
            allActors.add(a);
        }
    }

    @Override
    public void removeProductionSystem(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Production name cannot be null or empty");
        }

        Production toRemove = null;
        for (T item : personalContributions) {
            if (item instanceof Production && ((Production) item).getTitle().equals(name)) {
                toRemove = (Production) item;
                break;
            }
        }

        if (toRemove != null) {
            removeProductionFromAddedItems(toRemove);

            for (User user : imdb.getUsers()) {
                if ((toRemove instanceof Movie && user.searchFavouriteMovie(name) != null) || (toRemove instanceof Series && user.searchFavouriteSeries(name) != null)) {
                    user.removeProductionFromFavourites(toRemove);
                    user.addToFavorites(toRemove);
                }
            }

            imdb.getProductions().remove(toRemove);
        } else {
            throw new IllegalArgumentException("Production not found or not added by user: " + name);
        }
    }


    @Override
    public void removeActorSystem(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Actor name cannot be null or empty");
        }

        Actor toRemove = null;
        for (T item : personalContributions) {
            if (item instanceof Actor && ((Actor) item).getName().equals(name)) {
                toRemove = (Actor) item;
                break;
            }
        }

        if (toRemove != null) {
            removeActorFromAddedItems(toRemove);

            for (User user : imdb.getUsers()) {
                if (user.searchFavouriteActor(name) != null)
                    user.removeActorFromFavourites(toRemove);
            }
            imdb.getActors().remove(toRemove);
        } else {
            throw new IllegalArgumentException("Actor not found or not added by user: " + name);
        }
    }


    @Override
    public void updateProduction(Production p) {
        if (p == null) {
            throw new IllegalArgumentException("Production cannot be null");
        }

        Production toUpdate = null;
        for (T item : personalContributions) {
            if (item instanceof Production && ((Production) item).getTitle().equals(p.getTitle())) {
                toUpdate = (Production) item;
                break;
            }
        }

        if (toUpdate == null) {
            throw new IllegalArgumentException("Production not found or not added by user: " + p.getTitle());
        } else {
            removeProductionFromAddedItems(p);
            addToAddedItems((T) p);

            for (User user : imdb.getUsers()) {
                if ((p instanceof Movie && user.searchFavouriteMovie(p.getTitle()) != null) || (p instanceof Series && user.searchFavouriteSeries(p.getTitle()) != null)) {
                    user.removeProductionFromFavourites(toUpdate);
                    user.addToFavorites(toUpdate);
                }
            }
        }
    }

    @Override
    public void updateActor(Actor a) {
        if (a == null) {
            throw new IllegalArgumentException("Actor cannot be null");
        }

        Actor toUpdate = null;
        for (T item : personalContributions) {
            if (item instanceof Actor && ((Actor) item).getName().equals(a.getName())) {
                toUpdate = (Actor) item;
                break;
            }
        }

        if (toUpdate == null) {
            throw new IllegalArgumentException("Actor not found or not added by user: " + a.getName());
        } else {
            removeActorFromAddedItems(a);
            addToAddedItems((T) a);

            for (User user : imdb.getUsers()) {
                if (user.searchFavouriteActor(a.getName()) != null) {
                    user.removeActorFromFavourites(toUpdate);
                    user.addToFavorites(toUpdate);
                }
            }
        }
    }

    public void resolveRequest(Request request, String resolutionDetails) {
        if (request != null) {
            request.setStatus(RequestStatus.SOLVED);
            request.setResolutionDetails(resolutionDetails);
            request.notifyObservers("Your request has been solved");
            deleteProcessedRequest(request);
            User user = request.getCreatorUser();
            user.setExperienceStrategy(new ResolveRequestExperienceStrategy());
            user.updateExperience();
        }
    }

    public void denyRequest(Request request, String reason) {
        if (request != null) {
            request.setStatus(RequestStatus.DENIED);
            request.setResolutionDetails(reason);
            request.notifyObservers("Your request has been denied");
            deleteProcessedRequest(request);
        }
    }

    private void deleteProcessedRequest(Request request) {
        receivedRequests.remove(request);
        if (RequestsHolder.requestExist(request)) {
            RequestsHolder.removeRequest(request);
        }
    }

    public SortedSet<T> getPersonalContributions() {
        return personalContributions;
    }

    public List<Request> getReceivedRequests() {
        return receivedRequests;
    }

    public boolean hasContribution() {
        return !personalContributions.isEmpty();
    }
}
