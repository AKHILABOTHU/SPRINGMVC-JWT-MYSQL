package com.thinkbig.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thinkbig.entity.User;
import com.thinkbig.service.TokenService;
import com.thinkbig.service.UserService;
import com.thinkbig.helper.LoginUser;

@Controller
@RequestMapping("/authentication")
public class Authentication {

	@Autowired
	private UserService userService;
	@Autowired
	private TokenService tokenService;

	/**
	 * 
	 * @param user
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	// User Registration
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/register")
	public @ResponseBody String userRegistration(@RequestBody User user, HttpServletResponse response)
			throws JSONException {
		// creating json object
		JSONObject returnObject = new JSONObject();
		// User objects holds entire Sign Up form data
		// saveUser method returns boolean value it is true/false
		boolean success = userService.saveUser(user);
		if (success) {
			// Generate Token Code
			String token = tokenService.getToken(user);
			// add to Cookie
			Cookie c = new Cookie("token", token);
			c.setPath("/");
			response.addCookie(c);
			returnObject.put("rc", true);
			returnObject.put("m", "Registration Completed");
		} else {
			returnObject.put("rc", false);
			returnObject.put("m", "Registration Failed");
		}
		return returnObject.toString();
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/loginUser")
	public @ResponseBody String LoginUser(@RequestBody LoginUser loginUser, HttpServletRequest request,HttpServletResponse response) throws JSONException {
		
		//Creating user object
		User user = new User();
		
		//Passing email parameter in setter method
		user.setEmail(loginUser.getEmail());
		
		//Validate user method returns user status in object format
		JSONObject userDetails= userService.ValidateUser(user, loginUser.getPassword(),loginUser.getRememberMe(), response);
		return userDetails.toString();	
	}
	
	
	
	
	

	// User logout
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/logout")
	public @ResponseBody String logOutUser(HttpServletRequest request, HttpServletResponse response)
			throws JSONException {
		boolean operationSucessful = true;
		JSONObject returnObject = new JSONObject();
		Cookie c = new Cookie("token", "");
		c.setPath("/");
		response.addCookie(c);
		operationSucessful = true;
		returnObject.put("pg", "login");
		returnObject.put("m", " Log out successful!!");
		returnObject.put("rc", operationSucessful);
		return returnObject.toString();
	}

}
