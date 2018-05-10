<?php 

	//getting the dboperation class
	require_once '../includes/DbOperation.php';

	//function validating all the parameters are available
	//we will pass the required parameters to this function 
	function areTheseParametersAvailable($params){
		//assuming all parameters are available 
		$available = true; 
		$missingparams = ""; 
		
		foreach($params as $param){
			if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
				$available = false; 
				$missingparams = $missingparams . ", " . $param; 
			}
		}
		
		//if parameters are missing 
		if(!$available){
			$response = array(); 
			$response['error'] = true; 
			$response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';
			
			//displaying error
			echo json_encode($response);
			
			//stopping further execution
			die();
		}
	}
	
	//an array to display response
	$response = array();
	
	//if it is an api call 
	//that means a get parameter named api call is set in the URL 
	//and with this parameter we are concluding that it is an api call
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			//the CREATE operation
			//if the api call value is 'createuser'
			//we will create a record in the database
			case 'createuser':
				//first check the parameters required for this request are available or not 
				areTheseParametersAvailable(array('username', 'userpassword', 'useremail', 'sjsuid'));
				
				//creating a new dboperation object
				$db = new DbOperation();
				
				//creating a new record in the database
				$result = $db->createUser(
					$_POST['username'],
					$_POST['userpassword'],
					$_POST['useremail'],
					$_POST['sjsuid']
				);
				

				//if the record is created adding success to response
				if($result){
					//record is created means there is no error
					$response['error'] = false; 

					//in message we have a success message
					$response['message'] = 'User added successfully';

					//and we are getting all the users from the database in the response
					$response['userdata'] = $db->getAllUsers();
				}else{

					//if record is not added that means there is an error 
					$response['error'] = true; 

					//and we have the error message
					$response['message'] = 'Some error occurred please try again';
				}
				
			break; 
			
			//the READ operation
			//if the call is getallusers
			case 'getallusers':
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'Request successfully completed';
				$response['userdata'] = $db->getAllUsers();
			break; 

			//if the call is getuserbyusername
			case 'getuserbyusername':
				if(isset($_GET['username'])){
					$db = new DbOperation();
					$result = $db->getUserByUserName($_GET['username']);
					if($result){
						$response['error'] = false; 
						$response['message'] = 'Request successfully completed';
						$response['userdata'] = $result;
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Please provide a username';
				}
			break; 

			//if the call is getuserbyuserid
			case 'getuserbyuserid':
				if(isset($_GET['userid'])){
					$db = new DbOperation();
					$result = $db->getUserByUserID($_GET['userid']);
					if($result){
						$response['error'] = false; 
						$response['message'] = 'Request successfully completed';
						$response['userdata'] = $result;
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Please provide a userid';
				}
			break; 

			//the UPDATE operation
			case 'updateuser':
				areTheseParametersAvailable(array('userid', 'username', 'userpassword', 'useremail', 'verified', 'sjsuid'));
				$db = new DbOperation();
				$result = $db->updateUser(
					$_POST['userid'],
					$_POST['username'],
					$_POST['userpassword'],
					$_POST['useremail'],
					$_POST['verified'],
					$_POST['sjsuid']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'User updated successfully';
					$response['userdata'] = $db->getUserByUserName($_POST['username']);
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break; 

			//the UPDATE USER NAME operation
			case 'updateusername':
				areTheseParametersAvailable(array('userid', 'username'));
				$db = new DbOperation();
				$result = $db->updateUserName(
					$_POST['userid'],
					$_POST['username']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'User password updated successfully';
					$response['userdata'] = $db->getUserByUserID($_POST['userid']);
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break;

			//the UPDATE USER PASSWORD operation
			case 'updateuserpassword':
				areTheseParametersAvailable(array('userid', 'userpassword'));
				$db = new DbOperation();
				$result = $db->updateUserPassword(
					$_POST['userid'],
					$_POST['userpassword']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'User password updated successfully';
					$response['userdata'] = $db->getUserByUserID($_POST['userid']);
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break;
			
			//the UPDATE USER VERIFIED operation
			case 'updateuserverified':
				areTheseParametersAvailable(array('userid'));
				$db = new DbOperation();
				$result = $db->updateUserVerified(
					$_POST['userid']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'User password updated successfully';
					$response['userdata'] = $db->getUserByUserID($_POST['userid']);
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break; 

			
			//the delete operation
			case 'deleteuser':
				//for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
				if(isset($_GET['userid'])){
					$db = new DbOperation();
					if($db->deleteUser($_GET['userid'])){
						$response['error'] = false; 
						$response['message'] = 'User deleted successfully';
						$response['userdata'] = $db->getAllUsers();
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Nothing to delete, provide a userid please';
				}
			break; 





			//the create question operation
			case 'createquestion':
				//first check the parameters required for this request are available or not 
				areTheseParametersAvailable(array('questionbody', 'questioncategory', 'questiontype', 'expirationdate', 'userid'));
				
				//creating a new dboperation object
				$db = new DbOperation();
				
				//creating a new record in the database
				$result = $db->createQuestion(
					$_POST['questionbody'],
					$_POST['questioncategory'],
					$_POST['questiontype'],
					$_POST['expirationdate'],
					$_POST['userid']
				);
				
				//if the record is created adding success to response
				if($result){
					//record is created means there is no error
					$response['error'] = false; 

					//in message we have a success message
					$response['message'] = 'Question added successfully';

					//and we are getting all the users from the database in the response
					$response['question'] = $db->getQuestionInfo($_POST['questionbody'], $_POST['questioncategory'], $_POST['questiontype'], $_POST['expirationdate'], $_POST['userid']);
				}else{

					//if record is not added that means there is an error 
					$response['error'] = true; 

					//and we have the error message
					$response['message'] = 'Some error occurred please try again';
				}
			break; 

			//the create question operation
			case 'createquestiondateformat':
				//first check the parameters required for this request are available or not 
				areTheseParametersAvailable(array('questionbody', 'questioncategory', 'questiontype', 'expirationdate', 'userid'));
				
				//creating a new dboperation object
				$db = new DbOperation();
				
				//creating a new record in the database
				$result = $db->createQuestionDateFormat(
					$_POST['questionbody'],
					$_POST['questioncategory'],
					$_POST['questiontype'],
					$_POST['expirationdate'],
					$_POST['userid']
				);
				

				//if the record is created adding success to response
				if($result){
					//record is created means there is no error
					$response['error'] = false; 

					//in message we have a success message
					$response['message'] = 'Question added successfully';

					//and we are getting all the users from the database in the response
					$response['question'] = $db->getQuestionInfoDateFormat($_POST['questionbody'], $_POST['questioncategory'], $_POST['questiontype'], $_POST['expirationdate'], $_POST['userid']);
				}else{

					//if record is not added that means there is an error 
					$response['error'] = true; 

					//and we have the error message
					$response['message'] = 'Some error occurred please try again';
				}
			break; 
			
			//the READ all questions operation
			//if the call is getallquestions
			case 'getallquestions':
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'Request successfully completed';
				$response['question'] = $db->getAllQuestions();
			break;

			//if the call is getrecentquestions
			case 'getrecentquestions':
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'Request successfully completed';
				$response['question'] = $db->getRecentQuestions();
			break; 

			//if the call is gettopquestions
			case 'gettopquestions':
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'Request successfully completed';
				$response['question'] = $db->getTopQuestions();
			break; 
			
			//if the call is getquestion
			case 'getquestion':
				if(isset($_GET['questionid'])){
					$db = new DbOperation();
					$result = $db->getQuestion($_GET['questionid']);
					if($result){
						$response['error'] = false; 
						$response['message'] = 'Request successfully completed';
						$response['question'] = $result;
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Please provide a questionid';
				}
			break;

			//if the call is getquestionsbybody
			case 'getquestionsbybody':
				if(isset($_GET['questionbody'])){
					$db = new DbOperation();
					$result = $db->getQuestionsByBody($_GET['questionbody']);
					if($result){
						$response['error'] = false; 
						$response['message'] = 'Request successfully completed';
						$response['question'] = $result;
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Please provide a questionbody';
				}
			break;

			//if the call is getquestionsbycategory
			case 'getquestionsbycategory':
				if(isset($_GET['questioncategory'])){
					$db = new DbOperation();
					$result = $db->getQuestionsByCategory($_GET['questioncategory']);
					if($result){
						$response['error'] = false; 
						$response['message'] = 'Request successfully completed';
						$response['question'] = $result;
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Please provide a questioncategory';
				}
			break;

			//if the call is getquestionsbytype
			case 'getquestionsbytype':
				if(isset($_GET['questiontype'])){
					$db = new DbOperation();
					$result = $db->getQuestionsByType($_GET['questiontype']);
					if($result){
						$response['error'] = false; 
						$response['message'] = 'Request successfully completed';
						$response['question'] = $result;
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Please provide a questiontype';
				}
			break;

			//if the call is getquestionsbyusefulcount
			case 'getquestionsbyusefulcount':
				if(isset($_GET['usefulcount'])){
					$db = new DbOperation();
					$result = $db->getQuestionsByUsefulCount($_GET['usefulcount']);
					if($result){
						$response['error'] = false; 
						$response['message'] = 'Request successfully completed';
						$response['question'] = $result;
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Please provide a usefulcount';
				}
			break;

			//if the call is getquestionsbyuserid
			case 'getquestionsbyuserid':
				if(isset($_GET['userid'])){
					$db = new DbOperation();
					$result = $db->getQuestionsByUserID($_GET['userid']);
					if($result){
						$response['error'] = false; 
						$response['message'] = 'Request successfully completed';
						$response['question'] = $result;
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Please provide a userid';
				}
			break; 

			//the UPDATE question body operation
			case 'updatequestionbody':
				areTheseParametersAvailable(array('questionid', 'questionbody'));
				$db = new DbOperation();
				$result = $db->updateQuestionBody(
					$_POST['questionid'],
					$_POST['questionbody']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Question updated successfully';
					$response['question'] = $db->getQuestion($_POST['questionid']);
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break;
			
			//the UPDATE question visible operation
			case 'updatequestionvisible':
				areTheseParametersAvailable(array('questionid'));
				$db = new DbOperation();
				$result = $db->updateQuestionVisible(
					$_POST['questionid']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Question updated successfully';
					$response['question'] = $db->getQuestion($_POST['questionid']);
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break;

			//the UPDATE question upvote operation
			case 'updatequestionupvote':
				areTheseParametersAvailable(array('questionid'));
				$db = new DbOperation();
				$result = $db->updateQuestionUpvote(
					$_POST['questionid']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Question updated successfully';
					$response['question'] = $db->getQuestion($_POST['questionid']);
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break;
			
			//the UPDATE question downvote operation
			case 'updatequestiondownvote':
				areTheseParametersAvailable(array('questionid'));
				$db = new DbOperation();
				$result = $db->updateQuestionDownvote(
					$_POST['questionid']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Question updated successfully';
					$response['question'] = $db->getQuestion($_POST['questionid']);
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break;

			//the delete question operation
			case 'deletequestion':
				//for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
				if(isset($_GET['questionid'])){
					$db = new DbOperation();
					if($db->deleteQuestion($_GET['questionid'])){
						$response['error'] = false; 
						$response['message'] = 'Question deleted successfully';
						$response['question'] = $db->getAllQuestions();
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Nothing to delete, provide a questionid please';
				}
			break; 






			//the create question option operation
			case 'createquestionoption':
				//first check the parameters required for this request are available or not 
				areTheseParametersAvailable(array('optionname', 'questionbody', 'userid'));
				
				//creating a new dboperation object
				$db = new DbOperation();
				
				//creating a new record in the database
				$result = $db->createQuestionOption(
					$_POST['optionname'],
					$_POST['questionbody'],
					$_POST['userid']
				);
				

				//if the record is created adding success to response
				if($result){
					//record is created means there is no error
					$response['error'] = false; 

					//in message we have a success message
					$response['message'] = 'Question Option added successfully';

					//and we are getting all the users from the database in the response
					$response['questionoption'] = $db->getAllQuestionOptions();
				}else{

					//if record is not added that means there is an error 
					$response['error'] = true; 

					//and we have the error message
					$response['message'] = 'Some error occurred please try again';
				}	
			break;

			//the create question option id operation
			case 'createquestionoptionid':
				//first check the parameters required for this request are available or not 
				areTheseParametersAvailable(array('optionname', 'questionid'));
				
				//creating a new dboperation object
				$db = new DbOperation();
				
				//creating a new record in the database
				$result = $db->createQuestionOptionID(
					$_POST['optionname'],
					$_POST['questionid']
				);
				

				//if the record is created adding success to response
				if($result){
					//record is created means there is no error
					$response['error'] = false; 

					//in message we have a success message
					$response['message'] = 'Question Option added successfully';

					//and we are getting all the users from the database in the response
					$response['questionoption'] = $db->getAllQuestionOptions();
				}else{

					//if record is not added that means there is an error 
					$response['error'] = true; 

					//and we have the error message
					$response['message'] = 'Some error occurred please try again';
				}	
			break;
			
			//the get ALL question options operation
			case 'getallquestionoptions':
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'Request successfully completed';
				$response['questionoption'] = $db->getAllQuestionOptions();
			break; 

			//the get question options operation
			case 'getquestionoptions':
				if(isset($_GET['questionid'])){
					$db = new DbOperation();
					$result = $db->getQuestionOptions($_GET['questionid']);
					if($result){
						$response['error'] = false; 
						$response['message'] = 'Request successfully completed';
						$response['questionoption'] = $result;
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Please provide a questionid';
				}
			break; 

			//the get question option (singular) operation
			case 'getquestionoption':
				if(isset($_GET['optionid'])){
					$db = new DbOperation();
					$result = $db->getQuestionOption($_GET['optionid']);
					if($result){
						$response['error'] = false; 
						$response['message'] = 'Request successfully completed';
						$response['questionoption'] = $result;
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Please provide an optionid';
				}
			break; 

			// the update question option vote operation
			case 'updatequestionoptionvote':
				areTheseParametersAvailable(array('optionid'));
				$db = new DbOperation();
				$result = $db->updateQuestionOptionVote(
					$_POST['optionid']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Question Option updated successfully';
					$response['questionoption'] = $db->getQuestionOption($_POST['optionid']);
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break;

			// the update question option unvote operation
			case 'updatequestionoptionunvote':
				areTheseParametersAvailable(array('optionid'));
				$db = new DbOperation();
				$result = $db->updateQuestionOptionUnvote(
					$_POST['optionid']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Question Option updated successfully';
					$response['questionoption'] = $db->getQuestionOption($_POST['optionid']);
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break;

			//the delete question option operation
			case 'deletequestionoption':
				//for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
				if(isset($_GET['optionid'])){
					$db = new DbOperation();
					if($db->deleteQuestionOption($_GET['optionid'])){
						$response['error'] = false; 
						$response['message'] = 'Question deleted successfully';
						$response['questionoption'] = $db->getAllQuestionOptions();
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Nothing to delete, provide a questionid please';
				}
			break;





			//if the call is createvotehistory 
			case 'createvotehistory':
				//first check the parameters required for this request are available or not 
				areTheseParametersAvailable(array('userid', 'questionid'));
				
				//creating a new dboperation object
				$db = new DbOperation();
				
				//creating a new record in the database
				$result = $db->createVoteHistory(
					$_POST['userid'],
					$_POST['questionid']
				);
				

				//if the record is created adding success to response
				if($result){
					//record is created means there is no error
					$response['error'] = false; 

					//in message we have a success message
					$response['message'] = 'Vote History added successfully';

					//and we are getting the created votehistory record in the response
					$response['votehistory'] = $db->getVoteHistory($_POST['userid'], $_POST['questionid']);
				}else{

					//if record is not added that means there is an error 
					$response['error'] = true; 

					//and we have the error message
					$response['message'] = 'Some error occurred please try again';
				}	
			break;

			//if the call is getallvotehistory
			case 'getallvotehistory':
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'Request successfully completed';
				$response['votehistory'] = $db->getAllVoteHistory();
			break; 

			//if the call is getvotehistory
			case 'getvotehistory':
				if(isset($_GET['userid'], $_GET['questionid'])){
					$db = new DbOperation();
					$result = $db->getVoteHistory($_GET['userid'], $_GET['questionid']);
					if($result){
						$response['error'] = false; 
						$response['message'] = 'Request successfully completed';
						$response['votehistory'] = $result;
					}else{
						$response['error'] = true; 
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Please provide a userid and a questionid';
				}
			break;

			//if the call is updatevotehistoryhasvoted
			case 'updatevotehistoryhasvoted':
				areTheseParametersAvailable(array('userid', 'questionid'));
				$db = new DbOperation();
				$result = $db->updateVoteHistoryHasVoted(
					$_POST['userid'],
					$_POST['questionid']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'VoteHistory hasVoted updated successfully';
					$response['votehistory'] = $db->getVoteHistory($_POST['userid'], $_POST['questionid']);
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break;

			//if the call is updatevotehistoryhasupvoted
			case 'updatevotehistoryhasupvoted':
				areTheseParametersAvailable(array('userid', 'questionid'));
				$db = new DbOperation();
				$result = $db->updateVoteHistoryHasUpvoted(
					$_POST['userid'],
					$_POST['questionid']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'VoteHistory hasUpvoted updated successfully';
					$response['votehistory'] = $db->getVoteHistory($_POST['userid'], $_POST['questionid']);
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break;

			//if the call is deletevotehistory
			case 'deletevotehistory':
				areTheseParametersAvailable(array('userid', 'questionid'));
				$db = new DbOperation();
				$result = $db->deleteVoteHistory(
					$_POST['userid'],
					$_POST['questionid']
				);

				if($result){
					$response['error'] = false; 
					$response['message'] = 'VoteHistory deleted successfully';
					$response['votehistory'] = $db->getAllVoteHistory();
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				}
			break;

		}
		
	}else{
		//if it is not api call 
		//pushing appropriate values to response array 
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
	}
	
	//displaying the response in json structure 
	echo json_encode($response);

?>
