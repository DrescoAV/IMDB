package org.example;

public class UserFactory {

    public static User<?> createUser(AccountType accountType, String username, User.Information info, int experience) {
        return switch (accountType) {
            case REGULAR -> new Regular<>(username, accountType, info, experience);
            case CONTRIBUTOR -> new Contributor<>(username, accountType, info, experience);
            case ADMIN -> new Admin<>(username, accountType, info);
        };
    }
}
