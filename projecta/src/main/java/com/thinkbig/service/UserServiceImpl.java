package com.thinkbig.service;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkbig.dao.UserDAO;
import com.thinkbig.entity.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;
	
	@Override
	public boolean saveUser(User user) {
		// TODO Auto-generated method stub
		return userDAO.saveUser(user);
	}

	@Override
	public JSONObject ValidateUser(User user, String password, String rememberMe, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return userDAO.fetchUserId(user, password,rememberMe,response);
	}

}
