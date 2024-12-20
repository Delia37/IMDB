package org.example;

import java.util.List;

public class Movie extends Production {

    private final int duration;
    private final int releaseYear;

    public Movie(String title, List<String> directors, List<String> actors,
                 List<Genre> genres, List<Rating> ratings, String description,
                 int duration, int releaseYear) {
        super(title, directors, actors, genres, ratings, description);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public int getDuration() {
        return duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    @Override
    public void displayInfo() {
        System.out.println("Movie Title: " + getTitle());
        System.out.println("Directors: " + getDirectors());
        System.out.println("Actors: " + getActors());
        System.out.println("Genres: " + getGenres());
        System.out.println("Average Rating: " + getAverageRating());
        System.out.println("Description: " + getDescription());
        System.out.println("Duration: " + duration + " minutes");
        System.out.println("Release Year: " + releaseYear);

        displayRatings();

        System.out.println();
    }

    private void displayRatings() {
        List<Rating> ratings = getRatings();
        if (!ratings.isEmpty()) {
            System.out.println("Ratings:");
            for (Rating rating : ratings) {
                System.out.println("User: " + rating.getUsername());
                System.out.println("Score: " + rating.getScore());
                System.out.println("Comment: " + rating.getComments());
                System.out.println("---------------------------");
            }
        } else {
            System.out.println("No ratings available.");
        }
    }

//    @Override
//    public void displayInfo() {
//        System.out.println("Movie Title: " + getTitle());
//        System.out.println("Directors: " + getDirectors());
//        System.out.println("Actors: " + getActors());
//        System.out.println("Genres: " + getGenres());
//        System.out.println("Average Rating: " + getAverageRating());
//        System.out.println("Description: " + getDescription());
//        System.out.println("Duration: " + duration + " minutes");
//        System.out.println("Release Year: " + releaseYear);
//        System.out.println();
//    }

}

