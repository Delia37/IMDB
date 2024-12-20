package org.example;


public class Episode {

    private final String episodeName;
    private final int duration;

    public Episode(String episodeName, int duration) {
        this.episodeName = episodeName;
        this.duration = duration;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public int getDuration() {
        return duration;
    }

}
