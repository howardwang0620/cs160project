package com.example.andrea.pollingq;

public class PollingQuestion {
    private String title;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String creationDate;

    public PollingQuestion(String t, String q, String o1, String o2, String o3, String o4, String date){
        title = t;
        question = q;
        option1 = o1;
        option2 = o2;
        option3 = o3;
        option4 = o4;
        creationDate = date;
    }

    public String getTitle() {
        return title;
    }

    public String getQuestion() {
        return question;
    }

    public String getOption1(){
        return option1;
    }

    public String getOption2(){
        return option2;
    }

    public String getOption3(){
        return option3;
    }

    public String getOption4(){
        return option4;
    }

    public String getCreationDate(){
        return creationDate;
    }
}
