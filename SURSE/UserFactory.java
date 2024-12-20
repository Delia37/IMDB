package org.example;

import java.util.List;
import java.util.SortedSet;

public interface UserFactory {
    User createUser(AccountType accountType, User.Information information, String username, int experience, List
            notifications, SortedSet<Object> favourites);
}
