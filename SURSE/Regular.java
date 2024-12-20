package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;

public class Regular<T> extends User<String> implements RequestsManager, Observer, Subject{
    private List<Observer> observers;
    public Regular(Information information, String username, int experience,
                   List<String> notifications, SortedSet<String> favorites) {
        super(information, AccountType.REGULAR, username, experience, notifications, favorites);
        this.observers = new ArrayList<>();
    }

    public void displayMenu(Scanner scanner, List<Production> productions, List<Actor> actors) {
        System.out.println("Regular User Menu -> Please choose option:");
        System.out.println("1. View Production Details");
        System.out.println("2. View Actors Details");
        System.out.println("3. View Notifications");
        System.out.println("4. Search for actor/movie/series");
        System.out.println("5. Add/Delete actor/movie/series to/from favourites");
        System.out.println("6. Create/Remove Request");
        System.out.println("7. Add/Delete Review for a production");
        System.out.println("8. Logout");
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
                manageRatings(scanner, productions);
                break;
            case 8:
                logout();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void manageRatings(Scanner scanner, List<Production> productions) {
        System.out.println("Manage Ratings:");
        System.out.println("1. Add a Rating");
        System.out.println("2. Remove a Rating");
        int ratingChoice = scanner.nextInt();

        switch (ratingChoice) {
            case 1:
                System.out.println("Your current experience is: " + getExperience());
                addReviewForProduction(scanner, productions);
                System.out.println("Congratulations! You have just gained experience: " + getExperience());
                break;
            case 2:
                removeReviewForProduction(scanner, productions);
                break;
            default:
                System.out.println("Invalid choice.");
        }
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

    @Override
    public void update(String message) {
        System.out.println("Notification: " + message);
    }

    private void addReviewForProduction(Scanner scanner, List<Production> productions) {
        System.out.println("Select a production to leave a review:");
        ProductionViewer productionViewer = new ProductionViewer(productions, new ArrayList<>());
        productionViewer.viewAllProductions();

        int productionIndex = scanner.nextInt();

        if (productionIndex >= 0 && productionIndex < productions.size()) {
            Production selectedProduction = productions.get(productionIndex);

            System.out.print("Enter your rating (1 to 10): ");
            int rating = scanner.nextInt();

            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter your comment: ");
            String comment = scanner.nextLine();
            addReview(selectedProduction, rating, comment);

            System.out.println("Review added successfully.");
        } else {
            System.out.println("Invalid production index.");
        }
    }
    public void addReview(Production production, int rating, String comment) {
        if (rating < 1 || rating > 10) {
            System.out.println("Invalid rating. Rating must be between 1 and 10.");
            return;
        }

        Rating review = new Rating(this.getUsername(), rating, comment);
        production.addRating(review);

        ExperienceStrategy strategy = new ReviewExperienceStrategy(rating);
        updateExperience(strategy);
    }

    private void removeReviewForProduction(Scanner scanner, List<Production> productions) {
        System.out.println("Select a production to remove your review:");
        ProductionViewer productionViewer = new ProductionViewer(productions, new ArrayList<>());
        productionViewer.viewAllProductions();

        int productionIndex = scanner.nextInt();

        if (productionIndex >= 0 && productionIndex < productions.size()) {
            Production selectedProduction = productions.get(productionIndex);

            // Check if the user has already left a review for this production
            Rating existingRating = selectedProduction.getRatingByUsername(this.getUsername());

            if (existingRating != null) {
                selectedProduction.removeRating(existingRating);
                System.out.println("Your review has been removed successfully.");
            } else {
                System.out.println("You haven't left a review for this production.");
            }
        } else {
            System.out.println("Invalid production index.");
        }
    }
    private void updateExperience(ExperienceStrategy strategy) {
        int calculatedExperience = strategy.calculateExperience();
        updateExperienceInternal(calculatedExperience);
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
        scanner.nextLine(); // Consume the newline character
        String requestTypeStr = scanner.nextLine();
        RequestType requestType = RequestType.valueOf(requestTypeStr);

        System.out.println("Enter request description:");
        String description = scanner.nextLine();

        Request request = Request.createGenericRequest(requestType, description, this.getUsername(), null, LocalDateTime.now());

        createRequest(request);
    }

    private void deleteRequest(Scanner scanner) {
        System.out.println("Enter the description of the request to delete:");
        scanner.nextLine(); // Consume the newline character
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

}

