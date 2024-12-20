package org.example;

import java.util.List;
import java.util.Map;

public class Series extends Production {

    private final int releaseYear;
    private final int numberOfSeasons;
    private final Map<String, List<Episode>> seasons;

    public Series(String title, List<String> directors, List<String> actors,
                  List<Genre> genres, List<Rating> ratings, String description,
                  int releaseYear, int numberOfSeasons, Map<String, List<Episode>> seasons) {
        super(title, directors, actors, genres, ratings, description);
        this.releaseYear = releaseYear;
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public Map<String, List<Episode>> getSeasons() {
        return seasons;
    }

    @Override
    public void displayInfo() {
        System.out.println("Series Title: " + getTitle());
        System.out.println("Directors: " + getDirectors());
        System.out.println("Actors: " + getActors());
        System.out.println("Genres: " + getGenres());
        System.out.println("Average Rating: " + getAverageRating());
        System.out.println("Description: " + getDescription());
        System.out.println("Release Year: " + getReleaseYear());
        System.out.println("Number of Seasons: " + getNumberOfSeasons());

        if (getSeasons() != null) {
            for (Map.Entry<String, List<Episode>> entry : seasons.entrySet()) {
                String seasonName = entry.getKey();
                List<Episode> episodes = entry.getValue();

                System.out.println("Season: " + seasonName);
                for (Episode episode : episodes) {
                    System.out.println("  Episode: " + episode.getEpisodeName());
                    System.out.println("  Duration: " + episode.getDuration() + " minutes");
                }
            }
        }

        System.out.println();

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
}

