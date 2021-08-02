package com.thinkbig.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/authentication")
public class Authentication {
	
	
	
	
	
	
	
	
	
	
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
				returnObject.put("pg", "home");
				returnObject.put("m", " Log out successful!!");
				returnObject.put("rc", operationSucessful);
				return returnObject.toString();
			}

}
