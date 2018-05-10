package com.example.guest.askSJSU;

import java.sql.Date;

public class Question {
    private int QuestionID;
    private String QuestionBody;
    private String QuestionCategory;
    private String QuestionType;
    private Date DateCreated;
    private Date ExpirationDate;
    private int UsefulCount;
    private int Visible;
    private int UserID;

    public Question() {
        this.QuestionID = 0;
        this.QuestionBody = "";
        this.QuestionBody = "";
        this.QuestionType = "";
        this.DateCreated = Date.valueOf("1970-01-01");
        this.ExpirationDate = Date.valueOf("1970-01-01");
        this.UsefulCount = 0;
        this.Visible = 0;
        this.UserID = 0;
    }

    public Question(int id, String body, String category, String type, Date dc, Date ed, int uc, int vis, int uid) {
    	this.QuestionID = id;
    	this.QuestionBody = body;
    	this.QuestionCategory = category;
    	this.QuestionType = type;
    	this.DateCreated = dc;
    	this.ExpirationDate = ed;
    	this.UsefulCount = uc;
    	this.Visible = vis;
    	this.UserID = uid;
    }

    public int getQuestionId() { return QuestionID; }

    public String getQuestionBody() { return QuestionBody; }

    public String getQuestionCategory() { return QuestionCategory; }

    public String getQuestionType() { return QuestionType; }

    public Date getDateCreated() { return DateCreated; }

    public Date getExpirationDate() { return ExpirationDate; }

    public int getUsefulCount() { return UsefulCount; }

    public int getVisible() { return Visible; }

    public int getUserID() { return UserID; }
    
}
