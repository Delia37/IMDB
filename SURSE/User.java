package org.example;

import java.time.LocalDate;
import java.util.*;

public abstract class User<T> implements Comparable<User<T>> {

    private final Information information;
    private final AccountType accountType;
    private final String username;
    private int experience;
    private final List<String> notifications;
    private final
    SortedSet<String> favorites;

    public User(Information information, AccountType accountType, String username, int experience,
                List<String> notifications, SortedSet<String> favorites) {
        this.information = information;
        this.accountType = accountType;
        this.username = username;
        this.experience = experience;
        this.notifications = (notifications != null) ? notifications : new ArrayList<>();
        this.favorites = favorites;
    }

    @Override
    public int compareTo(User<T> otherUser) {
        return this.getUsername().compareTo(otherUser.getUsername());
    }

    public Information getInformation() {
        return information;
    }

    public int getExperience() {
        return experience;
    }

    public void displayNotifications() {
        System.out.println("Notifications:");
        if (notifications.isEmpty()) {
            System.out.println("No notifications.");
        } else {
            for (String notification : notifications) {
                System.out.println(notification);
            }
        }
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public abstract void displayMenu(Scanner scanner, List<Production> productions, List<Actor> actors);
    // Inner Information class
    public static class Information {
        private final Credentials credentials;
        private final String name;
        private final String country;
        private final int age;
        private final String gender;
        private final LocalDate birthDate;

        // Builder pattern for Information class
        public static class InformationBuilder {
            private final Credentials credentials;
            private String name;
            private String country;
            private int age;
            private String gender;
            private LocalDate birthDate;

            public InformationBuilder(Credentials credentials) {
                this.credentials = credentials;
            }

            public InformationBuilder name(String name) {
                this.name = name;
                return this;
            }

            public InformationBuilder country(String country) {
                this.country = country;
                return this;
            }

            public InformationBuilder age(int age) {
                this.age = age;
                return this;
            }

            public InformationBuilder gender(String gender) {
                this.gender = gender;
                return this;
            }

            public InformationBuilder birthDate(LocalDate birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }

        private Information(InformationBuilder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }

        // Getters for Information class
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

        public String getGender() {
            return gender;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }
    }
    private String generateUsername(String fullName) {
        return fullName.toLowerCase().replace(" ", "_") + "_" + new Random().nextInt(1000);
    }

    public void manageFavorites(Scanner scanner, List<Production> productions, List<Actor> actors) {
        System.out.println("1. Add Production to Favorites");
        System.out.println("2. Add Actor to Favorites");
        System.out.println("3. Remove from Favorites");
        int favChoice = scanner.nextInt();

        switch (favChoice) {
            case 1:
                displayFavorites();
                addToFavoritesProductions(scanner, productions);
                displayFavorites();
                break;
            case 2:
                displayFavorites();
                addToFavoritesActors(scanner, actors);
                displayFavorites();
                break;
            case 3:
                removeFromFavorites(scanner, productions, actors);
                displayFavorites();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void addToFavoritesProductions(Scanner scanner, List<Production> productions) {
        System.out.println("Select a production to add to favorites:");
        ProductionViewer productionViewer = new ProductionViewer(productions, new ArrayList<>());
        productionViewer.viewAllProductions();
        int productionIndex = scanner.nextInt();
        if (productionIndex >= 0 && productionIndex < productions.size()) {
            addToFavorites(String.valueOf(productions.get(productionIndex)));
            System.out.println("Production added to favorites.");
        } else {
            System.out.println("Invalid production index.");
        }
    }

    private void addToFavoritesActors(Scanner scanner, List<Actor> actors) {
        System.out.println("Select an actor to add to favorites:");
        ProductionViewer productionViewer = new ProductionViewer(new ArrayList<>(), actors);
        productionViewer.viewAllActors();
        int actorIndex = scanner.nextInt();
        if (actorIndex >= 0 && actorIndex < actors.size()) {
            addToFavorites(String.valueOf(actors.get(actorIndex)));
            System.out.println("Actor added to favorites.");
        } else {
            System.out.println("Invalid actor index.");
        }
    }

    private void removeFromFavorites(Scanner scanner, List<Production> productions, List<Actor> actors) {
        System.out.println("Select type to remove from favorites:");
        System.out.println("1. Production");
        System.out.println("2. Actor");
        int typeChoice = scanner.nextInt();

        switch (typeChoice) {
            case 1:
                viewAndRemoveFromFavoritesProductions(scanner, productions);
                break;
            case 2:
                viewAndRemoveFromFavoritesActors(scanner, actors);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void viewAndRemoveFromFavoritesProductions(Scanner scanner, List<Production> productions) {
        System.out.println("Select a production to remove from favorites(the number only):");
        displayFavorites();
        int productionIndex = scanner.nextInt();
        if (productionIndex >= 0 && productionIndex < getFavorites().size()) {
            removeFromFavorites((String) getFavorites().toArray()[productionIndex-1]);
            System.out.println("Production removed from favorites.");
        } else {
            System.out.println("Invalid production index.");
        }
    }

    private void viewAndRemoveFromFavoritesActors(Scanner scanner, List<Actor> actors) {
        System.out.println("Select an actor to remove from favorites(the number only):");
        displayFavorites();
        int actorIndex = scanner.nextInt();
        if (actorIndex >= 0 && actorIndex < getFavorites().size()) {
            removeFromFavorites((String) getFavorites().toArray()[actorIndex-1]);
            System.out.println("Actor removed from favorites.");
        } else {
            System.out.println("Invalid actor index.");
        }
    }

    private void addToFavorites(Object element) {
        System.out.println("Adding to favorites: " + element);
        favorites.add((String) element);;
    }



    private void removeFromFavorites(Object element) {
        favorites.remove((String)element);
    }

    public void displayFavorites() {
        System.out.println("Favorites:");
        int index = 1;
        for (Object favorite : getFavorites()) {
            System.out.println(index + ". " + favorite);
            index++;
        }
    }

    private SortedSet<String> getFavorites() {
        return favorites;
    }

    void updateExperienceInternal(int experience) {
        this.experience += experience;
    }

    protected String getUsername() {
        return username;
    }

    public void logout(){
        System.out.println("Loggin out...");
    }

}
