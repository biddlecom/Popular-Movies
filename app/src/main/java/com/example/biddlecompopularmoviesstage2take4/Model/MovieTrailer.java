package com.example.biddlecompopularmoviesstage2take4.Model;

public class MovieTrailer {

    private String name;
    private String key;

    //Constructor
    public MovieTrailer(String name, String key) {
        this.name = name;
        this.key = key;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
