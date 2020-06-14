package com.example.biddlecompopularmoviesstage2take4.Model;

public class MovieReview {

    private String author;
    private String content;

    //Constructor
    public MovieReview(String author, String content) {
        this.author = author;
        this.content = content;
    }

    //Getters and Setters
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
