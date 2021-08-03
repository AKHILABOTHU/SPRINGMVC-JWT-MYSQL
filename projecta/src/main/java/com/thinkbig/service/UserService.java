package com.thinkbig.service;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.thinkbig.entity.User;


public interface UserService {

	boolean saveUser(User user);

	JSONObject ValidateUser(User user, String password, String rememberMe, HttpServletResponse response);

}
