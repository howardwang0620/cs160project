package com.example.guest.askSJSU;

public class User {
    private int UserID;
    private String Username; 
    private String UserPassword;
    private String UserEmail;
    private int Verified;
    private int sjsuID;
    private int InviteCode;

    public User(int id, String name, String pass, String em, int ver, int sid, int icode) {
    	this.UserID = id;
    	this.Username = name; 
    	this.UserPassword = pass;
    	this.UserEmail = em;
    	this.Verified = ver;
    	this.sjsuID = sid;
    	this.InviteCode = icode;
    	
    }

    public int getUserId() {
        return UserID;
    }

    public String getUsername() {
        return Username;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public int getVerified() {
        return Verified;
    }

    public int getSjsuID() {
        return sjsuID;
    }

    public int getInviteCode() {
        return InviteCode;
    }
    
}
