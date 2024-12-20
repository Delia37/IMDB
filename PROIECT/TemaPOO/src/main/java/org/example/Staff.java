package org.example;

import java.util.*;

public abstract class Staff<T> extends User<T> implements StaffInterface  {

    public SortedSet<Object> addedToSystem;
    public Staff(Information information, AccountType accountType, String username,
                 int experience, List<String> notifications, SortedSet<String> favorites) {
        super(information, accountType, username, experience, (List<String>) notifications, favorites);
        this.addedToSystem = new TreeSet<>();
    }

    @Override
    public abstract void displayMenu(Scanner scanner, List<Production> productions, List<Actor> actors);

    @Override
    public void addProductionSystem(Production production) {
        addedToSystem.add(production);
        System.out.println("Production added: " + production.getTitle());
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

    @Override
    public void updateProduction(Production production) {
        addedToSystem.removeIf(item -> {
            if (item instanceof Production) {
                if (((Production) item).getTitle().equals(production.getTitle())) {
                    // Update production details using setter methods
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
}

