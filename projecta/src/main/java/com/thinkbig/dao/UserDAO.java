package com.thinkbig.dao;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.thinkbig.entity.User;

public interface UserDAO {
	boolean saveUser(User user);

	JSONObject fetchUserId(User user, String password, String rememberMe, HttpServletResponse response);
}
