package com.example.guest.askSJSU;

public class QuestionOption {
    private int OptionID;
    private String OptionName; 
    private int VoteCount;
    private int QuestionID;

    public QuestionOption(int OptionID, String OptionName, int VoteCount, int QuestionID) {
    	this.OptionID = OptionID;
    	this.OptionName = OptionName;
    	this.VoteCount = VoteCount;
    	this.QuestionID = QuestionID;
    	
    }

    public int getOptionId() {
        return OptionID;
    }

    public String getOptionName() {
        return OptionName;
    }

    public int getVoteCount() {
        return VoteCount;
    }

    public int getQuestionID() {
        return QuestionID;
    }

    public void vote() { VoteCount++; }
    
}
