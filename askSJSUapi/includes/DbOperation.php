<?php
 
class DbOperation
{
	//Database connection link
	private $con;

	//Class constructor
	function __construct()
	{
		//Getting the DbConnect.php file
		require_once dirname(__FILE__) . '/DbConnect.php';

		//Creating a DbConnect object to connect to the database
		$db = new DbConnect();

		//Initializing our connection link of this class
		//by calling the method connect of DbConnect class
		$this->con = $db->connect();
	}
	
	/*
	* The create user operation
	* When this method is called a new user record is created in the database
	*/
	function createUser($username, $userpassword, $useremail, $sjsuid){
		$stmt = $this->con->prepare("INSERT INTO userdata (username, userpassword, useremail, sjsuid) VALUES (?, ?, ?, ?)");
		$stmt->bind_param("sssi", $username, $userpassword, $useremail, $sjsuid);
		if($stmt->execute())
			return true; 
		return false; 
	}

	/*
	* The get all users operation
	* When this method is called it is returning all the existing user records in the database
	*/
	function getAllUsers(){
		$stmt = $this->con->prepare("SELECT userid, username, userpassword, useremail, verified, sjsuid, invitecode FROM userdata");
		$stmt->execute();
		$stmt->bind_result($userid, $username, $userpassword, $useremail, $verified, $sjsuid, $invitecode);
		
		$users = array(); 
		
		while($stmt->fetch()){
			$user  = array();
			$user['userid'] = $userid; 
			$user['username'] = $username;
			$user['userpassword'] = $userpassword; 
			$user['useremail'] = $useremail;
			$user['verified'] = $verified; 
			$user['sjsuid'] = $sjsuid; 
			$user['invitecode'] = $invitecode; 
			
			array_push($users, $user); 
		}
		
		return $users; 
	}
	
	/*
	* The get user operation with username set as parameter
	* When this method is called it is returning a single user record in the database
	*/
	function getUserByUserName($username){
		$stmt = $this->con->prepare("SELECT userid, username, userpassword, useremail, verified, sjsuid, invitecode FROM userdata WHERE username = ?");
		$stmt->bind_param("s", $username);
		$stmt->execute();
		$stmt->bind_result($userid, $username, $userpassword, $useremail, $verified, $sjsuid, $invitecode);
		
		$users = array(); 
		
		while($stmt->fetch()){
			$user  = array();
			$user['userid'] = $userid; 
			$user['username'] = $username;
			$user['userpassword'] = $userpassword; 
			$user['useremail'] = $useremail;
			$user['verified'] = $verified; 
			$user['sjsuid'] = $sjsuid; 
			$user['invitecode'] = $invitecode; 
			
			array_push($users, $user); 
		}
		
		return $users; 
	}


	/*
	* The get user operation with with user id set as parameter
	* When this method is called it is returning a single user record in the database
	*/
	function getUserByUserID($userid){
		$stmt = $this->con->prepare("SELECT userid, username, userpassword, useremail, verified, sjsuid, invitecode FROM userdata WHERE userid = ?");
		$stmt->bind_param("s", $userid);
		$stmt->execute();
		$stmt->bind_result($userid, $username, $userpassword, $useremail, $verified, $sjsuid, $invitecode);
		
		$users = array(); 
		
		while($stmt->fetch()){
			$user  = array();
			$user['userid'] = $userid; 
			$user['username'] = $username;
			$user['userpassword'] = $userpassword; 
			$user['useremail'] = $useremail;
			$user['verified'] = $verified; 
			$user['sjsuid'] = $sjsuid; 
			$user['invitecode'] = $invitecode; 
			
			array_push($users, $user); 
		}
		
		return $users; 
	} 

	/*
	* The update user operation
	* When this method is called the record with the given id is updated with the new given values
	*/
	function updateUser($userid, $username, $userpassword, $useremail, $verified, $sjsuid){
		$stmt = $this->con->prepare("UPDATE userdata SET username = ?, userpassword = ?, useremail = ?, verified = ?, sjsuid = ? WHERE userid = ?");
		$stmt->bind_param("sssiii", $username, $userpassword, $useremail, $verified, $sjsuid, $userid);
		if($stmt->execute())
			return true; 
		return false; 
	}

	/*
	* The update user name operation
	* When this method is called the record with the given id is updated with the new username
	*/
	function updateUserName($userid, $username){
		$stmt = $this->con->prepare("UPDATE userdata SET username = ? WHERE userid = ?");
		$stmt->bind_param("si", $username, $userid);
		if($stmt->execute())
			return true; 
		return false; 
	}

	/*
	* The update user password operation
	* When this method is called the record with the given id is updated with the new password
	*/
	function updateUserPassword($userid, $userpassword){
		$stmt = $this->con->prepare("UPDATE userdata SET userpassword = ? WHERE userid = ?");
		$stmt->bind_param("si", $userpassword, $userid);
		if($stmt->execute())
			return true; 
		return false; 
	}

	/*
	* The update user verified operation
	* When this method is called the record with the given id is updated with verified = 1
	*/
	function updateUserVerified($userid){
		$stmt = $this->con->prepare("UPDATE userdata SET verified = 1 WHERE userid = ?");
		$stmt->bind_param("i", $userid);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
	/*
	* The delete user operation
	* When this method is called record is deleted for the given id 
	*/
	function deleteUser($userid){
		$stmt = $this->con->prepare("DELETE FROM userdata WHERE userid = ?");
		$stmt->bind_param("i", $userid);
		if($stmt->execute())
			return true; 
		
		return false; 
	}





	/*
	* The create question operation
	* When this method is called a new question record is created in the database
	*/
	function createQuestion($questionbody, $questioncategory, $questiontype, $expirationdate, $userid){
		$visible = 0;
		if ($questiontype === "Polling")
			$visible = 1;
		$stmt = $this->con->prepare("INSERT INTO question (questionbody, questioncategory, questiontype, datecreated, expirationdate, visible, userid) VALUES (?, ?, ?, CURRENT_DATE, ?, $visible, ?)");
		$stmt->bind_param("ssssi", $questionbody, $questioncategory, $questiontype, $expirationdate, $userid);
		if($stmt->execute())
			return true; 
		return false; 
	}

	/*
	* The create question date format operation
	* When this method is called a new question record is created in the database, accepts date in format MM/DD/YY
	*/
	function createQuestionDateFormat($questionbody, $questioncategory, $questiontype, $expirationdate, $userid){
		$visible = 0;
		if ($questiontype === "Polling")
			$visible = 1;
		$stmt = $this->con->prepare("INSERT INTO question (questionbody, questioncategory, questiontype, datecreated, expirationdate, visibile, userid) VALUES (?, ?, ?, CURRENT_DATE, STR_TO_DATE(?, '%m/%d/%y'), $visible, ?)");
		$stmt->bind_param("ssssi", $questionbody, $questioncategory, $questiontype, $expirationdate, $userid);
		if($stmt->execute())
			return true; 
		return false; 
	}

	/*
	* The read all question operation
	* When this method is called it is returning all the existing question records in the database
	*/
	function getAllQuestions(){
		$stmt = $this->con->prepare("SELECT questionid, questionbody, questioncategory, questiontype, datecreated, expirationdate, usefulcount, visible, userid FROM question");
		$stmt->execute();
		$stmt->bind_result($questionid, $questionbody, $questioncategory, $questiontype, $datecreated, $expirationdate, $usefulcount, $visible, $userid);
		
		$questions = array(); 
		
		while($stmt->fetch()){
			$question  = array();
			$question['questionid'] = $questionid; 
			$question['questionbody'] = $questionbody;
			$question['questioncategory'] = $questioncategory; 
			$question['questiontype'] = $questiontype;
			$question['datecreated'] = $datecreated;
			$question['expirationdate'] = $expirationdate;
			$question['usefulcount'] = $usefulcount; 
			$question['visible'] = $visible; 
			$question['userid'] = $userid;
			
			array_push($questions, $question); 
		}
		
		return $questions; 
	}

	/*
	* The get top questions operation
	* When this method is called it returns the top question records in the database sorted by usefulcount
	*/
	function getTopQuestions(){
		$stmt = $this->con->prepare("SELECT questionid, questionbody, questioncategory, questiontype, datecreated, expirationdate, usefulcount, visible, userid FROM question WHERE ExpirationDate >= CURRENT_DATE OR Visible = 1 ORDER BY usefulcount DESC LIMIT 10");
		$stmt->execute();
		$stmt->bind_result($questionid, $questionbody, $questioncategory, $questiontype, $datecreated, $expirationdate, $usefulcount, $visible, $userid);
		
		$questions = array(); 
		
		while($stmt->fetch()){
			$question  = array();
			$question['questionid'] = $questionid; 
			$question['questionbody'] = $questionbody;
			$question['questioncategory'] = $questioncategory;
			$question['questiontype'] = $questiontype;
			$question['datecreated'] = $datecreated;
			$question['expirationdate'] = $expirationdate;
			$question['usefulcount'] = $usefulcount; 
			$question['visible'] = $visible; 
			$question['userid'] = $userid;
			
			array_push($questions, $question); 
		}
		
		return $questions; 
	}

	/*
	* The get recent questions operation
	* When this method is called it returns the most recent question records in the database sorted by datecreated
	*/
	function getRecentQuestions(){
		$stmt = $this->con->prepare("SELECT questionid, questionbody, questioncategory, questiontype, datecreated, expirationdate, usefulcount, visible, userid FROM question WHERE ExpirationDate >= CURRENT_DATE OR Visible = 1 ORDER BY questionid DESC LIMIT 10");
		$stmt->execute();
		$stmt->bind_result($questionid, $questionbody, $questioncategory, $questiontype, $datecreated, $expirationdate, $usefulcount, $visible, $userid);
		
		$questions = array(); 
		
		while($stmt->fetch()){
			$question  = array();
			$question['questionid'] = $questionid; 
			$question['questionbody'] = $questionbody;
			$question['questioncategory'] = $questioncategory;
			$question['questiontype'] = $questiontype;
			$question['datecreated'] = $datecreated;
			$question['expirationdate'] = $expirationdate;
			$question['usefulcount'] = $usefulcount; 
			$question['visible'] = $visible; 
			$question['userid'] = $userid;
			
			array_push($questions, $question); 
		}
		
		return $questions; 
	}

	/*
	* The get question operation
	* When this method is called it returns a specific question record in the database
	*/
	function getQuestion($questionid){
		$stmt = $this->con->prepare("SELECT questionid, questionbody, questioncategory, questiontype, datecreated, expirationdate, usefulcount, visible, userid FROM question WHERE questionid = ?");
		$stmt->bind_param("i", $questionid);
		$stmt->execute();
		$stmt->bind_result($questionid, $questionbody, $questioncategory, $questiontype, $datecreated, $expirationdate, $usefulcount, $visible, $userid);
		
		$question = array(); 
		
		if($stmt->fetch()){
			$question['questionid'] = $questionid; 
			$question['questionbody'] = $questionbody;
			$question['questioncategory'] = $questioncategory;
			$question['questiontype'] = $questiontype;
			$question['datecreated'] = $datecreated;
			$question['expirationdate'] = $expirationdate;
			$question['usefulcount'] = $usefulcount; 
			$question['visible'] = $visible; 
			$question['userid'] = $userid; 
		}
		
		return $question; 
	}

	/*
	* The get questions by body operation
	* When this method is called it returns all question records in the database that contain the specified questionbody
	*/
	function getQuestionsByBody($questionbody){
		$stmt = $this->con->prepare("SELECT questionid, questionbody, questioncategory, questiontype, datecreated, expirationdate, usefulcount, visible, userid FROM question WHERE questionbody LIKE ? AND (ExpirationDate >= CURRENT_DATE OR Visible = 1) ORDER BY questionid DESC");
		$like = "%$questionbody%";
		$stmt->bind_param("s", $like);
		$stmt->execute();
		$stmt->bind_result($questionid, $questionbody, $questioncategory, $questiontype, $datecreated, $expirationdate, $usefulcount, $visible, $userid);
		
		$questions = array(); 
		
		while($stmt->fetch()){
			$question = array();
			$question['questionid'] = $questionid; 
			$question['questionbody'] = $questionbody;
			$question['questioncategory'] = $questioncategory;
			$question['questiontype'] = $questiontype;
			$question['datecreated'] = $datecreated;
			$question['expirationdate'] = $expirationdate;
			$question['usefulcount'] = $usefulcount; 
			$question['visible'] = $visible; 
			$question['userid'] = $userid; 
			array_push($questions, $question); 
		}
		
		return $questions; 
	}

	/*
	* The get questions by category operation
	* When this method is called it returns all question records in the database created by the specified questioncategory
	*/
	function getQuestionsByCategory($questioncategory){
		$stmt = $this->con->prepare("SELECT questionid, questionbody, questioncategory, questiontype, datecreated, expirationdate, usefulcount, visible, userid FROM question WHERE questioncategory = ? AND (ExpirationDate >= CURRENT_DATE OR Visible = 1) ORDER BY questionid DESC");
		$stmt->bind_param("s", $questioncategory);
		$stmt->execute();
		$stmt->bind_result($questionid, $questionbody, $questioncategory, $questiontype, $datecreated, $expirationdate, $usefulcount, $visible, $userid);
		
		$questions = array(); 
		
		while($stmt->fetch()){
			$question = array();
			$question['questionid'] = $questionid; 
			$question['questionbody'] = $questionbody;
			$question['questioncategory'] = $questioncategory;
			$question['questiontype'] = $questiontype;
			$question['datecreated'] = $datecreated;
			$question['expirationdate'] = $expirationdate;
			$question['usefulcount'] = $usefulcount; 
			$question['visible'] = $visible; 
			$question['userid'] = $userid; 
			array_push($questions, $question); 
		}
		
		return $questions; 
	}

	/*
	* The get questions by type operation
	* When this method is called it returns all question records in the database created by the specified questiontype
	*/
	function getQuestionsByType($questiontype){
		$stmt = $this->con->prepare("SELECT questionid, questionbody, questioncategory, questiontype, datecreated, expirationdate, usefulcount, visible, userid FROM question WHERE questiontype = ? AND (ExpirationDate >= CURRENT_DATE OR Visible = 1) ORDER BY questionid DESC");
		$stmt->bind_param("s", $questiontype);
		$stmt->execute();
		$stmt->bind_result($questionid, $questionbody, $questioncategory, $questiontype, $datecreated, $expirationdate, $usefulcount, $visible, $userid);
		
		$questions = array(); 
		
		while($stmt->fetch()){
			$question = array();
			$question['questionid'] = $questionid; 
			$question['questionbody'] = $questionbody;
			$question['questioncategory'] = $questioncategory;
			$question['questiontype'] = $questiontype;
			$question['datecreated'] = $datecreated;
			$question['expirationdate'] = $expirationdate;
			$question['usefulcount'] = $usefulcount; 
			$question['visible'] = $visible; 
			$question['userid'] = $userid; 
			array_push($questions, $question); 
		}
		
		return $questions; 
	}

	/*
	* The get questions by usefulcount operation
	* When this method is called it returns all question records in the database with a usefulcount greater than or equal to the specified usefulcount
	*/
	function getQuestionsByUsefulCount($usefulcount){
		$stmt = $this->con->prepare("SELECT questionid, questionbody, questioncategory, questiontype, datecreated, expirationdate, usefulcount, visible, userid FROM question WHERE usefulcount >= ? AND (ExpirationDate >= CURRENT_DATE OR Visible = 1) ORDER BY usefulcount DESC");
		$stmt->bind_param("i", $usefulcount);
		$stmt->execute();
		$stmt->bind_result($questionid, $questionbody, $questioncategory, $questiontype, $datecreated, $expirationdate, $usefulcount, $visible, $userid);
		
		$questions = array(); 
		
		while($stmt->fetch()){
			$question = array();
			$question['questionid'] = $questionid; 
			$question['questionbody'] = $questionbody;
			$question['questioncategory'] = $questioncategory;
			$question['questiontype'] = $questiontype;
			$question['datecreated'] = $datecreated;
			$question['expirationdate'] = $expirationdate;
			$question['usefulcount'] = $usefulcount; 
			$question['visible'] = $visible; 
			$question['userid'] = $userid; 
			array_push($questions, $question); 
		}
		
		return $questions; 
	}


	/*
	* The get questions by userid operation
	* When this method is called it returns all question records in the database created by the specified userid
	*/
	function getQuestionsByUserID($userid){
		$stmt = $this->con->prepare("SELECT questionid, questionbody, questioncategory, questiontype, datecreated, expirationdate, usefulcount, visible, userid FROM question WHERE userid = ? ORDER BY questionid DESC");
		$stmt->bind_param("i", $userid);
		$stmt->execute();
		$stmt->bind_result($questionid, $questionbody, $questioncategory, $questiontype, $datecreated, $expirationdate, $usefulcount, $visible, $userid);
		
		$questions = array(); 
		
		while($stmt->fetch()){
			$question = array();
			$question['questionid'] = $questionid; 
			$question['questionbody'] = $questionbody;
			$question['questioncategory'] = $questioncategory;
			$question['questiontype'] = $questiontype;
			$question['datecreated'] = $datecreated;
			$question['expirationdate'] = $expirationdate;
			$question['usefulcount'] = $usefulcount; 
			$question['visible'] = $visible; 
			$question['userid'] = $userid; 
			array_push($questions, $question); 
		}
		
		return $questions; 
	}

	/*
	* The get question info operation
	* When this method is called it returns a specific question record in the database
	*/
	function getQuestionInfo($questionbody, $questioncategory, $questiontype, $expirationdate, $userid){
		$stmt = $this->con->prepare("SELECT questionid, questionbody, questioncategory, questiontype, datecreated, expirationdate, usefulcount, visible, userid FROM question WHERE questionbody = ? AND questioncategory = ? AND questiontype = ? AND expirationdate = ? AND userid = ?");
		$stmt->bind_param("ssssi", $questionbody, $questioncategory, $questiontype, $expirationdate, $userid);
		$stmt->execute();
		$stmt->bind_result($questionid, $questionbody, $questioncategory, $questiontype, $datecreated, $expirationdate, $usefulcount, $visible, $userid);
		
		$question = array(); 
		
		if($stmt->fetch()){
			$question['questionid'] = $questionid; 
			$question['questionbody'] = $questionbody;
			$question['questioncategory'] = $questioncategory;
			$question['questiontype'] = $questiontype;
			$question['datecreated'] = $datecreated;
			$question['expirationdate'] = $expirationdate;
			$question['usefulcount'] = $usefulcount; 
			$question['visible'] = $visible; 
			$question['userid'] = $userid;
		}
		
		return $question; 
	}

	/*
	* The get question info date format operation
	* When this method is called it returns a specific question record in the database, accepts date in format MM/DD/YY
	*/
	function getQuestionInfoDateFormat($questionbody, $questioncategory, $questiontype, $expirationdate, $userid){
		$stmt = $this->con->prepare("SELECT questionid, questionbody, questioncategory, questiontype, datecreated, expirationdate, usefulcount, visible, userid FROM question WHERE questionbody = ? AND questioncategory = ? AND questiontype = ? AND expirationdate = STR_TO_DATE(?, '%m/%d/%y') AND userid = ?");
		$stmt->bind_param("ssssi", $questionbody, $questioncategory, $questiontype, $expirationdate, $userid);
		$stmt->execute();
		$stmt->bind_result($questionid, $questionbody, $questioncategory, $questiontype, $datecreated, $expirationdate, $usefulcount, $visible, $userid);
		
		$question = array(); 
		
		if($stmt->fetch()){
			$question['questionid'] = $questionid; 
			$question['questionbody'] = $questionbody;
			$question['questioncategory'] = $questioncategory;
			$question['questiontype'] = $questiontype;
			$question['datecreated'] = $datecreated;
			$question['expirationdate'] = $expirationdate;
			$question['usefulcount'] = $usefulcount; 
			$question['visible'] = $visible; 
			$question['userid'] = $userid;
		}
		
		return $question; 
	}


	/*
	* The update question body operation
	* When this method is called the record with the given questionid is updated with the new given values
	*/
	function updateQuestionBody($questionid, $questionbody){
		$stmt = $this->con->prepare("UPDATE question SET questionbody = ? WHERE questionid = ?");
		$stmt->bind_param("si", $questionbody, $questionid);
		if($stmt->execute())
			return true; 
		return false; 
	}

	/*
	* The update question visible operation
	* When this method is called the record with the given questionid is updated to switch the visibility boolean value
	*/
	function updateQuestionVisible($questionid){
		$stmt = $this->con->prepare("UPDATE question SET visible = NOT visible WHERE questionid = ?");
		$stmt->bind_param("i", $questionid);
		if($stmt->execute())
			return true; 
		return false; 
	}

	/*
	* The update question upvote operation
	* When this method is called the record for the given questionid has its usefulcount incremented by 1
	*/
	function updateQuestionUpvote($questionid){
		$stmt = $this->con->prepare("UPDATE question SET usefulcount = usefulcount + 1 WHERE questionid = ?");
		$stmt->bind_param("i", $questionid);
		if($stmt->execute())
			return true; 
		
		return false; 
	}

	/*
	* The update question down operation
	* When this method is called the record for the given questionid has its usefulcount decremented by 1
	*/
	function updateQuestionDownvote($questionid){
		$stmt = $this->con->prepare("UPDATE question SET usefulcount = usefulcount - 1 WHERE questionid = ?");
		$stmt->bind_param("i", $questionid);
		if($stmt->execute())
			return true; 
		
		return false; 
	}
	
	/*
	* The delete question operation
	* When this method is called record is deleted for the given questionid 
	*/
	function deleteQuestion($questionid){
		$stmt = $this->con->prepare("DELETE FROM question WHERE questionid = ?");
		$stmt->bind_param("i", $questionid);
		if($stmt->execute())
			return true; 
		
		return false; 
	}





	/*
	* The create question option operation
	* When this method is called a new question option record is created in the database
	*/
	function createQuestionOption($optionname, $questionbody, $userid){
		$stmt = $this->con->prepare("INSERT INTO questionoption (OptionName, QuestionID) SELECT ?, QuestionID FROM Question WHERE QuestionBody = ? AND UserID = ?");
		$stmt->bind_param("ssi", $optionname, $questionbody, $userid);
		if($stmt->execute())
			return true; 
		return false; 
	}

	function createQuestionOptionID($optionname, $questionid){
		$stmt = $this->con->prepare("INSERT INTO questionoption (optionname, questionid) VALUES (?, ?)");
		$stmt->bind_param("si", $optionname, $questionid);
		if($stmt->execute())
			return true; 
		return false;
	}

	function getAllQuestionOptions(){
		$stmt = $this->con->prepare("SELECT optionid, optionname, votecount, questionid FROM questionoption");
		$stmt->execute();
		$stmt->bind_result($optionid, $optionname, $votecount, $questionid);
		
		$options = array(); 
		
		while($stmt->fetch()){
			$option  = array();
			$option['optionid'] = $optionid; 
			$option['optionname'] = $optionname; 
			$option['votecount'] = $votecount; 
			$option['questionid'] = $questionid; 
			
			array_push($options, $option); 
		}
		
		return $options; 
	}

	/*
	* The read question option operation
	* When this method is called it is returning all the existing question option records in the database for the given questionid
	*/
	function getQuestionOptions($questionid){
		$stmt = $this->con->prepare("SELECT optionid, optionname, votecount, questionid FROM questionoption WHERE questionid = ?");
		$stmt->bind_param("i", $questionid);
		$stmt->execute();
		$stmt->bind_result($optionid, $optionname, $votecount, $questionid);
		
		$options = array(); 
		
		while($stmt->fetch()){
			$option  = array();
			$option['optionid'] = $optionid; 
			$option['optionname'] = $optionname; 
			$option['votecount'] = $votecount; 
			$option['questionid'] = $questionid; 
			
			array_push($options, $option); 
		}
		
		return $options; 
	}

	/*
	* The read question option (singular) operation
	* When this method is called it returns a question option record in the database for the given questionid
	*/
	function getQuestionOption($optionid){
		$stmt = $this->con->prepare("SELECT optionid, optionname, votecount, questionid FROM questionoption WHERE optionid = ?");
		$stmt->bind_param("i", $optionid);
		$stmt->execute();
		$stmt->bind_result($optionid, $optionname, $votecount, $questionid);
		
		$option  = array();
		
		if($stmt->fetch()){
			$option['optionid'] = $optionid; 
			$option['optionname'] = $optionname; 
			$option['votecount'] = $votecount; 
			$option['questionid'] = $questionid; 
		}
		
		return $option; 
	}

	/*
	* The update question option vote operation
	* When this method is called the record for the given questionoptionid has its votecount incremented by 1
	*/
	function updateQuestionOptionVote($optionid){
		$stmt = $this->con->prepare("UPDATE questionoption SET votecount = votecount + 1 WHERE optionid = ?");
		$stmt->bind_param("i", $optionid);
		if($stmt->execute())
			return true; 
		return false; 
	}

	/*
	* The update question option unvote operation
	* When this method is called the record for the given questionoptionid has its votecount decremented by 1
	*/
	function updateQuestionOptionUnvote($optionid){
		$stmt = $this->con->prepare("UPDATE questionoption SET votecount = votecount - 1 WHERE optionid = ? AND votecount > 0");
		$stmt->bind_param("i", $optionid);
		if($stmt->execute())
			return true; 
		return false; 
	}

	/*
	* The delete question option operation
	* When this method is called record is deleted for the given questionid 
	*/
	function deleteQuestionOption($optionid){
		$stmt = $this->con->prepare("DELETE FROM questionoption WHERE optionid = ?");
		$stmt->bind_param("i", $optionid);
		if($stmt->execute())
			return true; 
		
		return false; 
	}

	



	/*
	* The create vote history operation
	* When this method is called a new votehistory record is created in the database
	*/
	function createVoteHistory($userid, $questionid){
		$stmt = $this->con->prepare("INSERT INTO votehistory (userid, questionid) VALUES (?, ?)");
		$stmt->bind_param("ii", $userid, $questionid);
		if($stmt->execute())
			return true; 
		return false; 
	}

	/*
	* The get all vote history operation
	* When this method is called it returns a votehistory record in the database
	*/
	function getAllVoteHistory(){
		$stmt = $this->con->prepare("SELECT userid, questionid, hasvoted, hasupvoted FROM votehistory");
		$stmt->execute();
		$stmt->bind_result($userid, $questionid, $hasvoted, $hasupvoted);
		
		$allvotehistory = array();
		
		while($stmt->fetch()){
			$votehistory = array();
			$votehistory['userid'] = $userid; 
			$votehistory['questionid'] = $questionid; 
			$votehistory['hasvoted'] = $hasvoted; 
			$votehistory['hasupvoted'] = $hasupvoted; 
			
			array_push($allvotehistory, $votehistory); 
		}
		
		return $allvotehistory; 
	}

	/*
	* The get vote history operation
	* When this method is called it returns a votehistory record in the database
	*/
	function getVoteHistory($userid, $questionid){
		$stmt = $this->con->prepare("SELECT userid, questionid, hasvoted, hasupvoted FROM votehistory WHERE userid = ? AND questionid = ?");
		$stmt->bind_param("ii", $userid, $questionid);
		$stmt->execute();
		$stmt->bind_result($userid, $questionid, $hasvoted, $hasupvoted);
		
		$votehistory = array();
		
		if($stmt->fetch()){
			$votehistory['userid'] = $userid; 
			$votehistory['questionid'] = $questionid; 
			$votehistory['hasvoted'] = $hasvoted; 
			$votehistory['hasupvoted'] = $hasupvoted; 
		}
		
		return $votehistory; 
	}

	/*
	* The update vote history hasvoted operation
	* When this method is called the record with the given userid and questionid is updated to switch the hasvoted boolean value
	*/
	function updateVoteHistoryHasVoted($userid, $questionid){
		$stmt = $this->con->prepare("UPDATE votehistory SET hasvoted = NOT hasvoted WHERE userid = ? AND questionid = ?");
		$stmt->bind_param("ii", $userid, $questionid);
		if($stmt->execute())
			return true;
		return false;
	}

	/*
	* The update vote history hasupvoted operation
	* When this method is called the record with the given userid and questionid is updated to switch the hasupvoted boolean value
	*/
	function updateVoteHistoryHasUpvoted($userid, $questionid){
		$stmt = $this->con->prepare("UPDATE votehistory SET hasupvoted = NOT hasupvoted WHERE userid = ? AND questionid = ?");
		$stmt->bind_param("ii", $userid, $questionid);
		if($stmt->execute())
			return true;
		return false;
	}

	/*
	* The delete vote history operation
	* When this method is called the record with the given userid and questionid is deleted 
	*/
	function deleteVoteHistory($userid, $questionid){
		$stmt = $this->con->prepare("DELETE FROM votehistory WHERE userid = ? AND questionid = ?");
		$stmt->bind_param("ii", $userid, $questionid);
		if($stmt->execute())
			return true; 
		return false; 
	}





}

?>
