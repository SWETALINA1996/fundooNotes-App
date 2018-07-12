package com.bridgelabz.fundoonotes.user.controllers;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.user.exceptions.RegistrationException;
import com.bridgelabz.fundoonotes.user.models.LoginDTO;
import com.bridgelabz.fundoonotes.user.models.RegistrationDTO;
import com.bridgelabz.fundoonotes.user.models.Response;
import com.bridgelabz.fundoonotes.user.services.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Response> login(@RequestBody LoginDTO loginDTO) throws LoginException {
		userService.login(loginDTO);
		Response dto = new Response();
		dto.setMessage("successfully logged in with email: " + loginDTO.getEmailId());
		dto.setStatus(1);
		return new ResponseEntity<Response>(dto, HttpStatus.OK);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Response> register(@RequestBody RegistrationDTO registrationDTO)
			throws RegistrationException {
		
		userService.register(registrationDTO);
		Response dto = new Response();
		dto.setMessage("Successfully Registered");
		dto.setStatus(2);
		return new ResponseEntity<Response>(dto, HttpStatus.CREATED);
	}

}
