package org.example;

import java.util.List;

public abstract class Production implements Comparable<Object> {

    private String title;
    private final List<String> directors;
    private final List<String> actors;
    private final List<Genre> genres;
    private final List<Rating> ratings;
    private String description;
    private final double averageRating;

    public Production(String title, List<String> directors, List<String> actors,
                      List<Genre> genres, List<Rating> ratings, String description) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.description = description;
        this.averageRating = calculateAverageRating();
    }

    public String getTitle() {
        return title;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public String getDescription() {
        return description;
    }

    public double getAverageRating() {
        return averageRating;
    }

    private double calculateAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0;
        }

        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getScore();
        }

        return sum / ratings.size();
    }

    public abstract void displayInfo();

     @Override
     public int compareTo(Object o) {
        if (o instanceof Production otherProduction) {
            int titleComparison = this.title.compareTo(otherProduction.title);
            if (titleComparison != 0) {
                return titleComparison;
            }
            return 0;
        }
        return 0;
    }

    public void addRating(Rating rating) {
        if (rating != null) {
            ratings.add(rating);
        }
    }

    public void removeRating(Rating existingRating) {
         if(existingRating != null) {
             ratings.remove(existingRating);
         }
    }

    @Override
    public String toString() {
        return "Production: " + getTitle();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirectors(List<String> directors) {
        this.directors.addAll(directors);
    }

    public void setActors(List<String> actors) {
        this.actors.addAll(actors);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenres(List<Genre> genres) {
        this.genres.addAll(genres);
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings.addAll(ratings);
    }

    public Rating getRatingByUsername(String username) {
        for (Rating rating : ratings) {
            if (rating.getUsername().equals(username)) {
                return rating;
            }
        }
        return null;
    }
}
