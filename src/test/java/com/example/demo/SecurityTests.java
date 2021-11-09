package com.example.demo;

import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SecurityTests {
	@Autowired
	private MockMvc mockMvc;
	private String username = "ojas";
	private String password = "456";

	@Test
	public void loginWithRegisteredUser() throws Exception{
		createUser(username, password);
		MvcResult mvcResult = performLoginAttempt(username, password);
		int actual = mvcResult.getResponse().getStatus();
		assertEquals(200, actual);
	}

	@Test
	public void loginWithUnregisteredUser() throws Exception{
		MvcResult mvcResult = performLoginAttempt("foo", "bar");
		int expected = 401;
		int actual = mvcResult.getResponse().getStatus();
		assertEquals(expected, actual);
	}

	@Test
	public void requestWithValidToken() throws Exception{
		createUser(username, password);
		MvcResult mvcResult = performLoginAttempt(username, password);
		String token = mvcResult.getResponse().getHeader("Authorization");
		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/" + username)
				.header("Authorization", token)).andExpect(status().isOk());
	}

	@Test
	public void requestWithInvalidToken() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/" + username)
				.header("Authorization", "Bilal")).andExpect(status().isUnauthorized());
	}

	private void createUser(String username, String password) throws Exception{
		CreateUserRequest request = createUserRequest(username, password);
		String registrationBody = objectToJSON(request);
		mockMvc.perform(MockMvcRequestBuilders.post(SecurityConstants.SIGN_UP_URL)
				.content(registrationBody).contentType("application/json"));
	}

	private MvcResult performLoginAttempt(String username, String password) throws Exception{
		Map loginData = new HashMap();
		loginData.put("username", username);
		loginData.put("password", password);
		String loginBody = objectToJSON(loginData);
		return mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.content(loginBody).contentType("application/json")).andReturn();
	}

	private CreateUserRequest createUserRequest(String username, String password) {
		CreateUserRequest request = new CreateUserRequest();
		request.setUsername(username);
		request.setPassword(password);
		request.setConfirmPassword(password);
		return request;
	}

	private String objectToJSON(Object object) throws Exception{
		return new ObjectMapper().writeValueAsString(object);
	}

}
