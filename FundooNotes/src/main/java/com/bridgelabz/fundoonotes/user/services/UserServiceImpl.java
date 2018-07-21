package com.bridgelabz.fundoonotes.user.services;


import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

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
import com.bridgelabz.fundoonotes.user.rabbitmq.IProducer;
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
	private IProducer producer;


	@Override
	public String login(LoginDTO loginDTO , HttpServletResponse resp) throws LoginException {
		
		Utility.isLoginValidate(loginDTO);
		
		Optional<User> optional = userRepository.findByEmailId(loginDTO.getEmailId());

		if (!optional.isPresent()) {
			throw new LoginException("You are not registered with us!!");
		}

		User dbUser = optional.get();

		if (!(encoder.matches(loginDTO.getPassword(), dbUser.getPassword()))) {
			throw new LoginException("You have entered a wrorng password");
		}
		if(!dbUser.isActiveStatus()) {
			throw new LoginException("Please activate your account before login");
		}

		String generateToken = tokenProvider.generator(dbUser.getUserId());
		resp.addHeader("token", generateToken);
		
		return "Successfully Verified";

	}

	@Override
	public void register(RegistrationDTO registrationDTO) throws RegistrationException, MessagingException {

		Utility.isRegistrationValidate(registrationDTO);
		
		Optional<User> optional = userRepository.findByEmailId(registrationDTO.getEmailId());
		if(optional.isPresent()) {
			throw new RegistrationException("You are already Registered");
		}
		
		User user = new User();
		user.setUserName(registrationDTO.getUserName());
		user.setPassword(encoder.encode(registrationDTO.getConfirmPassword()));
		user.setPhoneNumber(registrationDTO.getPhoneNumber());
		user.setEmailId(registrationDTO.getEmailId());

		userRepository.save(user);
		
		optional = userRepository.findByEmailId(registrationDTO.getEmailId());
		String userId = optional.get().getUserId();

		String generatedToken = tokenProvider.generator(userId);
		System.out.println(generatedToken);
		//nullchecker
		
		Email email = new Email();
		email.setTo(registrationDTO.getEmailId());
		email.setSubject("Link for activation");
		email.setText("http://localhost:8080/activate/?token=" +generatedToken);
		
		producer.produce(email);

		
	}

	@Override
	public void activate(String token) throws ActivationException {
	
		if(token == null ) {
			throw new ActivationException("Please enter a token");
		}

		String emailID = tokenProvider.parseJWT(token); 
		//malformed
		
		Optional<User> optional = userRepository.findById(emailID);
		if(!optional.isPresent()) {
			throw new ActivationException("User not present");
		}
		
		User user = optional.get();
		user.setActiveStatus(true);
		
		userRepository.save(user);
	}


	@Override
	public void forgotPassword(String emailId) throws LoginException, MessagingException {
		
		//validation
		if(emailId == null || !Utility.isValidEmail(emailId)) {
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
		
		//emailService.sendEmail(email);
		producer.produce(email);
		
		
	}
	
	@Override
	public void resetPassword(String token , ResetPasswordDTO passwordDto) throws LoginException {
		
		/*if(token == null || passwordDto.getPassword() == null || !Utility.isValidPassword(passwordDto.getPassword())) {
			
			throw new LoginException("FillUp all the fields properly");
		}*/
		String userId = tokenProvider.parseJWT(token);
		
		Optional<User> optional = userRepository.findById(userId);
		
		if(!passwordDto.getPassword().equals(passwordDto.getConfirmPassword())){
			throw new LoginException("Password not matching..");
		}
		
		User user = optional.get();
		user.setPassword(encoder.encode(passwordDto.getPassword()));
		
		userRepository.save(user);
	}

}
