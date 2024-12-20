package org.example;

import java.util.*;

public class ProductionViewer {
    private List<Production> productions;
    private List<Actor> actors;

    public ProductionViewer(List<Production> productions, List<Actor> actors) {
        this.productions = productions;
        this.actors = actors;
    }

    //METODE PENTRU OPTIUNEA 1

    public void viewProductionDetails(Scanner scanner) {
        System.out.println("View Production Details:");
        System.out.println("1. View All Productions");
        System.out.println("2. Filter Productions by Genre");
        System.out.println("3. Filter Productions by Number of Ratings");
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                viewAllProductions();
                break;
            case 2:
                viewProductionsByGenre(scanner);
                break;
            case 3:
                viewProductionsByNumRatings(scanner);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    public void viewAllProductions() {
        System.out.println("All Productions:");
        if (productions != null && !productions.isEmpty()) {
            for (int i = 0; i < productions.size(); i++) {
                System.out.println("Number " + i + ":");
                productions.get(i).displayInfo();
                System.out.println();
            }
        } else {
            System.out.println("No productions available.");
        }
    }

    public void viewProductionsByGenre(Scanner scanner) {
        System.out.println("Enter genre to filter by:");
        String genreStr = scanner.next();

        if (productions != null && !productions.isEmpty()) {
            boolean foundMatchingGenre = false;

            for (int i = 0; i < productions.size(); i++) {
                Production production = productions.get(i);
                for (Genre genre : production.getGenres()) {
                    if (genre.name().equalsIgnoreCase(genreStr)) {
                        System.out.println("Number " + i + ":");
                        production.displayInfo();
                        System.out.println();
                        foundMatchingGenre = true;
                        break;
                    }
                }
            }

            if (!foundMatchingGenre) {
                System.out.println("No productions available for the specified genre.");
            }
        } else {
            System.out.println("No productions available.");
        }
    }

    public void viewProductionsByNumRatings(Scanner scanner) {
        System.out.println("Enter minimum number of ratings:");
        int minRatings = scanner.nextInt();

        if (productions != null && !productions.isEmpty()) {
            for (int i = 0; i < productions.size(); i++) {
                Production production = productions.get(i);
                if (production.getRatings().size() >= minRatings) {
                    System.out.println("Number " + i + ":");
                    production.displayInfo();
                    System.out.println();
                }
            }
        } else {
            System.out.println("No productions available.");
        }
    }

    //METODE PENTRU OPTIUNEA 2
    public void viewActorDetails(Scanner scanner) {
        System.out.println("View Actor Details:");
        System.out.println("1. View All Actors");
        System.out.println("2. Sort Actors by Name");
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                viewAllActors();
                break;
            case 2:
                sortActorsByName();
                viewAllActors();
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    public void viewAllActors() {
        System.out.println("All Actors:");
        if (actors != null && !actors.isEmpty()) {
            for (int i = 0; i < actors.size(); i++) {
                System.out.println("Number " + i + ":");
                displayActorInfo(actors.get(i));
            }
        } else {
            System.out.println("No actors available.");
        }
    }

    private void displayActorInfo(Actor actor) {
        System.out.println("Name: " + actor.getName());
        System.out.println("Biography: " + actor.getBiography());
        System.out.println("Filmography:");

        // Display filmography
        for (Map.Entry<String, String> entry : actor.getFilmography()) {
            System.out.println("  " + entry.getKey() + " (" + entry.getValue() + ")");
        }

        System.out.println();
    }
    public void sortActorsByName() {
        if (actors != null) {
            Collections.sort(actors, Comparator.comparing(Actor::getName));
        }
    }

    //METODE PENTRU OPTIUNEA 4

    public void search(Scanner scanner) {
        System.out.println("Search for a film/series or actor:");
        System.out.println("1. Search for Productions");
        System.out.println("2. Search for Actors");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                searchProductions(scanner);
                break;
            case 2:
                searchActors(scanner);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void searchProductions(Scanner scanner) {
        System.out.print("Enter the title of the production: ");
        scanner.nextLine();  // Consume the newline character
        String title = scanner.nextLine();
        int ok = 0;
        if (productions != null) {
            for (Production production : productions) {
                if (production.getTitle().equals(title)) {
                    production.displayInfo();
                    ok = 1;
                    System.out.println();
                }
            }
            if(ok == 0){
                System.out.println("The production was not found.");
            }
        } else {
            System.out.println("No productions available.");
        }
    }

    private void searchActors(Scanner scanner) {
        System.out.print("Enter the name of the actor: ");
        scanner.nextLine();  // Consume the newline character
        String actorName = scanner.nextLine();
        int ok = 0;
        if (actors != null) {
            for (Actor actor : actors) {
                if(actor.getName().equals(actorName)){
                    displayActorInfo(actor);
                    ok = 1;
                }
            }
            if(ok == 0){
                System.out.println("The actor was not found.");
            }
        } else {
            System.out.println("No actors available.");
        }
    }


}