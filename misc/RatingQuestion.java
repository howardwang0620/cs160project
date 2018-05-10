package com.example.andrea.ratingq;

public class RatingQuestion {
    private String title;
    private String question;
    private String creationDate;

    public RatingQuestion(String t, String q,String date){
        title = t;
        question = q;
        creationDate = date;
    }

    public String getTitle() {
        return title;
    }

    public String getQuestion() {
        return question;
    }

    public String getCreationDate(){
        return creationDate;
    }
}
