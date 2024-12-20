package org.example;

import java.time.LocalDateTime;
import java.util.*;

public class Contributor<T> extends Staff<Contributor> implements RequestsManager, Observer, Subject {

    private List<String> productionsContribution;
    private List<String> actorsContribution;
    public Contributor(Information information, String username,
                       int experience, List<String> notifications, SortedSet<String> favorites) {
        super(information, AccountType.CONTRIBUTOR, username, experience, notifications, favorites);
    }

    public void displayMenu(Scanner scanner, List<Production> productions, List<Actor> actors) {
        System.out.println("Contributor User Menu -> Please choose option:");
        System.out.println("1. View Production Details");
        System.out.println("2. View Actors Details");
        System.out.println("3. View Notifications");
        System.out.println("4. Search for actor/movie/series");
        System.out.println("5. Add/Delete actor/movie/series to/from favourites");
        System.out.println("6. Create/Remove Request");
        System.out.println("7. Add/Delete a production/actor from the system");
        System.out.println("8. View and resolve requests");
        System.out.println("9. Update information about actors/productions");
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
                manageRequests(scanner);
                break;
            case 7:
                manageProductionsAndActors(scanner, productions, actors);
                break;
            case 8:
                System.out.println("8");
                break;
            case 9:
                System.out.println("9");
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
        scanner.nextLine();
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

    // Method to add an actor
    private void addActor(Scanner scanner, List<Actor> actors) {
        System.out.println("Enter the name of the actor to add:");
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

    @Override
    public void updateProduction(Production production) {
        addedToSystem.removeIf(item -> {
            if (item instanceof Production) {
                if (((Production) item).getTitle().equals(production.getTitle())) {
                    Production prod = (Production) item;
                    prod.setTitle(production.getTitle());
                    prod.setDirectors(production.getDirectors());
                    prod.setActors(production.getActors());
                    prod.setGenres(production.getGenres());
                    prod.setRatings(production.getRatings());
                    prod.setDescription(production.getDescription());
                    System.out.println("Production updated: " + production.getTitle());
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public void updateActor(Actor actor) {
        addedToSystem.removeIf(item -> {
            if (item instanceof Actor) {
                if (((Actor) item).getName().equals(actor.getName())) {
                    ((Actor) item).setBiography(actor.getBiography());
                    ((Actor) item).setFilmography(actor.getFilmography());
                    System.out.println("Actor updated: " + actor.getName());
                    return true;
                }
            }
            return false;
        });
    }


    @Override
    public void displayRequests() {
        List<Request> allRequests = RequestsHolder.getAllRequests();
        System.out.println("All Requests:");
        for (Request request : allRequests) {
            System.out.println(request.getDescription());
        }
    }

    @Override
    public void createRequest(Request request) {
        RequestsHolder.addRequest(request);

        notifyObservers("A new request has been created.");
    }
    @Override
    public void removeRequest(Request request) {
        RequestsHolder.removeRequest(request);
        notifyObservers("A request has been removed.");
    }

    @Override
    public void notifyObservers(String str){
        System.out.println(str);
    }

    private void manageRequests(Scanner scanner) {
        System.out.println("1. Create a Request");
        System.out.println("2. Delete a Request");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                createRequest(scanner);
                break;
            case 2:
                displayRequests();
                deleteRequest(scanner);
                displayRequests();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void createRequest(Scanner scanner) {
        System.out.println("Enter request type (DELETE_ACCOUNT, ACTOR_ISSUE, MOVIE_ISSUE, OTHERS):");
        String requestTypeStr = scanner.nextLine();
        RequestType requestType = RequestType.valueOf(requestTypeStr);

        System.out.println("Enter request description:");
        String description = scanner.nextLine();

        Request request = Request.createGenericRequest(requestType, description, this.getUsername(), null, LocalDateTime.now());

        createRequest(request);
    }

    private void deleteRequest(Scanner scanner) {
        System.out.println("Enter the description of the request to delete:");
        scanner.nextLine();
        String description = scanner.nextLine();

        List<Request> allRequests = RequestsHolder.getAllRequests();
        Request requestToDelete = allRequests.stream()
                .filter(request -> request.getDescription().equals(description))
                .findFirst()
                .orElse(null);

        if (requestToDelete != null) {
            removeRequest(requestToDelete);
        } else {
            System.out.println("Request not found.");
        }
    }


    @Override
    public void update(String message) {
        System.out.println("Notification: " + message);
    }

    public void setProductionsContribution(List<String> productionsContribution) {
        this.productionsContribution = productionsContribution;
    }

    public void setActorsContribution(List<String> actorsContribution) {
        this.actorsContribution = actorsContribution;
    }

}
