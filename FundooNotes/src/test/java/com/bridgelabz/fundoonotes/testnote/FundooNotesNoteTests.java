package com.bridgelabz.fundoonotes.testnote;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.fundoonotes.FundooNotesApplication;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = FundooNotesApplication.class)
@SpringBootTest
public class FundooNotesNoteTests {
	
	private MockMvc mockMvc;
		
		@Autowired
		private WebApplicationContext webAppContext;
		
		@Before
		public void setup() {
	        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();

		}
		
		
		/*public void verifyFundooToCreate() throws Exception {
			
			mockMvc.perform(MockMvcRequestBuilders.post("/create-note")
			        .contentType(MediaType.APPLICATION_JSON)
			        .header("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1YjRkZmE3ZGJlYmJlOTdhZWJiOGE2MGIiLCJpYXQiOjE1MzIzMjYxMjF9.Lz5ZF5Cp4XPJGzY0pc5KyteMxos9g22qQMdZF3s_ys8")
			        .content("{\"title\" : \"New Fundoo Sample\", \"description\" : \"FundooNotes started\"}"))
					.andExpect(jsonPath("$.title").value("New Fundoo Sample"))
					.andExpect(jsonPath("$.description").value("FundooNotes started"))
					.andDo(print());
		}*/
		/*@Test
		public void createNewNoteTest() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.post("/create-note").contentType(MediaType.APPLICATION_JSON).header(
	                "token",
	                "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1YjUxODRkZTgzMzQ2YzM2NjBiZTY0MWYiLCJpYXQiOjE1MzIzOTU3NDUsInN1YiI6IjViNTE4NGRlODMzNDZjMzY2MGJlNjQxZiJ9._HPw5aCnfzaqT3O2Fu-cYJaT5sFKWOVPctmtcnIY5C4")
	                .content(
	                        "{ \"title\" :\"Title Hello\",\"description\":\"Something\"}"))
	                .andExpect(jsonPath("$.title").value("Title Hello"))
	                .andExpect(jsonPath("$.description").value("Something"));
	        // .andExpect(jsonPath("$.createdAt").value("2018-07-23T04:47:57.438Z"))
	        // .andExpect(jsonPath("$.lastUpdated").value("2018-07-23T04:47:57.438Z"))
	        // .andExpect(jsonPath("$.reminder").value(""));
	    }
		*/
	/*	@Test
		public void updateNoteTest() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.post("/update-note").contentType(MediaType.APPLICATION_JSON).header(
	                "token",
	                "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1YjUxODRkZTgzMzQ2YzM2NjBiZTY0MWYiLCJpYXQiOjE1MzIzOTU3NDUsInN1YiI6IjViNTE4NGRlODMzNDZjMzY2MGJlNjQxZiJ9._HPw5aCnfzaqT3O2Fu-cYJaT5sFKWOVPctmtcnIY5C4")
	                .content(
	                       "{ \"title\" :\"Title Hello\",\"description\":\"Something\",\"updatedAt\":\"2018-07-25T04:56:14.154Z\"}"))
	                .andExpect(jsonPath("$.title").value("Title Hello"))
	                .andExpect(jsonPath("$.description").value("Something"))
	                .andExpect(jsonPath("$.message").exists())
	                .andExpect(jsonPath("$.status").exists())
	                .andExpect(jsonPath("$.message").value("Successfully updated Note.."))
	                .andExpect(jsonPath("$.status").value(10)).andDo(print());
	                //.andExpect(jsonPath("$.updatedAt").value(""));
		}*/
		
		/*//@Test
	    public void createNewNoteTest() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.put("/note/create").contentType(MediaType.APPLICATION_JSON).header(
	                "token",
	                "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1YjUxODRkZTgzMzQ2YzM2NjBiZTY0MWYiLCJpYXQiOjE1MzIzOTU3NDUsInN1YiI6IjViNTE4NGRlODMzNDZjMzY2MGJlNjQxZiJ9._HPw5aCnfzaqT3O2Fu-cYJaT5sFKWOVPctmtcnIY5C4")
	                .content("{ \"title\" :\"Title Hello\",\"description\":\"Something\",\"colour\":\"Red\",\"reminder\":\"2018-07-25T04:56:14.154Z\"}"))
	                .andExpect(jsonPath("$.title").value("Title Hello"))
	                .andExpect(jsonPath("$.description").value("Something"))
	                .andExpect(jsonPath("$createdAt").value(new Date()))
	                .andExpect(jsonPath("$lastUpdated").value(new Date()))
	                .andExpect(jsonPath("$reminder").value("2018-07-25T04:56:14.154Z"));
	    }*/

}
