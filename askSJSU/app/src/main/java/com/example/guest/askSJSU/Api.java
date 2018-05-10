package com.example.guest.askSJSU;

public class Api {

    private static final String ROOT_URL = "http://asksjsu.bitnamiapp.com/askSJSUapi/v1/Api.php?apicall=";

    public static final String URL_CREATE_USER = ROOT_URL + "createuser";
    public static final String URL_GET_ALL_USERS = ROOT_URL + "getallusers";
    public static final String URL_GET_USER_BY_USERNAME = ROOT_URL + "getuserbyusername&username=";
    public static final String URL_GET_USER_BY_USERID = ROOT_URL + "getuserbyuserid&userid=";
    public static final String URL_UPDATE_USER = ROOT_URL + "updateuser";
    public static final String URL_UPDATE_USER_NAME = ROOT_URL + "updateusername";
    public static final String URL_UPDATE_USER_PASSWORD = ROOT_URL + "updateuserpassword";
    public static final String URL_UPDATE_USER_VERIFIED = ROOT_URL + "updateuserverified";
    public static final String URL_DELETE_USER = ROOT_URL + "deleteuser&userid=";

    public static final String URL_CREATE_QUESTION = ROOT_URL + "createquestion";
    public static final String URL_GET_ALL_QUESTIONS = ROOT_URL + "getallquestions";
    public static final String URL_GET_RECENT_QUESTIONS = ROOT_URL + "getrecentquestions";
    public static final String URL_GET_TOP_QUESTIONS = ROOT_URL + "gettopquestions";
    public static final String URL_GET_QUESTION = ROOT_URL + "getquestion&questionid=";
    public static final String URL_GET_QUESTIONS_BY_BODY = ROOT_URL + "getquestionsbybody&questionbody=";
    public static final String URL_GET_QUESTIONS_BY_CATEGORY = ROOT_URL + "getquestionsbycategory&questioncategory=";
    public static final String URL_GET_QUESTIONS_BY_TYPE = ROOT_URL + "getquestionsbytype&questiontype=";
    public static final String URL_GET_QUESTIONS_BY_USEFUL_COUNT = ROOT_URL + "getquestionsbyusefulcount&usefulcount=";
    public static final String URL_GET_QUESTIONS_BY_USER_ID = ROOT_URL + "getquestionsbyuserid&userid=";
    public static final String URL_UPDATE_QUESTION_BODY = ROOT_URL + "updatequestionbody";
    public static final String URL_UPDATE_QUESTION_VISIBLE = ROOT_URL + "updatequestionvisible";
    public static final String URL_UPDATE_QUESTION_UPVOTE = ROOT_URL + "updatequestionupvote";
    public static final String URL_UPDATE_QUESTION_DOWNVOTE = ROOT_URL + "updatequestiondownvote";
    public static final String URL_DELETE_QUESTION = ROOT_URL + "deletequestion&questionid=";

    public static final String URL_CREATE_QUESTION_OPTION = ROOT_URL + "createquestionoption";
    public static final String URL_CREATE_QUESTION_OPTION_ID = ROOT_URL + "createquestionoptionid";
    public static final String URL_GET_ALL_QUESTION_OPTIONS = ROOT_URL + "getallquestionoptions";
    public static final String URL_GET_QUESTION_OPTIONS = ROOT_URL + "getquestionoptions&questionid=";
    public static final String URL_GET_QUESTION_OPTION = ROOT_URL + "getquestionoption&optionid=";
    public static final String URL_UPDATE_QUESTION_OPTION_VOTE = ROOT_URL + "updatequestionoptionvote";
    public static final String URL_UPDATE_QUESTION_OPTION_UNVOTE = ROOT_URL + "updatequestionoptionunvote";
    public static final String URL_DELETE_QUESTION_OPTION = ROOT_URL + "deletequestionoption&optionid=";

    public static final String URL_CREATE_VOTE_HISTORY = ROOT_URL + "createvotehistory";
    public static final String URL_GET_ALL_VOTE_HISTORY = ROOT_URL + "getallvotehistory";
    public static final String URL_GET_VOTE_HISTORY = ROOT_URL + "getvotehistory";
    public static final String URL_UPDATE_VOTE_HISTORY_HASVOTED = ROOT_URL + "updatevotehistoryhasvoted";
    public static final String URL_UPDATE_VOTE_HISTORY_HASUPVOTED = ROOT_URL + "updatevotehistoryhasupvoted";
    public static final String URL_DELETE_VOTE_HISTORY = ROOT_URL + "deletevotehistory";

    public static final int CODE_GET_REQUEST = 1024;
    public static final int CODE_POST_REQUEST = 1025;
}
