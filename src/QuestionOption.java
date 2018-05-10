
public class QuestionOption {
    private int OptionID;
    private String OptionName; 
    private int VoteCount;
    private int QuestionID;

    public QuestionOption(int id, String oname, int vc, int qid) {
    	this.OptionID = id;
    	this.OptionName = oname; 
    	this.VoteCount = vc;
    	this.QuestionID = qid;
    	
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
    
}
