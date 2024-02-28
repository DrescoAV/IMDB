package org.example;

import java.time.LocalDate;
import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})

public abstract class User<T extends Comparable<T>> implements Observer {

    public static class Information {
        private final Credentials credentials;
        private final String name;
        private final String country;
        private final int age;
        private char gender;
        private LocalDate birthDate;

        private Information(Builder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }

        public static class Builder {
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private char gender;
            private LocalDate birthDate;

            public Builder credentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder country(String country) {
                this.country = country;
                return this;
            }

            public Builder age(int age) {
                this.age = age;
                return this;
            }

            public Builder gender(char gender) {
                this.gender = gender;
                return this;
            }

            public Builder birthDate(LocalDate birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public int getAge() {
            return age;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }
    }


    private Information info;
    private final AccountType accountType;
    private final String username;
    private Integer experience;
    private final List<String> notifications;
    private SortedSet<T> favorites;
    private ExperienceStrategy experienceStrategy;


    public User(String username, AccountType accountType, Information info, Integer experience) {
        this.username = username;
        this.accountType = accountType;
        this.info = info;
        this.experience = experience;
        this.favorites = new TreeSet<>(new SortedComparator());
        this.notifications = new ArrayList<>();
    }

    @Override
    public void update(String message) {
        notifications.add(message);
        if (IMDB.getCurrentUser() != null && IMDB.getCurrentUser().equals(this)) {
            if (!IMDB.isStartGUI()) {
                System.out.println("Notification: " + message);
            } else {
                GUI.getInstance().updateNotificationsPanel();
            }
        }
    }

    public void addToFavorites(T item) {
        if (item != null)
            favorites.add(item);
    }

    public void removeActorFromFavourites(Actor a) {
        if (a != null) {
            Iterator<?> iterator = favorites.iterator();
            while (iterator.hasNext()) {
                T item = (T) iterator.next();
                if (item instanceof Actor actor && actor.getName().equals(a.getName())) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    public void removeProductionFromFavourites(Production p) {
        if (p != null) {
            Iterator<?> iterator = favorites.iterator();
            while (iterator.hasNext()) {
                T item = (T) iterator.next();
                if (item instanceof Production production && production.getTitle().equals(p.getTitle())) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    public void setExperienceStrategy(ExperienceStrategy experienceStrategy) {
        this.experienceStrategy = experienceStrategy;
    }

    public abstract void updateExperience();

    public ExperienceStrategy getExperienceStrategy() {
        return experienceStrategy;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getUsername() {
        return username;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public SortedSet<T> getFavourites() {
        return favorites;
    }

    public void displayFavoritesExpanded() {
        System.out.println("Favorites for user " + this.username + ":");
        for (T item : favorites) {
            if (item instanceof Movie movie) {
                movie.displayInfo();
                System.out.println();
            } else if (item instanceof Series series) {
                series.displayInfo();
                System.out.println();
            } else if (item instanceof Actor actor) {
                actor.displayInfo();
                System.out.println();
            } else {
                System.out.println(item.toString());
            }
            System.out.println();
        }
    }

    public void displayFavoritesSimple() {
        System.out.println("Favorites for user " + this.username + ":\n");
        for (T item : favorites) {
            if (item instanceof Movie movie) {
                System.out.printf("Movie - " + movie.getTitle());
                System.out.println();
            } else if (item instanceof Series series) {
                System.out.printf("Series - " + series.getTitle());
                System.out.println();
            } else if (item instanceof Actor actor) {
                System.out.printf("Actor - " + actor.getName());
                System.out.println();
            } else {
                System.out.println(item.toString());
            }
            System.out.println();
        }
    }

    public Actor searchFavouriteActor(String name) {
        for (T item : favorites) {
            if (item instanceof Actor && ((Actor) item).getName().equals(name)) {
                return (Actor) item;
            }
        }
        return null;
    }

    public Movie searchFavouriteMovie(String title) {
        for (T item : favorites) {
            if (item instanceof Movie && ((Movie) item).getTitle().equals(title)) {
                return (Movie) item;
            }
        }
        return null;
    }

    public Series searchFavouriteSeries(String title) {
        for (T item : favorites) {
            if (item instanceof Series && ((Series) item).getTitle().equals(title)) {
                return (Series) item;
            }
        }
        return null;
    }

    public String getPassword() {
        return info.credentials.getPassword();
    }

    public String getEmail() {
        return info.credentials.getEmail();
    }

    public void showNotifications() {
        for (String notification : notifications) {
            System.out.println(notification);
            System.out.println();
        }
    }

    public static String generateUsername(String fullName) {
        Random random = new Random();
        String userName;
        int randomNumber;
        IMDB imdb = IMDB.getInstance();
        do {
            randomNumber = random.nextInt(random.nextInt(1000));
            userName = fullName.toLowerCase().replace(" ", "_") + "_" + randomNumber;
        } while (imdb.doesUserExist(userName));
        return userName;
    }

    public Information getInfo() {
        return info;
    }

    public void displayAllInfo() {
        System.out.println("Name: " + info.name);
        System.out.println("Country: " + info.country);
        System.out.println("Birthdate: " + info.birthDate);
        System.out.println("Email: " + getEmail());
        System.out.println("Password: " + getPassword());
        System.out.println("Username: " + username);
        System.out.println("Age: " + info.age);
        System.out.println("Account type: " + accountType);
        if (experience != null)
            System.out.println("Experience: " + experience);
    }
}
    