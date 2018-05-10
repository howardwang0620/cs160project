import java.sql.Timestamp;

public class Question {
    private int QuestionID;
    private String QuestionBody; 
    private Timestamp DateCreated;
    private Timestamp ExpirationDate;
    private int UsefulCount;
    private int Visible;
    private int UserID;

    public Question(int id, String body, Timestamp dc, Timestamp ed, int uc, int vis, int uid) {
    	this.QuestionID = id;
    	this.QuestionBody = body; 
    	this.DateCreated = dc;
    	this.ExpirationDate = ed;
    	this.UsefulCount = uc;
    	this.Visible = vis;
    	this.UserID = uid;
    	
    }

    public int getQuestionId() {
        return QuestionID;
    }

    public String getQuestionBody() {
        return QuestionBody;
    }

    public Timestamp getDateCreated() {
        return DateCreated;
    }

    public Timestamp getExpirationDate() {
        return ExpirationDate;
    }

    public int getUsefulCount() {
        return UsefulCount;
    }

    public int getVisible() {
        return Visible;
    }

    public int getUserID() {
        return UserID;
    }
    
}
