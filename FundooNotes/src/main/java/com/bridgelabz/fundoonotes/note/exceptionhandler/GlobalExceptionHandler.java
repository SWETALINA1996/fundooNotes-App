package com.bridgelabz.fundoonotes.note.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.fundoonotes.note.exceptions.CreationException;
import com.bridgelabz.fundoonotes.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonotes.note.exceptions.UnAuthorisedAccess;
import com.bridgelabz.fundoonotes.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonotes.user.controllers.UserController;
import com.bridgelabz.fundoonotes.user.models.Response;

public class GlobalExceptionHandler {

	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	  @ExceptionHandler(CreationException.class)
	    public ResponseEntity<Response> handleRegistrationException(CreationException e) {
	       
		  logger.error(e.getMessage());
		  
		  Response response = new Response();
	        response.setMessage(e.getMessage());
	        response.setStatus(-10);
	        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
	    }

	    @ExceptionHandler(NoteNotFoundException.class)
	    public ResponseEntity<Response> handleLoginException(NoteNotFoundException e) {
	    	
	    	logger.error(e.getMessage());
	    	
	        Response response = new Response();
	        response.setMessage(e.getMessage());
	        response.setStatus(-11);
	        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
	    }
	    
	    @ExceptionHandler(UnAuthorisedAccess.class)
	    public ResponseEntity<Response> handleActivationException(UnAuthorisedAccess e) {
	    	
	    	logger.error(e.getMessage());
	    	
	        Response response = new Response();
	        response.setMessage(e.getMessage());
	        response.setStatus(-4);
	        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
	    }
	    
	    @ExceptionHandler(UserNotFoundException.class)
	    public ResponseEntity<Response> handleActivationException(UserNotFoundException e) {
	    	
	    	logger.error(e.getMessage());
	    	
	        Response response = new Response();
	        response.setMessage(e.getMessage());
	        response.setStatus(-4);
	        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
	    }


	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<Response> handleGenericExceptions(Exception e) {
	    	
	    	logger.error(e.toString());
	    	
	        Response response = new Response();
	        response.setMessage("Something went wrong");
	        response.setStatus(-1);
	        return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
}
