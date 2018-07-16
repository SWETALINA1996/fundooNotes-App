package com.bridgelabz.fundoonotes.user.services;


import java.util.Optional;

import javax.mail.MessagingException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.user.exceptions.ActivationException;
import com.bridgelabz.fundoonotes.user.exceptions.LoginException;
import com.bridgelabz.fundoonotes.user.exceptions.RegistrationException;
import com.bridgelabz.fundoonotes.user.models.Email;
import com.bridgelabz.fundoonotes.user.models.LoginDTO;
import com.bridgelabz.fundoonotes.user.models.RegistrationDTO;
import com.bridgelabz.fundoonotes.user.models.ResetPasswordDTO;
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
	
	@Autowired
	private JWTtokenProvider tokenProvider;
	
	@Autowired
	private EmailService emailService;

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
		else if(!dbUser.isActiveStatus()) {
			throw new LoginException("Please activate your account before login");
		}

		
		return "Successfully Verified";

	}

	@Override
	public void register(RegistrationDTO registrationDTO) throws RegistrationException, MessagingException {

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
		
		optional = userRepository.findByEmailId(registrationDTO.getEmailId());
		if(!optional.isPresent()) {
			throw new RegistrationException("Please register again");
		}
		
		String userId = optional.get().getUserId();

		String generatedToken = tokenProvider.generator(userId);
		System.out.println(generatedToken);
		//nullchecker
		
		Email email = new Email();
		email.setTo(registrationDTO.getEmailId());
		email.setSubject("Link for activation");
		email.setText("http://localhost:8080/activate/?token=" +generatedToken);
		
		emailService.sendEmail(email);
	}

	@Override
	public void activate(String token) throws ActivationException {

		String emailID = tokenProvider.parseJWT(token); 
		//malformed
		
		Optional<User> optional = userRepository.findByEmailId(emailID);
		if(!optional.isPresent()) {
			throw new ActivationException("User not present");
		}
		
		User user = optional.get();
		user.setActiveStatus(true);
		
		userRepository.save(user);
	}


	@Override
	public void forgotPassword(String emailId) throws LoginException, MessagingException {
		
		if(emailId == null) {
			throw new LoginException("Enter valid emailId");
		}
		
		Optional<User> optional = userRepository.findByEmailId(emailId);
		if(!optional.isPresent()) {
			throw new LoginException("User not present");
		}
		
		String userId = optional.get().getUserId();
		String generatedToken = tokenProvider.generator(userId);
	
		Email email = new Email();
		email.setTo(emailId);
		email.setSubject("Reset your Password");
		email.setText("http://localhost:8080/forgotpassword/?token=" +generatedToken);
		
		emailService.sendEmail(email);
		
		
	}
	
	@Override
	public void resetPassword(String token , ResetPasswordDTO password) throws LoginException {
		
		String userId = tokenProvider.parseJWT(token);
		
		Optional<User> optional = userRepository.findById(userId);
		
		if(!password.getPassword().equals(password.getConfirmPassword())){
			throw new LoginException("Password not matching..");
		}
		
		User user = optional.get();
		user.setPassword(encoder.encode(password.getPassword()));
		
		userRepository.save(user);
	}

}
