package org.example;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})

public class Terminal {
    private static Terminal instance = null;

    private Terminal() {
    }

    public static Terminal getInstance() {
        if (instance == null) {
            instance = new Terminal();
        }
        return instance;
    }

    public User<?> login() {
        Scanner scanner = new Scanner(System.in);
        IMDB imdb = IMDB.getInstance();
        IMDB.setLoggedIn(false);

        System.out.println("Login to your account");
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User<?> user = imdb.getUserWithEmail(email);

        while (true) {
            if (user != null && user.getPassword().equals(password)) {
                System.out.println("Login successful!");
                System.out.println("\nWelcome back " + user.getUsername() + "!");
                IMDB.setLoggedIn(true);
                return user;
            } else {
                System.out.println("Invalid email or password, try again!");
                System.out.print("Email: ");
                email = scanner.nextLine();

                System.out.print("Password: ");
                password = scanner.nextLine();
                user = imdb.getUserWithEmail(email);
            }
        }
    }


    public Integer displayOptionsForRegulars(User<?> user) {
        System.out.println();
        System.out.println("Username: " + user.getUsername());
        System.out.println("User experience: " + user.getExperience());
        System.out.println("Choose action:");
        System.out.println("1) View productions details");
        System.out.println("2) View actors details");
        System.out.println("3) View notifications");
        System.out.println("4) Search for actor/movie/series");
        System.out.println("5) Display personal requests");
        System.out.println("6) Add/Delete request");
        System.out.println("7) Display favourites");
        System.out.println("8) Add/Delete actor/movie/series to/from favorites");
        System.out.println("9) Add/Delete rating for a production");
        System.out.println("10) Logout");

        Scanner scanner = new Scanner(System.in);
        return integerChoice(scanner, 10, 1);
    }

    public Integer displayOptionsForContributors(User<?> user) {
        System.out.println();
        System.out.println("Username: " + user.getUsername());
        System.out.println("User experience: " + user.getExperience());
        System.out.println("Choose action:");
        System.out.println("1) View productions details");
        System.out.println("2) View actors details");
        System.out.println("3) View notifications");
        System.out.println("4) Search for actor/movie/series");
        System.out.println("5) Display personal requests");
        System.out.println("6) Display contributions");
        System.out.println("7) Display favourites");
        System.out.println("8) Add/Delete actor/movie/series to/from favorites");
        System.out.println("9) Add/Delete request");
        System.out.println("10) Add/Delete actor/production to/from system");
        System.out.println("11) Show received requests");
        System.out.println("12) Manage received requests");
        System.out.println("13) Update production/actors");
        System.out.println("14) Logout");

        Scanner scanner = new Scanner(System.in);
        return integerChoice(scanner, 14, 1);

    }

    public Integer displayOptionsForAdmins(User<?> user) {
        System.out.println();
        System.out.println("Username: " + user.getUsername());
        if (user.getExperience() != null)
            System.out.println("User experience: " + user.getExperience());
        System.out.println("Choose action:");
        System.out.println("1) View productions details");
        System.out.println("2) View actors details");
        System.out.println("3) View notifications");
        System.out.println("4) Search for actor/movie/series");
        System.out.println("5) Display contributions");
        System.out.println("6) Display favourites");
        System.out.println("7) Add/Delete actor/movie/series to/from favorites");
        System.out.println("8) Add/Delete actor/production to/from system");
        System.out.println("9) Show received requests");
        System.out.println("10) Manage received requests");
        System.out.println("11) Update production/actors");
        System.out.println("12) Add/delete user.");
        System.out.println("13) Logout");

        Scanner scanner = new Scanner(System.in);
        return integerChoice(scanner, 13, 1);
    }


    public void processInputForRegulars(Integer choice, Regular<?> user) {

        IMDB imdb = IMDB.getInstance();

        switch (choice) {
            case 1:
                showProductions(imdb);
                return;

            case 2:
                showActors(imdb);
                return;

            case 3:
                user.showNotifications();
                return;

            case 4:
                search(imdb);
                return;

            case 5:
                user.displayPersonalRequests();
                return;

            case 6:
                createOrDeleteRequestsRegularUser(user, imdb);
                return;

            case 7:
                System.out.println("Your favourites are: ");
                user.displayFavoritesExpanded();
                return;

            case 8:
                modifyFavorites(user, imdb);
                return;

            case 9:
                rateProduction(user, imdb);
                return;

            case 10:
                Logout(imdb);
                return;

            default:
        }
    }

    public void processInputForContributors(Integer choice, Contributor<?> user) {

        IMDB imdb = IMDB.getInstance();

        switch (choice) {
            case 1:
                showProductions(imdb);
                return;

            case 2:
                showActors(imdb);
                return;

            case 3:
                user.showNotifications();
                return;

            case 4:
                search(imdb);
                return;

            case 5:
                System.out.println("Your personal requests are: ");
                user.displayPersonalRequests();
                return;

            case 6:
                user.displayContributionsSimple();
                return;

            case 7:
                user.displayFavoritesExpanded();
                return;

            case 8:
                modifyFavorites(user, imdb);
                return;

            case 9:
                createOrDeleteRequestsContributorUser(user, imdb);
                return;

            case 10:
                addOrRemoveProduction(user, imdb);
                return;

            case 11:
                user.displayReceivedRequests();
                return;

            case 12:
                resolveOrDenyRequest(user);
                return;
            case 13:
                updateProductionOrActor(user, imdb);
                return;

            case 14:
                Logout(imdb);
                return;

            default:
        }
    }

    public void processInputForAdmins(Integer choice, Admin<?> user) {

        IMDB imdb = IMDB.getInstance();

        switch (choice) {
            case 1:
                showProductions(imdb);
                return;

            case 2:
                showActors(imdb);
                return;

            case 3:
                user.showNotifications();
                return;

            case 4:
                search(imdb);
                return;

            case 5:
                user.displayContributionsSimple();
                return;

            case 6:
                user.displayFavoritesExpanded();
                return;

            case 7:
                modifyFavorites(user, imdb);
                return;

            case 8:
                addOrRemoveProduction(user, imdb);
                return;

            case 9:
                user.displayReceivedRequests();
                return;

            case 10:
                resolveOrDenyRequest(user);
                return;
            case 11:
                updateProductionOrActor(user, imdb);
                return;

            case 12:
                addOrDeleteUser(user, imdb);
                return;

            case 13:
                Logout(imdb);
                return;

            default:
        }
    }

    private void addOrDeleteUser(Admin<?> user, IMDB imdb) {
        System.out.println("Do you want do add or to delete an user?");
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("add")) {
                createUser(user, scanner);
                return;
            } else if (input.equalsIgnoreCase("delete")) {
                deleteUser(user, imdb, scanner);
                return;
            } else {
                System.out.println("Please choose between add or delete.");
            }
        } while (true);
    }

    private void deleteUser(Admin<?> user, IMDB imdb, Scanner scanner) {
        System.out.println("Available users:");
        for (User<?> item : imdb.getUsers()) {
            System.out.println((imdb.getUsers().indexOf(item) + 1) + "): ");
            item.displayAllInfo();
            System.out.println();
        }
        System.out.println("Select the number of the user you want to delete:");
        int index = integerChoice(scanner, imdb.getUsers().size() - 1, 1) - 1;
        try {
            user.deleteUser(imdb.getUsers().get(index));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Successfully deleted user!");
    }

    private void createUser(Admin<?> user, Scanner scanner) {
        String input;
        System.out.println("Account type: (REGULAR, CONTRIBUTOR, ADMIN)");
        String accountTypeString;
        accountTypeString = scanner.nextLine();
        AccountType accountType;
        try {
            accountType = AccountType.valueOf(accountTypeString);
        } catch (IllegalArgumentException e) {
            System.out.println("Account type not exiting.");
            return;
        }
        System.out.println("User full name:");
        String name = scanner.nextLine();
        System.out.println("Country:");
        String country = scanner.nextLine();
        System.out.println("Age:");
        int age = integerChoice(scanner, 1000, 0);
        System.out.println("Gender: (F,M,N)");
        char gender;
        do {
            input = scanner.nextLine();
            gender = input.charAt(0);

            if (gender == 'F' || gender == 'M' || gender == 'N')
                break;
            else
                System.out.println("Please choose between F/M/N");
        } while (true);
        System.out.println("Please enter the birthday (YYYY-MM-DD):");
        LocalDate birthDate = null;
        do {
            input = scanner.nextLine();
            try {
                birthDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD:");
            }
        } while (birthDate == null);

        System.out.println("Enter your email:");
        String email;
        do {
            email = scanner.nextLine();
            if (Credentials.isValidEmail(email))
                break;
            else
                System.out.println("Please enter a valid email address. This one is invalid or in use.");
        } while (true);

        Credentials credentials = new Credentials(email);

        User.Information info = new User.Information.Builder()
                .credentials(credentials)
                .name(name)
                .country(country)
                .age(age)
                .gender(gender)
                .birthDate(birthDate)
                .build();

        String username = User.generateUsername(name);
        User<?> createdUser = UserFactory.createUser(accountType, username, info, 0);
        user.addUser(createdUser);
        System.out.println("Ai creat user: ");
        createdUser.displayAllInfo();
    }

    private void updateProductionOrActor(Staff<?> user, IMDB imdb) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please remember, you can only modify actor or productions from your contribution!");
        user.displayContributionsSimple();
        System.out.println("Do you want to update an actor, a movie or a series?");
        do {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("actor")) {
                System.out.println("What is the name of the actor you want to update?");
                String actorName = scanner.nextLine();
                Actor toUpdate = imdb.searchActor(actorName);
                if (toUpdate == null || !user.findInContributions(toUpdate.getName())) {
                    System.out.println("Actor not in contributions or in system.");
                    return;
                }
                updateActorFromInput(scanner, toUpdate);
                try {
                    user.updateActor(toUpdate);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                return;
            } else if (input.equalsIgnoreCase("movie")) {
                System.out.println("What is the title of the movie?");
                String movieTitle = scanner.nextLine();
                Movie toUpdate = imdb.searchMovie(movieTitle);
                if (toUpdate == null || !user.searchInAddedItems(movieTitle)) {
                    System.out.println("Movie not in contribution or in system!");
                    return;
                }

                System.out.println("What do you want to modify? : ");
                System.out.println("""
                        Choose what do you want to change :
                        1) Title
                        2) Description
                        3) Cast
                        4) Directors
                        5) Genres
                        6) Duration
                        7) Release Year
                        """);

                int integerChoice = integerChoice(scanner, 7, 1);
                switch (integerChoice) {
                    case 1:
                        updateTitle(scanner, toUpdate);
                        break;
                    case 2:
                        updateDescription(scanner, toUpdate);
                        break;
                    case 3:
                        if (updateCast(scanner, imdb, toUpdate)) return;
                        break;
                    case 4:
                        updateDirectors(scanner, toUpdate);
                        break;
                    case 5:
                        updateGenre(scanner, toUpdate);
                        break;
                    case 6:
                        System.out.println("New duration:");
                        int newDuration = integerChoice(scanner, 100000, 0);
                        toUpdate.setDuration(newDuration);
                        System.out.println("Duration updated successfully.");
                        break;
                    case 7:
                        System.out.println("New release year:");
                        int newReleaseYear = integerChoice(scanner, 100000, 0);
                        toUpdate.setReleaseYear(newReleaseYear);
                        System.out.println("Release year updated successfully.");
                        break;
                }
                user.updateProduction(toUpdate);
                return;
            } else if (input.equalsIgnoreCase("series")) {
                System.out.println("What is the title of the movie?");
                String seriesTitle = scanner.nextLine();

                Series toUpdate = imdb.searchSeries(seriesTitle);
                if (toUpdate == null || !user.searchInAddedItems(seriesTitle)) {
                    System.out.println("Series not in contribution or in system!");
                    return;
                }


                System.out.println("What do you want to modify? : ");
                System.out.println("""
                        Choose what do you want to change :
                        1) Title
                        2) Description
                        3) Cast
                        4) Directors
                        5) Genres
                        6) Release Year
                        7) Number of seasons
                        """);

                int integerChoice = integerChoice(scanner, 7, 1);
                switch (integerChoice) {
                    case 1:
                        updateTitle(scanner, toUpdate);
                        break;
                    case 2:
                        updateDescription(scanner, toUpdate);
                        break;
                    case 3:
                        if (updateCast(scanner, imdb, toUpdate)) return;
                        break;
                    case 4:
                        updateDirectors(scanner, toUpdate);
                        break;
                    case 5:
                        updateGenre(scanner, toUpdate);
                        break;
                    case 6:
                        System.out.println("New duration:");
                        System.out.println("New release year:");
                        int newReleaseYear = integerChoice(scanner, 100000, 0);
                        toUpdate.setReleaseYear(newReleaseYear);
                        System.out.println("Release year updated successfully.");
                        break;
                    case 7:
                        int newNumSeasons = integerChoice(scanner, 100000, 0);
                        toUpdate.setNumberOfSeasons(newNumSeasons);
                        System.out.println("Number of seasons updated successfully.");
                        break;
                }
                user.updateProduction(toUpdate);
                return;
            } else {
                System.out.println("Invalid option, please choose between actor, movie or series.");
            }
        } while (true);
    }

    private void updateActorFromInput(Scanner scanner, Actor toUpdate) {
        String input;

        System.out.println("""
                Choose what do you want to change :
                1) Name
                2) Performances
                3) Biography
                """);

        int integerInput = integerChoice(scanner, 3, 1);
        switch (integerInput) {
            case 1:
                System.out.println("Type the new name please.");
                String newActorName = scanner.nextLine();
                toUpdate.setName(newActorName);
                System.out.println("Name modified successfully.");
                break;
            case 2:
                System.out.println("Do you want to update, add or to remove a performance?.");
                do {
                    input = scanner.nextLine();
                    if (input.equalsIgnoreCase("update")) {
                        toUpdate.displayPerformances();
                        int performanceChoice = integerChoice(scanner, toUpdate.getPerformances().size() - 1, 1);
                        ActorPerformance updatedPerformance = toUpdate.getPerformances().get(performanceChoice - 1);
                        System.out.println("What do you want to modify? Type, Title or both?");
                        do {
                            input = scanner.nextLine();
                            if (input.equalsIgnoreCase("type")) {
                                System.out.println("New type: ");
                                do {
                                    input = scanner.nextLine();
                                    if (input.equalsIgnoreCase("movie")) {
                                        updatedPerformance.setType(PerformanceType.MOVIE);
                                        break;
                                    } else if (input.equalsIgnoreCase("series")) {
                                        updatedPerformance.setType(PerformanceType.SERIES);
                                        break;
                                    } else {
                                        System.out.println("Please choose between series or movie.");
                                    }
                                } while (true);
                                System.out.println("Performance type modified successfully.");
                                break;
                            } else if (input.equalsIgnoreCase("title")) {
                                System.out.println("Enter new title:");
                                String newTitle = scanner.nextLine();
                                updatedPerformance.setTitle(newTitle);
                                System.out.println("Performance title modified successfully.");
                                break;
                            } else if (input.equalsIgnoreCase("both")) {
                                System.out.println("Enter new title:");
                                String newTitle = scanner.nextLine();
                                updatedPerformance.setTitle(newTitle);
                                System.out.println("New type: ");
                                do {
                                    input = scanner.nextLine();
                                    if (input.equalsIgnoreCase("movie")) {
                                        updatedPerformance.setType(PerformanceType.MOVIE);
                                        break;
                                    } else if (input.equalsIgnoreCase("series")) {
                                        updatedPerformance.setType(PerformanceType.SERIES);
                                        break;
                                    } else {
                                        System.out.println("Please choose between series or movie.");
                                    }
                                } while (true);
                                System.out.println("Type and title modified successfully.");
                                break;
                            } else {
                                System.out.println("Please choose between type, title or both");
                            }
                        } while (true);
                        toUpdate.getPerformances().set(performanceChoice - 1, updatedPerformance);
                        break;
                    } else if (input.equalsIgnoreCase("add")) {
                        ActorPerformance performance = getActorPerformanceFromInput(scanner);
                        toUpdate.addPerformance(performance);
                        System.out.println("Performance added successfully.");
                        break;
                    } else if (input.equalsIgnoreCase("remove")) {
                        System.out.println("Please select the performance you want to remove:");
                        toUpdate.displayPerformances();
                        int performanceChoice = integerChoice(scanner, toUpdate.getPerformances().size() - 1, 1);
                        toUpdate.getPerformances().remove(performanceChoice - 1);
                        System.out.println("Performance removed successfully.");
                        break;
                    }
                } while (true);
                break;
            case 3:
                System.out.println("Type the new biography:");
                String newBiography = scanner.nextLine();
                toUpdate.setBiography(newBiography);
                System.out.println("Biography updated successfully.");
                break;
        }
    }

    private void updateTitle(Scanner scanner, Production updatedProduction) {
        System.out.println("New title:");
        String newTitle = scanner.nextLine();
        updatedProduction.setTitle(newTitle);
        System.out.println("Title modified successfully.");
    }

    private void updateDescription(Scanner scanner, Production updatedProduction) {
        System.out.println("New description:");
        String newDescription = scanner.nextLine();
        updatedProduction.setDescription(newDescription);
        System.out.println("Description modified successfully.");
    }

    private boolean updateCast(Scanner scanner, IMDB imdb, Production updatedProduction) {
        String input;
        System.out.println("Add, remove or update existing cast?");
        do {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("add")) {
                System.out.println("Type the name of the actor you want to add.");
                String newActorName = scanner.nextLine();
                if (imdb.searchActor(newActorName) == null) {
                    System.out.println("Actor must exist in the system! Please add actor first!");
                    return true;
                }
                updatedProduction.addActorToCast(newActorName);
                System.out.println("Cast added successfully.");
                break;
            }
            if (input.equalsIgnoreCase("remove")) {
                System.out.println("Please input the actor name:");
                String actorName = scanner.nextLine();
                if (imdb.searchActor(actorName) == null) {
                    System.out.println("Actor not found. Add him before trying to remove.");
                    return true;
                }
                updatedProduction.removeActorFromCast(actorName);
                System.out.println("Cast removed successfully.");
                break;
            }
            if (input.equalsIgnoreCase("update")) {
                System.out.println("Select the cast you want to update:");
                updatedProduction.displayCast();
                int castChoice = integerChoice(scanner, updatedProduction.getCast().size() - 1, 1);
                System.out.println("Input new name:");
                String newName = scanner.nextLine();
                if (imdb.searchActor(newName) == null) {
                    System.out.println("Actor must exist in the system! Please add actor first!");
                    return true;
                }
                updatedProduction.getCast().set(castChoice - 1, newName);
                System.out.println("Cast updated successfully.");
                break;
            } else {
                System.out.println("Please select add, remove or update.");
            }
        } while (true);
        return false;
    }

    private void updateDirectors(Scanner scanner, Production updatedProduction) {
        String input;
        System.out.println("Add, remove or update existing director?");
        do {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("add")) {
                System.out.println("Type the name of the director you want to add.");
                String newDirectorName = scanner.nextLine();
                updatedProduction.addDirectorToList(newDirectorName);
                System.out.println("Director name updated successfully.");
                break;
            }
            if (input.equalsIgnoreCase("remove")) {
                System.out.println("Please input the actor name:");
                String directorName = scanner.nextLine();
                updatedProduction.getDirectors().remove(directorName);
                System.out.println("Director name removed successfully");
                break;
            }
            if (input.equalsIgnoreCase("update")) {
                System.out.println("Select the cast you want to update:");
                updatedProduction.displayDirectors();
                int directorChoice = integerChoice(scanner, updatedProduction.getDirectors().size() - 1, 1);
                System.out.println("Input new name:");
                String newName = scanner.nextLine();
                updatedProduction.getDirectors().set(directorChoice - 1, newName);
                System.out.println("Director updated successfully.");
                break;
            } else {
                System.out.println("Please select add, remove or update.");
            }
        } while (true);
    }

    private void updateGenre(Scanner scanner, Production updatedProduction) {
        String input;
        System.out.println("Add or remove existing genre?");
        do {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("add")) {
                System.out.println("Type the genre you want to add.");
                String newGenre;
                do {
                    newGenre = scanner.nextLine();
                } while (!updatedProduction.addGenreAsStringToList(newGenre));
                System.out.println("Genre added successfully.");
                break;
            }
            if (input.equalsIgnoreCase("remove")) {
                System.out.println("Please select the genre you want to remove:");
                updatedProduction.displayGenres();
                int genreChoice = integerChoice(scanner, updatedProduction.getGenres().size() - 1, 1);
                updatedProduction.getGenres().remove(genreChoice - 1);
                System.out.println("Genre removed successfully.");
                break;
            }
        } while (true);
    }

    private int integerChoice(Scanner scanner, int upperBound, int lowerBound) {
        int choiceInput = 0;
        do {
            if (scanner.hasNextInt()) {
                choiceInput = scanner.nextInt();
                if (choiceInput < lowerBound || choiceInput > upperBound) {
                    System.out.println("Invalid option. Please enter a number between " + lowerBound + " and " + upperBound + ".");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        } while (choiceInput < lowerBound || choiceInput > upperBound);
        scanner.nextLine();
        return choiceInput;
    }

    private void resolveOrDenyRequest(Staff<?> user) {
        Scanner scanner;
        String input;
        if (!user.displayReceivedRequests()) {
            System.out.println("You don't have any pending requests.");
        }
        scanner = new Scanner(System.in);
        System.out.println("Please select the index of the request that you want to resolve or deny.");
        int requestIndex;
        do {
            if (scanner.hasNextInt()) {
                requestIndex = scanner.nextInt();
                break;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume the invalid input
            }
        } while (true);
        scanner.nextLine(); // Consume end line
        Request selectedRequest = user.getReceivedRequestWithIndex(requestIndex - 1);

        System.out.println("You selected the following request: ");
        selectedRequest.displayInfo();

        System.out.println("Do you want to mark it as solved, deny it or to go back?");
        System.out.println("Please choose between resolve, deny or back");

        do {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("resolve")) {
                System.out.println("Please enter the resolution's details:");
                String resolutionDetails = scanner.nextLine();
                user.resolveRequest(selectedRequest, resolutionDetails);
                return;
            } else if (input.equalsIgnoreCase("deny")) {
                System.out.println("Please enter the reason for the deny:");
                String denyReason = scanner.nextLine();
                user.denyRequest(selectedRequest, denyReason);
                return;
            } else if (input.equalsIgnoreCase("back")) {
                return;
            } else {
                System.out.println("Please choose between resolve, deny or back");
            }
        } while (true);
    }

    private void addOrRemoveProduction(Staff<?> user, IMDB imdb) {
        String input;
        Scanner scanner;
        System.out.println("Do you want to add or to remove an actor/production?");
        scanner = new Scanner(System.in);
        do {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("add")) {
                System.out.println("Choose what do you want to add : actor, movie or series.");
                do {
                    input = scanner.nextLine();
                    if (input.equalsIgnoreCase("actor")) {
                        addActorToSystem(user, imdb, scanner);
                        return;
                    } else if (input.equalsIgnoreCase("movie")) {
                        addMovieToSystem(user, imdb, scanner);
                        return;
                    } else if (input.equalsIgnoreCase("series")) {
                        addSeriesToSystem(user, imdb, scanner);
                        return;
                    } else {
                        System.out.println("Invalid input, please choose between actor, movie or series.");
                    }
                } while (true);
            } else if (input.equalsIgnoreCase("remove")) {
                System.out.println("You can only remove actors/productions from your contributions!");
                System.out.println("Your contributions are: ");
                if (!user.displayContributionsSimple()) {
                    System.out.println("You don't have any contributions to modify!");
                    return;
                }
                System.out.println("Choose what do you want to remove : actor or production.");
                do {
                    input = scanner.nextLine();
                    if (input.equalsIgnoreCase("actor")) {
                        System.out.println("Please type the name of the actor you want to remove.");
                        String actorName = scanner.nextLine();
                        try {
                            user.removeActorSystem(actorName);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                        System.out.println("Successfully removed actor from system!");
                        return;
                    } else if (input.equalsIgnoreCase("production")) {
                        System.out.println("Please type the name of the production you want to remove.");
                        String productionName = scanner.nextLine();
                        try {
                            user.removeProductionSystem(productionName);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                        System.out.println("Successfully removed production from system!");
                        return;
                    } else {
                        System.out.println("Invalid input, please choose between actor or production.");
                    }
                } while (true);
            } else {
                System.out.println("Invalid input. Please choose between add or remove.");
            }
        } while (true);
    }

    private void addSeriesToSystem(Staff<?> user, IMDB imdb, Scanner scanner) {
        System.out.println("Please type the name of the series you want to add.");
        String productionTitle = scanner.nextLine();
        if (imdb.searchSeries(productionTitle) == null) {
            Series newSeries = new Series(productionTitle);

            System.out.println("Please enter a description for this production.");
            String description = scanner.nextLine();
            newSeries.setDescription(description);

            System.out.println("Let's enter the cast. How many actors?");
            int actorsNumber = 0;
            do {
                if (scanner.hasNextInt()) {
                    actorsNumber = scanner.nextInt();
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Consume the invalid input
                }
            } while (actorsNumber < 1);
            scanner.nextLine(); // Consume end line
            for (int i = 1; i <= actorsNumber; i++) {
                System.out.println("Actor no:" + i + " name:");
                String actorName = scanner.nextLine();
                if (imdb.searchActor(actorName) == null) {
                    System.out.println("Actor does not exist! Please add him to the system before adding him to a production!");
                    System.out.println("Back to menu or try again another actor? Choose between try again or back");
                    do {
                        String input = scanner.nextLine();
                        if (input.equalsIgnoreCase("back")) {
                            return;
                        } else if (input.equalsIgnoreCase("try again")) {
                            i--;
                            break;
                        } else {
                            System.out.println("Choose between try again or back");
                        }
                    } while (true);

                } else {
                    newSeries.addCastToList(actorName);
                }
            }

            System.out.println("Let's add genres. Type a genre and press Enter. Type 'done' to finish.");
            String genreInput;
            while (!(genreInput = scanner.nextLine()).equalsIgnoreCase("done")) {
                if (!newSeries.addGenreAsStringToList(genreInput)) {
                    System.out.println("Please enter a valid genre.");
                }
            }

            System.out.println("Let's add directors. Type a director and press Enter. Type 'done' to finish.");
            String directorInput;
            while (!(directorInput = scanner.nextLine()).equalsIgnoreCase("done")) {
                newSeries.addDirectorToList(directorInput);
            }

            System.out.println("Please enter the release year.");
            int releaseYear;
            do {
                if (scanner.hasNextInt()) {
                    releaseYear = scanner.nextInt();
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Consume the invalid input
                }
            } while (true);
            scanner.nextLine(); // Consume end line
            newSeries.setReleaseYear(releaseYear);

            System.out.println("Please enter the number of seasons.");
            int numberOfSeasons = 0;
            do {
                if (scanner.hasNextInt()) {
                    numberOfSeasons = scanner.nextInt();
                } else {
                    System.out.println("Invalid input. Please enter a number above 0.");
                    scanner.next(); // Consume the invalid input
                }
            } while (numberOfSeasons < 1);
            scanner.nextLine(); // Consume end line
            newSeries.setNumberOfSeasons(numberOfSeasons);

            Map<String, List<Episode>> seasons = new HashMap<>();
            System.out.println("For each season, let's add the episodes.");
            for (int i = 1; i <= numberOfSeasons; i++) {
                System.out.println("Season " + i + ":");
                List<Episode> episodes = new ArrayList<>();

                System.out.println("How many episodes in Season " + i + "?");
                int numEpisodes = 0;
                do {
                    if (scanner.hasNextInt()) {
                        numEpisodes = scanner.nextInt();
                    } else {
                        System.out.println("Invalid input. Please enter a number above 0.");
                        scanner.next(); // Consume the invalid input
                    }
                } while (numEpisodes < 1);
                scanner.nextLine(); // Consume end line

                for (int j = 1; j <= numEpisodes; j++) {
                    System.out.println("Episode " + j + " name:");
                    String episodeName = scanner.nextLine();

                    System.out.println("Episode " + j + " duration (in minutes):");
                    int duration = scanner.nextInt();
                    scanner.nextLine(); // Consume newline left-over

                    Episode episode = new Episode(episodeName, duration);
                    episodes.add(episode);
                }
                seasons.put("Season " + i, episodes);
            }
            newSeries.setSeasons(seasons);

            try {
                user.addProductionSystem(newSeries);
            } catch (IllegalArgumentException e) {
                System.out.println("Error.");
                return;
            }
            System.out.println("Successfully added series to system!");
        } else {
            System.out.println("Series already in system, failed to add!");
        }
    }

    private void addMovieToSystem(Staff<?> user, IMDB imdb, Scanner scanner) {
        System.out.println("Please type the name of the movie you want to add.");
        String productionTitle = scanner.nextLine();
        if (imdb.searchMovie(productionTitle) == null) {
            Movie newMovie = new Movie(productionTitle);

            System.out.println("Please enter a description for this production.");
            String description = scanner.nextLine();
            newMovie.setDescription(description);

            System.out.println("Let's enter the cast. How many actors?");
            int actorsNumber = 0;
            do {
                if (scanner.hasNextInt()) {
                    actorsNumber = scanner.nextInt();
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Consume the invalid input
                }
            } while (actorsNumber < 1);
            scanner.nextLine(); // Consume end line
            for (int i = 1; i <= actorsNumber; i++) {
                System.out.println("Actor no:" + i + " name:");
                String actorName = scanner.nextLine();
                if (imdb.searchActor(actorName) == null) {
                    System.out.println("Actor does not exist! Please add him to the system before adding him to a production!");
                    System.out.println("Back to menu or try again another actor? Choose between try again or back");
                    do {
                        String input = scanner.nextLine();
                        if (input.equalsIgnoreCase("back")) {
                            return;
                        } else if (input.equalsIgnoreCase("try again")) {
                            i--;
                            break;
                        } else {
                            System.out.println("Choose between try again or back");
                        }
                    } while (true);

                } else {
                    newMovie.addCastToList(actorName);
                }
            }

            System.out.println("Let's add genres. Type a genre and press Enter. Type 'done' to finish.");
            String genreInput;
            while (!(genreInput = scanner.nextLine()).equalsIgnoreCase("done")) {
                if (!newMovie.addGenreAsStringToList(genreInput)) {
                    System.out.println("Please enter a valid genre.");
                }
            }

            System.out.println("Let's add directors. Type a director and press Enter. Type 'done' to finish.");
            String directorInput;
            while (!(directorInput = scanner.nextLine()).equalsIgnoreCase("done")) {
                newMovie.addDirectorToList(directorInput);
            }

            System.out.println("Please enter the release year.");
            int releaseYear;
            do {
                if (scanner.hasNextInt()) {
                    releaseYear = scanner.nextInt();
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Consume the invalid input
                }
            } while (true);
            scanner.nextLine(); // Consume end line
            newMovie.setReleaseYear(releaseYear);

            System.out.println("Please enter the duration in minutes.");
            int duration;
            do {
                if (scanner.hasNextInt()) {
                    duration = scanner.nextInt();
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Consume the invalid input
                }
            } while (true);
            scanner.nextLine(); // Consume end line
            newMovie.setDuration(duration);

            try {
                user.addProductionSystem(newMovie);
            } catch (IllegalArgumentException e) {
                System.out.println("Error.");
                return;
            }
            System.out.println("Successfully added movie to system!");
        } else {
            System.out.println("Movie already in system, failed to add!");
        }
    }

    private void addActorToSystem(Staff<?> user, IMDB imdb, Scanner scanner) {
        System.out.println("Please type the name of the actor you want to add.");
        String actorName = scanner.nextLine();
        if (imdb.searchActor(actorName) == null) {
            System.out.println("Please type your actor's biography:");
            String biography = scanner.nextLine();
            Actor newActor = new Actor();
            newActor.setName(actorName);
            newActor.setBiography(biography);
            System.out.println("How many performances? : ");
            int performanceNumber;
            do {
                if (scanner.hasNextInt()) {
                    performanceNumber = scanner.nextInt();
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Consume the invalid input
                }
            } while (true);
            scanner.nextLine(); // Consume end line

            for (int i = 1; i <= performanceNumber; i++) {
                System.out.println("Performance number : " + i);
                ActorPerformance performance = getActorPerformanceFromInput(scanner);
                newActor.addPerformance(performance);
            }
            try {
                user.addActorSystem(newActor);
            } catch (IllegalArgumentException e) {
                System.out.println("Error.");
                return;
            }
            System.out.println("Successfully added actor to system!");
        } else {
            System.out.println("Actor is already in the system, maybe try to update it!");
        }
    }

    @NotNull
    private ActorPerformance getActorPerformanceFromInput(Scanner scanner) {
        System.out.println("Type the performance's title");
        String performanceTitle = scanner.nextLine();
        System.out.println("Type the performance's type : movie or series");
        String performanceType;
        do {
            performanceType = scanner.nextLine();
            if (performanceType.equalsIgnoreCase("movie")) {
                break;
            } else if (performanceType.equalsIgnoreCase("series")) {
                break;
            } else {
                System.out.println("Please choose between movie or series!");
            }
        } while (true);
        return new ActorPerformance(performanceTitle, performanceType);
    }

    private void showActors(IMDB imdb) {
        Scanner scanner;
        String input;
        System.out.println("Sorted by name? yes/no");
        scanner = new Scanner(System.in);
        do {
            input = scanner.nextLine();
            if (input.equals("yes")) {
                imdb.showActors(imdb.sortActorsByName());
                return;
            } else if (input.equals("no")) {
                imdb.showActors(imdb.getActors());
                return;
            } else {
                System.out.println("Please choose between yes or no.");
            }
        } while (true);
    }

    private void showProductions(IMDB imdb) {
        Scanner scanner;
        scanner = new Scanner(System.in);

        System.out.println("""
                Sort by :\s
                1) Genre (ACTION)
                2) Genre (ADVENTURE)
                3) Genre (COOKING)
                4) Genre (COMEDY)
                5) Genre (DRAMA)
                6) Genre (HORROR)
                7) Genre (SF)
                8) Genre (FANTASY)
                9) Genre (ROMANCE)
                10) Genre (MYSTERY)
                11) Genre (THRILLER)
                12) Genre (CRIME)
                13) Genre (BIOGRAPHY)
                14) Genre (WAR)
                15) Number of ratings""");

        int integerInput;
        do {
            if (scanner.hasNextInt()) {
                integerInput = scanner.nextInt();
                if (integerInput < 1 || integerInput > 15) {
                    System.out.println("Invalid option. Please enter a number between 1 and 15.");
                } else {
                    switch (integerInput) {
                        case 1:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.ACTION));
                            return;
                        case 2:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.ADVENTURE));
                            return;
                        case 3:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.COOKING));
                            return;
                        case 4:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.COMEDY));
                            return;
                        case 5:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.DRAMA));
                            return;
                        case 6:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.HORROR));
                            return;
                        case 7:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.SF));
                            return;
                        case 8:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.FANTASY));
                            return;
                        case 9:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.ROMANCE));
                            return;
                        case 10:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.MYSTERY));
                            return;
                        case 11:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.THRILLER));
                            return;
                        case 12:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.CRIME));
                            return;
                        case 13:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.BIOGRAPHY));
                            return;
                        case 14:
                            imdb.showProductions(imdb.filterProductionsByGenre(Genre.WAR));
                            return;
                        case 15:
                            System.out.println("What is the minimum number of ratings for this?");
                            int minRatings = -1;
                            do {
                                if (scanner.hasNextInt()) {
                                    minRatings = scanner.nextInt();
                                } else {
                                    System.out.println("Invalid input. Please enter a number.");
                                    scanner.next(); // Consume the invalid input
                                }
                            } while (minRatings < 0);
                            imdb.showProductions(imdb.filterProductionsByNumberOfRatings(minRatings));
                            return;
                    }
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume the invalid input
            }
        } while (true);
    }

    private void search(IMDB imdb) {
        Scanner scanner;
        System.out.println("What do you want to search for? Movie/Series/Actor. For exit type exit.");
        scanner = new Scanner(System.in);
        String searchChoice;
        do {
            searchChoice = scanner.nextLine();
            if (searchChoice.equalsIgnoreCase("Movie")) {

                System.out.println("Type the movie's title: ");
                String title = scanner.nextLine();
                Movie movie = imdb.searchMovie(title);
                if (movie != null) {
                    movie.displayInfo();
                } else {
                    System.out.println("Movie not found!");
                }
                break;
            } else if (searchChoice.equalsIgnoreCase("Series")) {
                System.out.println("Type the series's title: ");
                String title = scanner.nextLine();
                Series series = imdb.searchSeries(title);
                if (series != null) {
                    series.displayInfo();
                } else {
                    System.out.println("Series not found!");
                }
                break;
            } else if (searchChoice.equalsIgnoreCase("Actor")) {
                System.out.println("Type the actor's name: ");
                String name = scanner.nextLine();
                Actor actor = imdb.searchActor(name);
                if (actor != null) {
                    actor.displayInfo();
                } else {
                    System.out.println("Actor not found!");
                }
                break;
            } else if (searchChoice.equalsIgnoreCase("exit")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter Movie or Series or Actor. Type exit to exit.");
            }
        } while (true);
    }

    private void createOrDeleteRequestsRegularUser(Regular<?> user, IMDB imdb) {

        String input;
        Scanner scanner;
        System.out.println("Do you want to add or to delete a request? Type add or delete.");
        scanner = new Scanner(System.in);
        do {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("add")) {
                System.out.println("""
                        What type of request would you like to add? Choose between:
                         - delete_account
                         - production_issue
                         - actor_issue
                         - other
                         - back(to go back)""");
                do {
                    input = scanner.nextLine();
                    if (input.equalsIgnoreCase("delete_account")) {
                        System.out.println("Please tell us the reason why do you want to delete your account.");
                        String description = scanner.nextLine();
                        Request request = Request.buildRequest(RequestsTypes.DELETE_ACCOUNT, null, description, user.getUsername());
                        user.createRequest(request);
                        System.out.println("Request created successfully!");
                        return;
                    } else if (input.equalsIgnoreCase("production_issue")) {
                        System.out.println("Please tell us the title of the movie or series you have an issue with.");
                        String title = scanner.nextLine();
                        if (imdb.searchMovie(title) == null && imdb.searchSeries(title) == null) {
                            System.out.println("This movie/series does not exist! Canceling request creation.");
                            return;
                        }
                        System.out.println("Please describe your issue.");
                        String description = scanner.nextLine();
                        Request request = Request.buildRequest(RequestsTypes.MOVIE_ISSUE, title, description, user.getUsername());
                        user.createRequest(request);
                        System.out.println("Request created successfully!");
                        return;
                    } else if (input.equalsIgnoreCase("actor_issue")) {
                        System.out.println("Please tell us the name of the actor you have an issue with. .");
                        String name = scanner.nextLine();
                        if (imdb.searchActor(name) == null) {
                            System.out.println("This actor does not exist! Canceling request creation.");
                            return;
                        }
                        System.out.println("Please describe your issue.");
                        String description = scanner.nextLine();
                        Request request = Request.buildRequest(RequestsTypes.ACTOR_ISSUE, name, description, user.getUsername());
                        user.createRequest(request);
                        System.out.println("Request created successfully!");
                        return;
                    } else if (input.equalsIgnoreCase("other")) {
                        System.out.println("Please tell us your problem.");
                        String description = scanner.nextLine();
                        Request request = Request.buildRequest(RequestsTypes.OTHERS, null, description, user.getUsername());
                        user.createRequest(request);
                        System.out.println("Request created successfully!");
                        return;
                    } else if (input.equalsIgnoreCase("back")) {
                        return;
                    } else {
                        System.out.println("Wrong choice, please choose a correct option!");
                    }
                }
                while (true);
            } else if (input.equalsIgnoreCase("delete")) {
                System.out.println("Your requests are: ");
                if (user.displayPersonalRequests()) {
                    System.out.println("Please type the number of the request you want to delete. To go back type back.");
                    int request_no = 0;
                    do {
                        if (scanner.hasNextInt()) {
                            request_no = scanner.nextInt();
                            if (request_no < 1 || request_no > user.getPersonalRequestsSize()) {
                                System.out.println("Invalid option. Please enter a number between 1 and " + user.getPersonalRequestsSize() + ".");
                            } else {
                                Request request = user.getPersonalRequestFromIndex(request_no);
                                if (!request.getStatus().equals(RequestStatus.ONGOING)) {
                                    System.out.println("You can not delete requests that have been managed.");
                                    return;
                                }
                                user.removeRequest(request);
                                System.out.println("Request deleted successfully!");
                                return;
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number.");
                            if (scanner.next().equalsIgnoreCase("back")) {
                                return;
                            }
                        }
                    } while (request_no < 1 || request_no > user.getPersonalRequestsSize());
                }
                return;
            } else {
                System.out.println("Wrong choice, please choose a correct option!");
            }
        } while (true);
    }

    private void createOrDeleteRequestsContributorUser(Contributor<?> user, IMDB imdb) {

        String input;
        Scanner scanner;
        System.out.println("Do you want to add or to delete a request? Type add or delete.");
        scanner = new Scanner(System.in);
        do {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("add")) {
                System.out.println("""
                        What type of request would you like to add? Choose between:
                         - delete_account
                         - production_issue
                         - actor_issue
                         - other
                         - back(to go back)""");
                do {
                    input = scanner.nextLine();
                    if (input.equalsIgnoreCase("delete_account")) {
                        System.out.println("Please tell us the reason why do you want to delete your account.");
                        String description = scanner.nextLine();
                        Request request = Request.buildRequest(RequestsTypes.DELETE_ACCOUNT, null, description, user.getUsername());
                        user.createRequest(request);
                        System.out.println("Request created successfully!");
                        return;
                    } else if (input.equalsIgnoreCase("production_issue")) {
                        System.out.println("Please tell us the title of the movie or series you have an issue with.");
                        String title = scanner.nextLine();
                        if (imdb.searchMovie(title) == null && imdb.searchSeries(title) == null) {
                            System.out.println("This movie/series does not exist! Canceling request creation.");
                            return;
                        }
                        if (user.findInContributions(title)) {
                            System.out.println("You can not create a request about your own contribution!");
                            return;
                        }
                        System.out.println("Please describe your issue.");
                        String description = scanner.nextLine();
                        Request request = Request.buildRequest(RequestsTypes.MOVIE_ISSUE, title, description, user.getUsername());
                        user.createRequest(request);
                        System.out.println("Request created successfully!");
                        return;
                    } else if (input.equalsIgnoreCase("actor_issue")) {
                        System.out.println("Please tell us the name of the actor you have an issue with. .");
                        String name = scanner.nextLine();
                        if (imdb.searchActor(name) == null) {
                            System.out.println("This actor does not exist! Canceling request creation.");
                            return;
                        }
                        if (user.findInContributions(name)) {
                            System.out.println("You can not create a request about your own contribution!");
                            return;
                        }
                        System.out.println("Please describe your issue.");
                        String description = scanner.nextLine();
                        Request request = Request.buildRequest(RequestsTypes.ACTOR_ISSUE, name, description, user.getUsername());
                        user.createRequest(request);
                        System.out.println("Request created successfully!");
                        return;
                    } else if (input.equalsIgnoreCase("other")) {
                        System.out.println("Please tell us your problem.");
                        String description = scanner.nextLine();
                        Request request = Request.buildRequest(RequestsTypes.OTHERS, null, description, user.getUsername());
                        user.createRequest(request);
                        System.out.println("Request created successfully!");
                        return;
                    } else if (input.equalsIgnoreCase("back")) {
                        return;
                    } else {
                        System.out.println("Wrong choice, please choose a correct option!");
                    }
                }
                while (true);
            } else if (input.equalsIgnoreCase("delete")) {
                System.out.println("Your requests are: ");
                if (user.displayPersonalRequests()) {
                    System.out.println("Please type the number of the request you want to delete. To go back type back.");
                    int request_no = 0;
                    do {
                        if (scanner.hasNextInt()) {
                            request_no = scanner.nextInt();
                            if (request_no < 1 || request_no > user.getPersonalRequestsSize()) {
                                System.out.println("Invalid option. Please enter a number between 1 and " + user.getPersonalRequestsSize() + ".");
                            } else {
                                Request request = user.getPersonalRequestFromIndex(request_no);
                                user.removeRequest(request);
                                System.out.println("Request deleted successfully!");
                                return;
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number.");
                            if (scanner.next().equalsIgnoreCase("back")) {
                                return;
                            }
                        }
                    } while (request_no < 1 || request_no > user.getPersonalRequestsSize());
                }
                return;
            } else {
                System.out.println("Wrong choice, please choose a correct option!");
            }
        } while (true);
    }

    private void modifyFavorites(User user, IMDB imdb) {
        Scanner scanner;
        String input;
        user.displayFavoritesSimple();
        System.out.println("Do you want to add or to remove an actor/production from favourites?");
        scanner = new Scanner(System.in);
        do {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("add")) {
                System.out.println("Choose what do you want to add : actor, movie or series.");
                do {
                    input = scanner.nextLine();
                    if (input.equalsIgnoreCase("actor")) {
                        System.out.println("Please type the name of the actor you want to add.");
                        input = scanner.nextLine();
                        Actor actor = imdb.searchActor(input);
                        if (actor != null) {
                            user.addToFavorites(actor);
                            System.out.println("Successfully added actor to your favourites!");
                        } else {
                            System.out.println("Actor not found, failed to add to favourites!");
                        }
                        return;
                    } else if (input.equalsIgnoreCase("movie")) {
                        System.out.println("Please type the name of the movie you want to add.");
                        input = scanner.nextLine();
                        Movie movie = imdb.searchMovie(input);
                        if (movie != null) {
                            user.addToFavorites(movie);
                            System.out.println("Successfully added movie to your favourites!");
                        } else {
                            System.out.println("Movie not found, failed to add to favourites!");
                        }
                        return;
                    } else if (input.equalsIgnoreCase("series")) {
                        System.out.println("Please type the name of the series you want to add.");
                        input = scanner.nextLine();
                        Series series = imdb.searchSeries(input);
                        if (series != null) {
                            user.addToFavorites(series);
                            System.out.println("Successfully added series to your favourites!");
                        } else {
                            System.out.println("Series not found, failed to add to favourites!");
                        }
                        return;
                    } else {
                        System.out.println("Invalid input, please choose between actor, movie or series.");
                    }
                } while (true);
            } else if (input.equalsIgnoreCase("remove")) {
                System.out.println("Choose what do you want to remove : actor, movie or series.");
                do {
                    input = scanner.nextLine();
                    if (input.equalsIgnoreCase("actor")) {
                        System.out.println("Please type the name of the actor you want to remove.");
                        input = scanner.nextLine();
                        Actor actor = user.searchFavouriteActor(input);
                        if (actor != null) {
                            user.removeActorFromFavourites(actor);
                            System.out.println("Successfully removed actor from your favourites!");
                        } else {
                            System.out.println("Actor not found, failed to remove from favourites!");
                        }
                        return;
                    } else if (input.equalsIgnoreCase("movie")) {
                        System.out.println("Please type the name of the movie you want to remove.");
                        input = scanner.nextLine();
                        Movie movie = user.searchFavouriteMovie(input);
                        if (movie != null) {
                            user.removeProductionFromFavourites(movie);
                            System.out.println("Successfully removed movie from your favourites!");
                        } else {
                            System.out.println("Movie not found, failed to remove from favourites!");
                        }
                        return;
                    } else if (input.equalsIgnoreCase("series")) {
                        System.out.println("Please type the name of the series you want to remove.");
                        input = scanner.nextLine();
                        Series series = user.searchFavouriteSeries(input);
                        if (series != null) {
                            user.removeProductionFromFavourites(series);
                            System.out.println("Successfully removed series from your favourites!");
                        } else {
                            System.out.println("Series not found, failed to remove from favourites!");
                        }
                        return;
                    } else {
                        System.out.println("Invalid input, please choose between actor, movie or series.");
                    }
                } while (true);

            } else {
                System.out.println("Invalid input. Please choose between add or remove.");
            }
        } while (true);
    }

    private void rateProduction(Regular<?> user, IMDB imdb) {
        Scanner scanner;
        String input;
        Production movie;
        Production series;
        Production production;
        scanner = new Scanner(System.in);
        System.out.println("What is the name of the movie or series you want to rate?");
        input = scanner.nextLine();
        movie = imdb.searchMovie(input);
        series = imdb.searchSeries(input);

        System.out.println("Is it a movie or a series?");
        do {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("movie")) {
                if (movie == null) {
                    System.out.println("Movie not found, failed to add a rating");
                    return;
                }
                production = movie;
                break;
            } else if (input.equalsIgnoreCase("series")) {
                if (series == null) {
                    System.out.println("Series not found, failed to add a rating");
                    return;
                }
                production = series;
                break;
            } else {
                System.out.println("Invalid input. Please choose between movie or series.");
            }
        } while (true);

        System.out.println("Do you want to add or to remove a current rating?");
        do {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("add")) {
                System.out.println("Rate this production with a number from 1 to 10.");
                int ratingValue = integerChoice(scanner, 10, 1);
                System.out.println("Please add your comment: ");
                String comment = scanner.nextLine();

                if (production.findRatingByUsername(user.getUsername()) != null) {
                    System.out.println("Rating already added for this production, proceed to update it? yes/no");
                    do {
                        input = scanner.nextLine();
                        if (input.equalsIgnoreCase("yes")) {
                            user.updateRatingForProduction(production, ratingValue, comment);
                            System.out.println("Rating updated successfully!");
                            return;
                        } else if (input.equalsIgnoreCase("no")) {
                            System.out.println("Rating aborted!");
                            return;
                        } else {
                            System.out.println("Invalid input, please choose between yes or no.");
                        }
                    } while (true);
                } else {
                    user.rateProduction(production, ratingValue, comment);
                    System.out.println("Rating added successfully!");
                    return;
                }
            } else if (input.equalsIgnoreCase("remove")) {
                if (production.findRatingByUsername(user.getUsername()) == null) {
                    System.out.println("You did not rate this production, removing rating aborted.");
                    return;
                }
                user.removeRatingForProduction(production);
                System.out.println("Successfully removed rating.");
                return;
            } else {
                System.out.println("Invalid input. Please choose between add or remove.");
            }

        } while (true);
    }

    private void Logout(IMDB imdb) {
        String input;
        Scanner scanner;
        System.out.println("Successfully logged out");
        IMDB.setLoggedIn(false);
        System.out.println("Login or exit?");
        scanner = new Scanner(System.in);
        do {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("Login")) {
                return;
            } else if (input.equalsIgnoreCase("exit")) {
                imdb.setExit(true);
                return;
            } else {
                System.out.println("Please choose between login or exit");
            }
        } while (true);
    }
}
