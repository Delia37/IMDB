package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Actor implements Comparable<Object> {

    private String name;
    private List<Map.Entry<String, String>> filmography;
    private String biography;
    private List<String> productions;

    public Actor(String name, List<Map.Entry<String, String>> filmography, String biography) {
        this.name = name;
        this.filmography = filmography;
        this.biography = biography;
    }


    public String getName() {
        return name;
    }

    public List<Map.Entry<String, String>> getFilmography() {
        return filmography;
    }

    public String getBiography() {
        return biography;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getProductions() {
        return productions;
    }

    public void setFilmography(List<Map.Entry<String, String>> filmography) {
        this.filmography = filmography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (o instanceof Actor otherActor) {
            return this.getName().compareTo(otherActor.getName());
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Actor: " + getName();
    }

}
