package com.bridgelabz.fundoonotes.user.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.fundoonotes.user.exceptions.LoginException;
import com.bridgelabz.fundoonotes.user.exceptions.RegistrationException;
import com.bridgelabz.fundoonotes.user.models.Response;

@ControllerAdvice
public class GlobalExceptionHandler {

	  @ExceptionHandler(RegistrationException.class)
	    public ResponseEntity<Response> handleRegistrationException(RegistrationException e) {
	        Response response = new Response();
	        response.setMessage(e.getMessage());
	        response.setStatus(-3);
	        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
	    }

	    @ExceptionHandler(LoginException.class)
	    public ResponseEntity<Response> handleLoginException(LoginException e) {
	        Response response = new Response();
	        response.setMessage(e.getMessage());
	        response.setStatus(-2);
	        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<Response> handleGenericExceptions(Exception e) {
	        Response response = new Response();
	        response.setMessage("Something went wrong");
	        response.setStatus(-1);
	        return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	
}
