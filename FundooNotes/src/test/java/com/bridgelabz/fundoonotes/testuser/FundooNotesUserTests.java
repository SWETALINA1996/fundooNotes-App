package com.bridgelabz.fundoonotes.testuser;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
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
	    public void register() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.post("/save").contentType(MediaType.APPLICATION_JSON)
	                .content("{\"userName\":\"Swetalina\",\"email\" :\"nshwetlina@gmail.com\",\"contactNum\":\"9110697579\",\" password\":\"Choc@1234\",\"confirmpassword \":\"Choc@1234\"}")
	                .accept(MediaType.APPLICATION_JSON))
	                .andExpect(jsonPath("$.userName").exists())
	                .andExpect(jsonPath("$.email").exists())
	                .andExpect(jsonPath("$.contactNum").exists())
	                .andExpect(jsonPath("$.password").exists())
	                .andExpect(jsonPath("$.confirmpassword").exists())
	                .andExpect(jsonPath("$.message").value("Registration Successfull!!"))
	                .andExpect(jsonPath("$.status").value(202));
	       
	    }

}
