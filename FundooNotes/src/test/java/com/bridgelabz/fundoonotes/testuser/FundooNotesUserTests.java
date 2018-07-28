package com.bridgelabz.fundoonotes.testuser;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.fundoonotes.FundooNotesApplication;
import com.bridgelabz.fundoonotes.user.controllers.UserController;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = FundooNotesApplication.class)
@SpringBootTest
public class FundooNotesUserTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webAppContext;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();

	}
	
	 @Test
	    public void verifyRegistrationUser() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(
	                "{ \"emailId\": \"swetalina96nayak@gmail.com\", \"userName\" : \"sweta\", \"password\" : \"Sweta@123\" , \"confirmPassword\": \"Sweta@123\",\"mobileNumber\": \"9438449700\"}")
	                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
	                .andExpect(jsonPath("$.status").exists())
	                .andExpect(jsonPath("$.message").value("Successfully Registered"))
	                .andExpect(jsonPath("$.status").value(2)).andDo(print());
	       
	    }

	@Test
    public void verifyLoginUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"emailId\": \"nshwetlina@gmail.com\",\"password\" : \"chocolate@96\"}")
                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").value("successfully logged in"))
                .andExpect(jsonPath("$.status").value(1)).andDo(print());
    }
	/*********************************************************/
	
	 /*************************************
     * login
     *********************************************/
    @Test
    public void loginTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"nshwetlina@gmail.com\", \"password\" : \"chocolate@96\"}")
                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").value("successfully logged in"))
                .andExpect(jsonPath("$.status").value(1));

    }

    @Test
    public void loginTestWithNullValue() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"nshwetlina@gmail.com\"}").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists()).andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").value("Fill all the fields"))
                .andExpect(jsonPath("$.status").value(-2));

    }

    @Test
    public void loginTestWithoutRegisteredEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"nshwetlina@gmail.com\", \"password\" : \"chocolate@96\"}")
                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists()).andExpect(jsonPath("$.message").value("You are not registered with us!!"))
                .andExpect(jsonPath("$.status").value(-2));

    }

    @Test
    public void loginTestWithIncorrectPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/fundoo/login").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"nshwetlina@gmail.com\", \"password\" : \"choc96\"}")
                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists()).andExpect(jsonPath("$.message").value("You have entered a wrorng password"))
                .andExpect(jsonPath("$.status").value(-2));

    }

    @Test
    public void loginTestWithInvalidRegexOfEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/fundoo/login").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"nshwetlina@gmail.com\", \"password\" : \"chocolate@96\"}")
                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").value("Emaild is invalid"))
                .andExpect(jsonPath("$.status").value(-2));

    }

    /**********************************************
     * Register
     ********************************************/
    @Test
    public void verifyRegistrationTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"name\": \"Swetalina\", \"email\" : \"nshwetlina@gmail.com\", \"contactNumber\" : \"7377145005\", \"password\" : \"chocolate@96\", \"confirmPassword\" : \"chocolate@96\" }")
                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").value("Successfully Registered"))
                .andExpect(jsonPath("$.status").value(2));

    }

    @Test
    public void registrationTestWithWrongName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"name\": \"Sn\", \"email\" : \"nshwetlina@gmail.com\", \"contactNumber\" : \"7377145005\", \"password\" : \"Mama1234?\", \"confirmPassword\" : \"Mama1234?\" }")
                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").value("Username should contain more than 3 characters"))
                .andExpect(jsonPath("$.status").value(-3));

    }

    @Test
    public void registrationTestWithNullField() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(
                "{\"email\" : \"nshwetlina@gmail.com\", \"contactNumber\" : \"9110697579\", \"password\" : \"chocolate@96\", \"confirmPassword\" : \"chocolate@96\" }")
                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").value("FillUp all the fields"))
                .andExpect(jsonPath("$.status").value(-3));

    }

    @Test
    public void registrationTestWithWrongContactNum() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"name\": \"Swetalina\", \"email\" : \"nshwetlina@gmail.com\", \"contactNumber\" : \"986442\", \"password\" : \"chocolate@96\", \"confirmPassword\" : \"chocolate@96\" }")
                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").value("Phone Number should be of minimum 10 digits"))
                .andExpect(jsonPath("$.status").value(-3));

    }

    @Test
    public void registrationTestWithUnequalPasswordField() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"name\": \"Swetalina\", \"email\" : \"nshwetlina@gmail.com\", \"contactNumber\" : \"9110697579\", \"password\" : \"chocolate@96\", \"confirmPassword\" : \"chocolate@96\" }")
                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message")
                        .value("Both Passwords does not match.."))
                .andExpect(jsonPath("$.status").value(-3));

    }

    @Test
    public void registrationTestWithInvalidPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"name\": \"Swetalina\", \"email\" : \"nshwetlina@gmail.com\", \"contactNumber\" : \"9110697579\", \"password\" : \"choc\", \"confirmPassword\" : \"chocolate@96\" }")
                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").value("Password must contain 8 characters"))
                .andExpect(jsonPath("$.status").value(-3));

    }

    @Test
    public void registrationTestWithInvalidRegexOfEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(
                "{ \"name\": \"Swetalina\", \"email\" : \"nshwetlinagmailcom\", \"contactNumber\" : \"7377145005\", \"password\" : \"chocolate@96\", \"confirmPassword\" : \"chocolate@96\" }")
                .accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").value("EmailId must be in 'abcd@mail.com' format"))
                .andExpect(jsonPath("$.status").value(-3));

    }
    /*************************************************forgotPassword*****************************************/
    @Test
    public void forgotPasswordTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/forgotpassword").contentType(MediaType.APPLICATION_JSON)
                .content("nshwetlina@gmail.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").value("Link sent to change the password"))
                .andExpect(jsonPath("$.status").value(4));
    }
    
    @Test
    public void resetPasswordTest() throws Exception {
        
        mockMvc.perform(MockMvcRequestBuilders.post("/resetPassword").contentType(MediaType.APPLICATION_JSON)
                .param("token",
                "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI1YjUyYjgwNTQ5MzA0ZDM1Y2ZmZWYyOGYifQ.18aeyklbm4sIG_vFtkBRfQ7xJMaO8L5Jo5qtqhLAmqVdyE5UCMvc8dhOPJosBjSdYlmkUjgj8sEz11piCdVxlw")
                .content(
                        "{ \"newPassword\": \"chocolate.96\", \"confirmNewPassword\" : \"chocolate.96\"}").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").value("Password has been successfully reset."))
                .andExpect(jsonPath("$.status").value(5));
    }


}
