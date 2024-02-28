package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})

public class IMDB {
    private static IMDB instance = null;

    private final List<User<?>> users;
    private List<Actor> actors;
    private List<Request> requests;
    private final List<Production> productions;

    private static boolean startGUI = false;

    private boolean exit = false;
    private static boolean loggedIn = false;

    private static User<?> currentUser = null;

    private IMDB() {
        users = new ArrayList<>();
        actors = new ArrayList<>();
        requests = new ArrayList<>();
        productions = new ArrayList<>();
    }

    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

    public void run() throws IOException {

        loadActorsFromJson();

        loadProductionFromJson();
        loadUsersFromJson();
        loadRequestsFromJson();
//        GUI.getInstance();
//        startGUI = true;

        interface_choice();
        if (!startGUI) {
            Terminal terminal = Terminal.getInstance();
            while (!exit) {
                currentUser = terminal.login();
                if (currentUser.getAccountType().equals(AccountType.REGULAR)) {
                    while (loggedIn && !exit) {
                        terminal.processInputForRegulars(terminal.displayOptionsForRegulars(currentUser), (Regular<?>) currentUser);
                    }
                } else if (currentUser.getAccountType().equals(AccountType.CONTRIBUTOR)) {
                    while (loggedIn && !exit) {
                        terminal.processInputForContributors(terminal.displayOptionsForContributors(currentUser), (Contributor<?>) currentUser);
                    }
                } else if (currentUser.getAccountType().equals(AccountType.ADMIN)) {
                    while (loggedIn && !exit) {
                        terminal.processInputForAdmins(terminal.displayOptionsForAdmins(currentUser), (Admin<?>) currentUser);
                    }
                }
            }
        } else {
            GUI.getInstance();
        }

    }

    public static void setCurrentUser(User<?> currentUser) {
        IMDB.currentUser = currentUser;
    }

    private void interface_choice() {
        System.out.println("Choose between terminal or GUI: ");

        Scanner scanInput = new Scanner(System.in);
        String choice;

        while (true) {
            choice = scanInput.nextLine();
            if (choice.equalsIgnoreCase("terminal")) {
                System.out.println("Starting terminal...");
                startGUI = false;
                break;
            } else if (choice.equalsIgnoreCase("GUI")) {
                System.out.println("Opening GUI...");
                startGUI = true;
                break;
            } else {
                System.out.println("Wrong input! Please input \"terminal\" or \"GUI\"!");
            }
        }
    }

    private void loadActorsFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.actors = objectMapper.readValue(
                new File("src/main/resources/input/actors.json"),
                new TypeReference<>() {
                }
        );

    }

    private void loadRequestsFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.requests = objectMapper.readValue(
                new File("src/main/resources/input/requests.json"),
                new TypeReference<>() {
                }
        );
        for (Request request : requests) {
            if (request.getType().equals(RequestsTypes.OTHERS) || request.getType().equals(RequestsTypes.DELETE_ACCOUNT)) {
                RequestsHolder.addRequest(request);
                for (User<?> user : users) {
                    if (user instanceof Admin) {
                        request.registerObserver(user);
                    }
                }
            } else {
                User<?> resolverUser = searchUser(request.getResolverUsername());
                if (resolverUser != null) {
                    ((Staff<?>) resolverUser).addRequestToReceivedRequestsList(request);
                    request.registerObserver(resolverUser);
                }
            }
            request.notifyObservers("You have a new request pending");

            User<?> user = searchUser(request.getCreatorUsername());
            if (user instanceof Regular<?> regular) {
                regular.getPersonalRequests().add(request);
            } else if (user instanceof Contributor<?> contributor) {
                contributor.getPersonalRequests().add(request);
            }
        }
    }

    private void loadProductionFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> productionsData = objectMapper.readValue(
                new File("src/main/resources/input/production.json"),
                new TypeReference<>() {
                });

        List<Actor> uncompletedActors = new ArrayList<>();

        for (Map<String, Object> productionData : productionsData) {
            String title = (String) productionData.get("title");
            String type = (String) productionData.get("type");


            List<String> actors = (List<String>) productionData.get("actors");
            for (String actorName : actors) {
                Actor actor = searchActor(actorName);
                if (actor == null) {
                    actor = new Actor(actorName, "");
                    Admin.addAdminTeamContribution(actor);
                    uncompletedActors.add(actor);
                }
            }

            List<String> directors = (List<String>) productionData.get("directors");
            List<Genre> genres = parseGenres((List<String>) productionData.get("genres"));

            List<Map<String, Object>> ratingsData = (List<Map<String, Object>>) productionData.get("ratings");
            List<Rating> ratings = parseRatings(ratingsData);

            String description = (String) productionData.get("plot");
            double averageRating = (Double) productionData.get("averageRating");
            int releaseYear = parseReleaseYear(productionData);
            if ("Movie".equals(type)) {
                int duration = parseDuration((String) productionData.get("duration"));
                Movie movie = new Movie(title, directors, actors, genres, description, ratings, averageRating, duration, releaseYear);
                productions.add(movie);
            } else if ("Series".equals(type)) {

                int numberOfSeasons = (Integer) productionData.get("numSeasons");
                Map<String, List<Episode>> seasons = parseSeasons((Map<String, List<Map<String, Object>>>) productionData.get("seasons"));
                Series series = new Series(title, directors, actors, genres, description, ratings, averageRating, releaseYear, numberOfSeasons, seasons);
                productions.add(series);
            }
        }

        for (Actor actor : uncompletedActors) {
            for (Production production : productions) {
                if (production.findActorInCast(actor.getName())) {
                    ActorPerformance performance = new ActorPerformance(production.getTitle());
                    if (production instanceof Movie) {
                        performance.setType(PerformanceType.valueOf("MOVIE"));
                    } else if (production instanceof Series) {
                        performance.setType(PerformanceType.valueOf("SERIES"));
                    }
                    actor.getPerformances().add(performance);
                }
            }
            actors.add(actor);
        }
    }


    private List<Rating> parseRatings(List<Map<String, Object>> ratingsData) {
        List<Rating> ratings = new ArrayList<>();
        for (Map<String, Object> ratingData : ratingsData) {
            String username = (String) ratingData.get("username");
            int ratingValue = (Integer) ratingData.get("rating");
            String comment = (String) ratingData.get("comment");

            ratings.add(new Rating(username, ratingValue, comment));
        }
        return ratings;
    }

    private int parseReleaseYear(Map<String, Object> productionData) {
        if (!productionData.containsKey("releaseYear")) {
            return 0;
        }

        Object releaseYearObj = productionData.get("releaseYear");
        if (releaseYearObj instanceof Integer) {
            return (Integer) releaseYearObj;
        } else {
            return 0;
        }
    }

    private int parseDuration(String durationStr) {
        return Integer.parseInt(durationStr.split(" ")[0]);
    }

    private List<Genre> parseGenres(List<String> genreString) {
        List<Genre> genres = new ArrayList<>();
        for (String genreStr : genreString) {
            genres.add(Genre.valueOf(genreStr.toUpperCase()));
        }
        return genres;
    }

    private Map<String, List<Episode>> parseSeasons(Map<String, List<Map<String, Object>>> seasonsData) {
        Map<String, List<Episode>> seasons = new HashMap<>();
        for (Map.Entry<String, List<Map<String, Object>>> seasonEntry : seasonsData.entrySet()) {
            List<Episode> episodes = new ArrayList<>();
            for (Map<String, Object> episodeData : seasonEntry.getValue()) {
                String name = (String) episodeData.get("episodeName");
                int duration = parseDuration((String) episodeData.get("duration"));
                episodes.add(new Episode(name, duration));
            }
            seasons.put(seasonEntry.getKey(), episodes);
        }
        return seasons;
    }


    private void loadUsersFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> usersData = objectMapper.readValue(
                new File("src/main/resources/input/accounts.json"),
                new TypeReference<>() {
                }
        );

        for (Map<String, Object> userData : usersData) {
            User<?> user = createUserFromData(userData);

            List<String> favoriteProductionTitles = (List<String>) userData.get("favoriteProductions");
            List<String> favoriteActorNames = (List<String>) userData.get("favoriteActors");

            populateUserFavorites(user, favoriteProductionTitles, favoriteActorNames);

            if (user instanceof Staff) {
                List<String> addedProductionTitles = (List<String>) userData.get("productionsContribution");
                List<String> addedActorNames = (List<String>) userData.get("actorsContribution");
                populateUserAddedItems(user, addedProductionTitles, addedActorNames);
            }

            users.add(user);
        }
    }

    private User<?> createUserFromData(Map<String, Object> userData) {
        String username = (String) userData.get("username");

        int experience = 0;
        String experienceStr = (String) userData.get("experience");
        if (experienceStr != null && !experienceStr.isEmpty()) {
            experience = Integer.parseInt(experienceStr);
        }

        String userTypeString = (String) userData.get("userType");
        AccountType userType = AccountType.valueOf(userTypeString.toUpperCase());

        Map<String, Object> infoMap = (Map<String, Object>) userData.get("information");
        Map<String, String> credentialsMap = (Map<String, String>) infoMap.get("credentials");
        Credentials credentials = new Credentials(credentialsMap.get("email"), credentialsMap.get("password"));
        String name = (String) infoMap.get("name");
        String country = (String) infoMap.get("country");
        int age = (int) infoMap.get("age");

        char gender = ((String) infoMap.get("gender")).charAt(0);
        LocalDate birthDate = LocalDate.parse((String) infoMap.get("birthDate"));

        User.Information info = new User.Information.Builder()
                .credentials(credentials)
                .name(name)
                .country(country)
                .age(age)
                .gender(gender)
                .birthDate(birthDate)
                .build();

        return UserFactory.createUser(userType, username, info, experience);
    }

    private void populateUserFavorites(User user, List<String> favoriteProductionTitles, List<String> favoriteActorNames) {
        if (favoriteProductionTitles != null) {
            for (String title : favoriteProductionTitles) {
                Production favProduction = findProductionByTitle(title);
                if (favProduction != null) {
                    user.addToFavorites(favProduction);
                }
            }
        }

        if (favoriteActorNames != null) {
            for (String name : favoriteActorNames) {
                Actor favActor = findActorByName(name);
                if (favActor != null) {
                    user.addToFavorites(favActor);
                }
            }
        }
    }

    private void populateUserAddedItems(User<?> user, List<String> addedProductionTitles, List<String> addedActorNames) {
        if (!(user instanceof Staff staff)) {
            return;
        }

        if (addedProductionTitles != null) {
            for (String title : addedProductionTitles) {
                Production addedProduction = findProductionByTitle(title);
                if (addedProduction != null) {
                    staff.addToAddedItems(addedProduction);
                    addedProduction.registerObserver(staff);
                }
            }
        }

        if (addedActorNames != null) {
            for (String name : addedActorNames) {
                Actor addedActor = findActorByName(name);
                if (addedActor != null) {
                    staff.addToAddedItems(addedActor);
                }
            }
        }
    }

    public Production findProductionByTitle(String title) {
        for (Production production : productions) {
            if (production.getTitle().equalsIgnoreCase(title)) {
                return production;
            }
        }
        return null;
    }

    private Actor findActorByName(String name) {
        for (Actor actor : actors) {
            if (actor.getName().equalsIgnoreCase(name)) {
                return actor;
            }
        }
        return null;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public User<?> getUserWithEmail(String email) {
        for (User<?> user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }


    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public static void setLoggedIn(boolean status) {
        loggedIn = status;
    }

    public void showProductions(List<Production> productionList) {
        for (Production production : productionList) {
            production.displayInfo();
            System.out.println();
        }
    }

    public void showActors(List<Actor> actorList) {
        for (Actor actor : actorList) {
            actor.displayInfo();
            System.out.println();
        }
    }

    public Movie searchMovie(String title) {
        for (Production production : productions) {
            if (production instanceof Movie && production.getTitle().equalsIgnoreCase(title)) {
                return (Movie) production;
            }
        }
        return null;
    }

    public Series searchSeries(String title) {
        for (Production production : productions) {
            if (production instanceof Series && production.getTitle().equalsIgnoreCase(title)) {
                return (Series) production;
            }
        }
        return null;
    }

    public Actor searchActor(String name) {
        for (Actor actor : actors) {
            if (actor.getName().equalsIgnoreCase(name)) {
                return actor;
            }
        }
        return null;
    }

    public User<?> findResolverUser(String nameOrTitle) {
        for (User<?> user : users) {
            if ((user instanceof Contributor && ((Contributor<?>) user).searchInAddedItems(nameOrTitle))) {
                return user;
            }
            if ((user instanceof Admin && ((Admin<?>) user).searchInAddedItems(nameOrTitle))) {
                return user;
            }
        }
        return null;
    }

    public User<?> searchUser(String username) {
        for (User<?> user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void addRequestToDataBase(Request request) {
        if (request.getType() == RequestsTypes.DELETE_ACCOUNT || request.getType() == RequestsTypes.OTHERS) {
            RequestsHolder.addRequest(request);
        }
        requests.add(request);
    }

    public void removeRequestFromDataBase(Request request) {
        for (Request item : requests) {
            if (item.equals(request)) {
                requests.remove(item);
                return;
            }
        }
    }

    public List<User<?>> getUsers() {
        return users;
    }

    public static User<?> getCurrentUser() {
        return currentUser;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public List<Production> filterProductionsByGenre(Genre genre) {
        return productions.stream()
                .filter(p -> p.getGenres().contains(genre))
                .collect(Collectors.toList());
    }

    public List<Production> filterProductionsByNumberOfRatings(int minRatings) {
        return productions.stream()
                .filter(p -> p.getRatings().size() >= minRatings)
                .collect(Collectors.toList());
    }

    public List<Production> sortProductionsByNumberOfRatings() {
        return productions.stream()
                .sorted(Comparator.comparing(
                        (Production p) -> (p.getRatings() != null ? p.getRatings().size() : 0),
                        Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public List<Actor> sortActorsByName() {
        return actors.stream()
                .sorted(Comparator.comparing(Actor::getName))
                .collect(Collectors.toList());
    }

    public boolean doesUserExist(String userName) {
        boolean itExists = false;
        for (User<?> user : users) {
            if (user.getUsername().equals(userName)) {
                itExists = true;
                break;
            }
        }
        return itExists;
    }

    public boolean doesEmailExist(String email) {
        boolean itExists = false;
        for (User<?> user : users) {
            if (user.getEmail().equals(email)) {
                itExists = true;
                break;
            }
        }
        return itExists;
    }

    public static boolean isStartGUI() {
        return startGUI;
    }
}


