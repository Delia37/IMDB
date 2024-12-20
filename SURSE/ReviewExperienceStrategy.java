package org.example;

public class ReviewExperienceStrategy implements ExperienceStrategy {
    private final int rating;

    public ReviewExperienceStrategy(int rating) {
        this.rating = rating;
    }

    @Override
    public int calculateExperience() {
        return rating + 1;
    }
}

