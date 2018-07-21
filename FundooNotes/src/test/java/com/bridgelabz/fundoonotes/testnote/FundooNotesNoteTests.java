package com.bridgelabz.fundoonotes.testnote;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Date;

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

public class FundooNotesNoteTests {
	
	private MockMvc mockMvc;
		
		@Autowired
		private WebApplicationContext webAppContext;
		
		@Before
		public void setup() {
	        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();

		}
		
		@Test
		public void verifyFundooToCreate() throws Exception {
			Date date = new Date();
			mockMvc.perform(MockMvcRequestBuilders.post("/create-note")
			        .contentType(MediaType.APPLICATION_JSON)
			        .content("{\"title\" : \"New Fundoo Sample\", \"description\" : \"FundooNotes started\", \"color\" : \"red\", \"createdAt\" : \"date\" , \"updatedAt\" : \"date\"}")
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.title").exists())
					.andExpect(jsonPath("$.color").exists())
					.andExpect(jsonPath("$.description").exists())
					.andExpect(jsonPath("$.createdAt").exists())
					.andExpect(jsonPath("$.updatedAt").exists())
					.andExpect(jsonPath("$.title").value("New Fundoo Sample"))
					.andExpect(jsonPath("$.description").value("FundooNotes started"))
					.andExpect(jsonPath("$.color").value("red"))
					.andExpect(jsonPath("$.createdAt").value(date))
					.andExpect(jsonPath("$.updtedAt").value(date))
					.andDo(print());
		}
}
