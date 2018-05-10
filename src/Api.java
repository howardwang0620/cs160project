
public class Api {

    private static final String ROOT_URL = "http://asksjsu.bitnamiapp.com/askSJSUapi/v1/Api.php?apicall=";

    public static final String URL_CREATE_USER = ROOT_URL + "createuser";
    public static final String URL_GET_ALL_USERS = ROOT_URL + "getallusers";
    public static final String URL_UPDATE_USER = ROOT_URL + "updateuser";
    public static final String URL_DELETE_USER = ROOT_URL + "deleteuser&userid=";
    public static final String URL_CREATE_QUESTION = ROOT_URL + "createquestion";
    public static final String URL_GET_ALL_QUESTIONS = ROOT_URL + "getallquestions";
    public static final String URL_GET_QUESTION = ROOT_URL + "getquestion";
    public static final String URL_UPDATE_QUESTION_BODY = ROOT_URL + "updatequestionbody";
    public static final String URL_UPDATE_QUESTION_VISIBLE = ROOT_URL + "updatequestionvisible";
    public static final String URL_DELETE_QUESTION = ROOT_URL + "deletequestion";
    public static final String URL_CREATE_QUESTION_OPTION = ROOT_URL + "updatehero&questionid=";
    public static final String URL_GET_ALL_QUESTION_OPTIONS = ROOT_URL + "getallquestionoptions";
    public static final String URL_GET_QUESTION_OPTIONS = ROOT_URL + "getquestionoptions";
    public static final String URL_DELETE_QUESTION_OPTION = ROOT_URL + "deletequestionoption&optionid=";

}
