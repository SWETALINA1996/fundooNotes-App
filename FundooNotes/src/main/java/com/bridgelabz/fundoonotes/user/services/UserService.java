package com.bridgelabz.fundoonotes.user.services;

import javax.mail.MessagingException;
import com.bridgelabz.fundoonotes.user.exceptions.ActivationException;
import com.bridgelabz.fundoonotes.user.exceptions.LoginException;
import com.bridgelabz.fundoonotes.user.exceptions.RegistrationException;
import com.bridgelabz.fundoonotes.user.models.LoginDTO;
import com.bridgelabz.fundoonotes.user.models.RegistrationDTO;

public interface UserService
{
	
	String login(LoginDTO loginDTO) throws LoginException;

	void register(RegistrationDTO registrationDTO) throws RegistrationException, MessagingException;
	
	void activate(String token) throws ActivationException;

}
