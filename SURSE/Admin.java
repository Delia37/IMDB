package org.example;

import java.util.*;

public class Admin<T> extends Staff<Admin>{

    private java.util.List<String> productionsContribution;
    private java.util.List<String> actorsContribution;
    public Admin(Information information, String username,
                 int experience, List notifications, SortedSet<String> favorites) {
        super(information, AccountType.ADMIN, username, experience, (java.util.List<String>) notifications, favorites);
    }

    @Override
    public void displayMenu(Scanner scanner, List<Production> productions, List<Actor> actors) {
        System.out.println("Admin User Menu -> Please choose option:");
        System.out.println("1. View Production Details");
        System.out.println("2. View Actors Details");
        System.out.println("3. View Notifications");
        System.out.println("4. Search for actor/movie/series");
        System.out.println("5. Add/Delete actor/movie/series to/from favourites");
        System.out.println("6. Add/Delete a production/actor from the system");
        System.out.println("7. View and resolve requests");
        System.out.println("8. Update information about actors/productions");
        System.out.println("9. Add/Delete user from to/from the system");
        System.out.println("10. Logout");
        int choice = scanner.nextInt();
        ProductionViewer productionViewer = new ProductionViewer(productions, actors);
        switch (choice) {
            case 1:
                productionViewer.viewProductionDetails(scanner);
                break;
            case 2:
                productionViewer.viewActorDetails(scanner);
                break;
            case 3:
                displayNotifications();
                break;
            case 4:
                productionViewer.search(scanner);
                break;
            case 5:
                manageFavorites(scanner, productions, actors);
                break;
            case 6:
                manageProductionsAndActors(scanner, productions, actors);
                break;
            case 7:
                System.out.println("Not implemented yet :(");
            case 8:
                System.out.println("Not implemented yet :(");
                break;
            case 9:
                System.out.println("Not implemented yet :(");
                break;
            case 10:
                logout();
                break;
            default:
                System.out.println("Invalid choice.");
        }

    }

    private void manageProductionsAndActors(Scanner scanner, List<Production> productions, List<Actor> actors) {
        System.out.println("1. Add a production");
        System.out.println("2. Remove a production");
        System.out.println("3. Add an actor");
        System.out.println("4. Remove an actor");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                addProductions(scanner, productions);
                break;
            case 2:
                removeProduction(scanner, productions);
                break;
            case 3:
                addActor(scanner, actors);
                break;
            case 4:
                removeActor(scanner, actors);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void addProductions(Scanner scanner, List<Production> productions) {
        System.out.println("Enter the title of the production(to check if is on the site):");
        scanner.nextLine(); // Consume the newline character
        String title = scanner.nextLine();

        Production existingProduction = productions.stream()
                .filter(production -> production.getTitle().equals(title))
                .findFirst()
                .orElse(null);

        if (existingProduction != null) {
            System.out.println("Production already exists in the system.");
        } else {
            Production newProduction = createProduction(scanner);
            addProductionSystem(newProduction);
            productions.add(newProduction);
        }
    }

    private void removeProduction(Scanner scanner, List<Production> productions) {
        System.out.println("Enter the title of the production to remove:");
        scanner.nextLine();
        String title = scanner.nextLine();

        removeProductionSystem(title);
        productions.removeIf(production -> production.getTitle().equals(title));
    }

    private void addActor(Scanner scanner, List<Actor> actors) {
        System.out.println("Enter the name of the actor(to check if it's in the system):");
        scanner.nextLine();
        String name = scanner.nextLine();

        Actor existingActor = actors.stream()
                .filter(actor -> actor.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (existingActor != null) {
            System.out.println("Actor already exists in the system.");
        } else {
            Actor newActor = createActor(scanner);
            addActorSystem(newActor);
            actors.add(newActor);
        }
    }

    private void removeActor(Scanner scanner, List<Actor> actors) {
        System.out.println("Enter the name of the actor to remove:");
        scanner.nextLine();
        String name = scanner.nextLine();

        removeActorSystem(name);
        actors.removeIf(actor -> actor.getName().equals(name));
    }

    @Override
    public void addProductionSystem(Production production) {
        if(production != null) {
            addedToSystem.add(production);
            //productions.add(production);
            System.out.println("Production added: " + production.getTitle());
        }
    }

    @Override
    public void addActorSystem(Actor actor) {
        addedToSystem.add(actor);
        System.out.println("Actor added: " + actor.getName());
    }

    @Override
    public void removeProductionSystem(String name) {
        addedToSystem.removeIf(item -> {
            if (item instanceof Production) {
                return ((Production) item).getTitle().equals(name);
            }
            return false;
        });
        System.out.println("Production removed: " + name);
    }

    @Override
    public void removeActorSystem(String name) {
        addedToSystem.removeIf(item -> {
            if (item instanceof Actor) {
                return ((Actor) item).getName().equals(name);
            }
            return false;
        });
        System.out.println("Actor removed: " + name);
    }

    private Production createProduction(Scanner scanner) {
        System.out.println("Enter the title of the production:");
        String title = scanner.nextLine();

        System.out.println("Enter the description of the production:");
        String description = scanner.nextLine();
        System.out.println("Enter the number of directors:");
        int numDirectors = scanner.nextInt();
        List<String> directors = new ArrayList<>();
        System.out.println("Enter the names of the directors:");
        for (int i = 0; i < numDirectors; i++) {
            directors.add(scanner.next());
        }

        System.out.println("Enter the number of actors:");
        int numActors = scanner.nextInt();
        List<String> actors = new ArrayList<>();
        System.out.println("Enter the names of the actors:");
        for (int i = 0; i < numActors; i++) {
            if(i == 0){
                scanner.nextLine();
            }
            actors.add(scanner.nextLine());
        }

        System.out.println("Enter the number of genres:");
        int numGenres = scanner.nextInt();
        List<Genre> genres = new ArrayList<>();
        System.out.println("Enter the names of the genres:");
        for (int i = 0; i < numGenres; i++) {
            genres.add(Genre.valueOf(scanner.next()));
        }

        System.out.println("Enter the type of production (MOVIE or SERIES):");
        Production newProduction;
        String productionType = scanner.next();
        if (productionType.equalsIgnoreCase("MOVIE")) {
            System.out.println("Enter the duration of the movie in minutes:");
            int duration = scanner.nextInt();
            System.out.println("Enter the release year of the movie:");
            int releaseYear = scanner.nextInt();
            newProduction = new Movie(title, directors, actors, genres, new ArrayList<>(), description, duration, releaseYear);
        } else if (productionType.equalsIgnoreCase("SERIES")) {
            System.out.println("Enter the release year of the series:");
            int releaseYear = scanner.nextInt();
            System.out.println("Enter the number of seasons in the series:");
            int numberOfSeasons = scanner.nextInt();
            newProduction = new Series(title, directors, actors, genres, new ArrayList<>(), description, releaseYear, numberOfSeasons, null);
        } else {
            System.out.println("Invalid production type.");
            return null;
        }

        return newProduction;
    }

    private Actor createActor(Scanner scanner) {
        System.out.println("Enter the name of the actor:");
        String name = scanner.nextLine();

        System.out.println("Enter the biography of the actor:");
        String biography = scanner.nextLine();

        System.out.println("Enter the number of entries in the actor's filmography:");
        int numEntries = scanner.nextInt();
        List<Map.Entry<String, String>> filmography = new ArrayList<>();
        System.out.println("Enter the entries in the actor's filmography (e.g., MovieName, RoleName):");
        for (int i = 0; i < numEntries; i++) {
            if(i == 0){
                scanner.nextLine();
            }
            String movieName = scanner.nextLine();
            String roleName = scanner.nextLine();
            filmography.add(Map.entry(movieName, roleName));
        }

        return new Actor(name, filmography, biography);
    }

//    public void addUser(User user) {
//        // Implement logic to add a user
//        // ...
//    }
//
//    public void removeUser(User user) {
//        // Implement logic to remove a user
//        // ...
//    }
//
//    public void addAdminContribution(Production production) {
//        // Implement logic to add a contribution to a production on behalf of an admin
//        // ...
//    }
//
//    public void removeAdminContribution(Production production) {
//        // Implement logic to remove a contribution from a production on behalf of an admin
//        // ...
//    }

    @Override
    public void updateProduction(Production production) {

    }

    @Override
    public void updateActor(Actor actor) {

    }

    public void setProductionsContribution(java.util.List<String> productionsContribution) {
        this.productionsContribution = productionsContribution;
    }

    public void setActorsContribution(java.util.List<String> actorsContribution) {
        this.actorsContribution = actorsContribution;
    }
}

