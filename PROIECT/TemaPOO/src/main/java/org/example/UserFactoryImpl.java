package org.example;

import java.util.List;
import java.util.SortedSet;

public class UserFactoryImpl implements UserFactory {
    @Override
    public User createUser(AccountType accountType, User.Information information, String username,
                           int experience, List notifications, SortedSet<Object> favourites) {
        switch (accountType) {
            case REGULAR:
                return new Regular(information, username, experience, (List<String>) notifications, favourites);
            case CONTRIBUTOR:
                return new Contributor(information, username, experience, (List) notifications, favourites );
            case ADMIN:
                return new Admin(information, username, experience, (List) notifications, favourites);
            default:
                throw new IllegalArgumentException("Invalid account type");
        }
    }
}