package com.bridgelabz.fundoonotes.user.services;

import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.user.exceptions.RegistrationException;
import com.bridgelabz.fundoonotes.user.models.LoginDTO;
import com.bridgelabz.fundoonotes.user.models.RegistrationDTO;
import com.bridgelabz.fundoonotes.user.models.User;
import com.bridgelabz.fundoonotes.user.repositories.UserRepository;
import com.bridgelabz.fundoonotes.user.security.JWTtokenProvider;
import com.bridgelabz.fundoonotes.user.utility.Utility;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public String login(LoginDTO loginDTO) throws LoginException {
		Optional<User> optional = userRepository.findByEmailId(loginDTO.getEmailId());

		if (!optional.isPresent()) {
			throw new LoginException("You are not registered with us!!");
		}

		User dbUser = optional.get();

		if (!(encoder.matches(loginDTO.getPassword(), dbUser.getPassword()))) {
			throw new LoginException("You have entered a wrorng password");
		}

		
		return "Successfully Verified";

	}

	@Override
	public void register(RegistrationDTO registrationDTO) throws RegistrationException {

		Optional<User> optional = userRepository.findByEmailId(registrationDTO.getEmailId());
		if(optional.isPresent()) {
			throw new RegistrationException("You are already Registered");
		}
		
		Utility.isRegistrationValidate(registrationDTO);
		
		User user = new User();
		user.setUserName(registrationDTO.getUserName());
		user.setPassword(encoder.encode(registrationDTO.getConfirmPassword()));
		user.setPhoneNumber(registrationDTO.getPhoneNumber());
		user.setEmailId(registrationDTO.getEmailId());

		userRepository.save(user);
		
		JWTtokenProvider token = new JWTtokenProvider();
		token.generator(user);
	}

}
