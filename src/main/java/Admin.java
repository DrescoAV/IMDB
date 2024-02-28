package org.example;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})

public class Admin<T extends Comparable<T>> extends Staff<T> implements StaffInterface {

    public Admin(String username, AccountType accountType, Information info) {
        super(username, accountType, info, null);
    }

    private static final SortedSet<Object> adminTeamContributions = new TreeSet<>(new SortedComparator());


    public void addUser(User<?> user) {
        imdb.getUsers().add(user);
    }

    public void deleteUser(User<?> user) {

        if (user.equals(this)) {
            throw new IllegalArgumentException("You can not delete yourself!");
        }

        if (user instanceof Staff<?> staff && staff.hasContribution()) {
            for (Object item : staff.getPersonalContributions()) {
                addAdminTeamContribution(item);
            }
        }

        for (Production p : imdb.getProductions()) {
            if (p.findRatingByUsername(user.getUsername()) != null) {
                p.removeRating(user.getUsername());
            }
        }

        if (user instanceof Regular<?> regular) {
            for (Request request : regular.getPersonalRequests()) {
                regular.removeRequest(request);
            }
        }

        if (user instanceof Contributor<?> contributor) {
            for (Request request : contributor.getPersonalRequests()) {
                contributor.removeRequest(request);
            }
        }

        imdb.getUsers().remove(user);
    }

    public static void addAdminTeamContribution(Object item) {
        adminTeamContributions.add(item);
    }

    public static void removeAdminTeamContribution(Object item) {
        adminTeamContributions.remove(item);
    }


    @Override
    public boolean displayContributionsSimple() {
        if (getPersonalContributions().isEmpty()) {
            System.out.println("You don't have any contributions!");
            return false;
        }

        System.out.println("Personal contributions: ");
        for (T item : getPersonalContributions()) {
            if (item instanceof Movie movie) {
                System.out.printf("Movie - " + movie.getTitle());
            } else if (item instanceof Series series) {
                System.out.printf("Series - " + series.getTitle());
            } else if (item instanceof Actor actor) {
                System.out.printf("Actor - " + actor.getName());
            } else {
                System.out.println(item.toString());
            }
            System.out.println();
        }

        System.out.println("Admin team contributions:");
        for (Object item : adminTeamContributions) {
            if (item instanceof Movie movie) {
                System.out.printf("Movie - " + movie.getTitle());
            } else if (item instanceof Series series) {
                System.out.printf("Series - " + series.getTitle());
            } else if (item instanceof Actor actor) {
                System.out.printf("Actor - " + actor.getName());
            } else {
                System.out.println(item.toString());
            }
            System.out.println();
        }

        return true;
    }

    @Override
    public boolean findInContributions(String nameOrTitle) {
        for (T item : getPersonalContributions()) {
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

        for (Object item : adminTeamContributions) {
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

    @Override
    public boolean searchInAddedItems(String nameOrTitle) {
        for (T item : getPersonalContributions()) {
            if (item instanceof Movie && ((Movie) item).getTitle().equals(nameOrTitle)) {
                return true;
            } else if (item instanceof Series && ((Series) item).getTitle().equals(nameOrTitle)) {
                return true;
            } else if (item instanceof Actor && ((Actor) item).getName().equals(nameOrTitle)) {
                return true;
            }
        }
        for (Object item : adminTeamContributions) {
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
    public void removeActorFromAddedItems(Actor a) {
        if (a != null) {
            Iterator<?> iterator = getPersonalContributions().iterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                if (item instanceof Actor actor && actor.getName().equals(a.getName())) {
                    iterator.remove();
                    break;
                }
            }
            iterator = adminTeamContributions.iterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                if (item instanceof Actor actor && actor.getName().equals(a.getName())) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    @Override
    public void removeProductionFromAddedItems(Production p) {
        if (p != null) {
            Iterator<?> iterator = getPersonalContributions().iterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                if (item instanceof Production && ((Production) item).getTitle().equals(p.getTitle())) {
                    iterator.remove();
                    break;
                }
            }
            iterator = adminTeamContributions.iterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                if (item instanceof Production && ((Production) item).getTitle().equals(p.getTitle())) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    @Override
    public Request getReceivedRequestWithIndex(Integer index) {
        if (index < getReceivedRequests().size()) {
            return getReceivedRequests().get(index);
        } else {
            index -= getReceivedRequests().size();
            return RequestsHolder.getRequests().get(index);
        }
    }

    @Override
    public boolean displayReceivedRequests() {
        if (getReceivedRequests().isEmpty() && RequestsHolder.getRequests().isEmpty()) {
            System.out.println("You currently don't have any requests to take care of.");
            return false;
        }
        int index = 1;
        System.out.println("These are your received personal requests:");
        for (Request request : getReceivedRequests()) {
            System.out.println("Request no: " + index);
            request.displayInfo();
            System.out.println();
            index++;
        }

        System.out.println("These are the request for the whole admin team:");
        for (Request request : RequestsHolder.getRequests()) {
            System.out.println("Request no: " + index);
            request.displayInfo();
            System.out.println();
            index++;
        }
        return true;
    }

    @Override
    public void removeProductionSystem(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Production name cannot be null or empty");
        }

        Production toRemove = null;
        for (T item : getPersonalContributions()) {
            if (item instanceof Production && ((Production) item).getTitle().equals(name)) {
                toRemove = (Production) item;
                break;
            }
        }
        for (Object item : adminTeamContributions) {
            if (item instanceof Production && ((Production) item).getTitle().equals(name)) {
                toRemove = (Production) item;
                break;
            }
        }

        if (toRemove != null) {
            removeProductionFromAddedItems(toRemove);
            if (adminTeamContributions.contains(toRemove))
                removeAdminTeamContribution(toRemove);
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
        for (T item : getPersonalContributions()) {
            if (item instanceof Actor && ((Actor) item).getName().equals(name)) {
                toRemove = (Actor) item;
                break;
            }
        }
        for (Object item : adminTeamContributions) {
            if (item instanceof Actor && ((Actor) item).getName().equals(name)) {
                toRemove = (Actor) item;
                break;
            }
        }

        if (toRemove != null) {
            removeActorFromAddedItems(toRemove);
            if (adminTeamContributions.contains(toRemove))
                removeAdminTeamContribution(toRemove);
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
        boolean fromPersonalContributions = true;
        for (T item : getPersonalContributions()) {
            if (item instanceof Production && ((Production) item).getTitle().equals(p.getTitle())) {
                toUpdate = (Production) item;
                break;
            }
        }
        for (Object item : adminTeamContributions) {
            if (item instanceof Production && ((Production) item).getTitle().equals(p.getTitle())) {
                toUpdate = (Production) item;
                fromPersonalContributions = false;
                break;
            }
        }

        if (toUpdate == null) {
            throw new IllegalArgumentException("Production not found or not added by user: " + p.getTitle());
        } else {
            removeProductionFromAddedItems(p);
            if (fromPersonalContributions) {
                addToAddedItems((T) p);
            } else {
                addAdminTeamContribution(p);
            }

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
        boolean fromPersonalContributions = true;
        for (T item : getPersonalContributions()) {
            if (item instanceof Actor && ((Actor) item).getName().equals(a.getName())) {
                toUpdate = (Actor) item;
                break;
            }
        }
        for (Object item : adminTeamContributions) {
            if (item instanceof Actor && ((Actor) item).getName().equals(a.getName())) {
                toUpdate = (Actor) item;
                fromPersonalContributions = false;
                break;
            }
        }

        if (toUpdate == null) {
            throw new IllegalArgumentException("Actor not found or not added by user: " + a.getName());
        } else {
            removeActorFromAddedItems(a);
            if (fromPersonalContributions) {
                addToAddedItems((T) a);
            } else {
                addAdminTeamContribution(a);
            }

            for (User user : imdb.getUsers()) {
                if (user.searchFavouriteActor(a.getName()) != null) {
                    user.removeActorFromFavourites(toUpdate);
                    user.addToFavorites(toUpdate);
                }
            }
        }
    }

    @Override
    public void updateExperience() {
    }

}
