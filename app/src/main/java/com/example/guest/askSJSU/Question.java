package com.example.guest.askSJSU;

import java.sql.Timestamp;

public class Question {
    private int QuestionID;
    private String QuestionBody;
    private String QuestionType;
    private Timestamp DateCreated;
    private Timestamp ExpirationDate;
    private int UsefulCount;
    private int Visible;
    private int UserID;

    public Question() {
        this.QuestionID = 0;
        this.QuestionBody = "";
        this.QuestionType = "";
        this.DateCreated = Timestamp.valueOf("1970-01-01 00:00:01");
        this.ExpirationDate = Timestamp.valueOf("1970-01-01 00:00:01");
        this.UsefulCount = 0;
        this.Visible = 0;
        this.UserID = 0;
    }

    public Question(int id, String body, String type, Timestamp dc, Timestamp ed, int uc, int vis, int uid) {
    	this.QuestionID = id;
    	this.QuestionBody = body;
    	this.QuestionType = type;
    	this.DateCreated = dc;
    	this.ExpirationDate = ed;
    	this.UsefulCount = uc;
    	this.Visible = vis;
    	this.UserID = uid;
    }

    public int getQuestionId() { return QuestionID; }

    public String getQuestionBody() { return QuestionBody; }

    public String getQuestionType() { return QuestionType; }

    public Timestamp getDateCreated() { return DateCreated; }

    public Timestamp getExpirationDate() { return ExpirationDate; }

    public int getUsefulCount() { return UsefulCount; }

    public int getVisible() { return Visible; }

    public int getUserID() { return UserID; }
    
}
