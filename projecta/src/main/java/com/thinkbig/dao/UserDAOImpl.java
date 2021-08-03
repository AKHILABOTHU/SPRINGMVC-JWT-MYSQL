package com.thinkbig.dao;


import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.thinkbig.entity.User;
import com.thinkbig.service.TokenService;

@Repository
public class UserDAOImpl implements UserDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private TokenService tokenService;

	@Override
	@Transactional
	public boolean saveUser(User user) {
		// TODO Auto-generated method stub
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.save(user);
		return true;
	}

	@Override
	public JSONObject fetchUserId(User user, String password, String rememberMe, HttpServletResponse response) {
		// TODO Auto-generated method stub
		// Holds user information
				JSONObject data = new JSONObject();
				Session currentSession = sessionFactory.getCurrentSession();
				// validating email and display name with db
				Query<User> query = currentSession.createQuery("FROM User where email = :email or userName= :email");
				query.setParameter("email", user.getEmail());
				// Holds list of user records
				List<User> users = query.list();
				// Initializing dbUser object
				User dbUser = null;

				// checking users count, the count is greater than one it returns true, other
				// wise false
				if (!(users.size() > 0)) {
					data.put("rc", false);
					data.put("msg", "Invalid Email/Password");
				} else {
					// dbUser holds entire user information
					dbUser = users.get(0);
					try {
						if (dbUser.checkPassword(password)) {
							data.put("rc", true);
							// if remember me value is true it will be executed
							if (rememberMe == "true") {
								String token = tokenService.getExtendToken(dbUser);// passing dbUser to getExtendedToken method
																					// it creates token for 15 days
								Cookie c = new Cookie("token", token);// setting created token in cookie
								c.setPath("/");// The cookie will be available to all pages and subdirectories
								response.addCookie(c);// adding cookie to response object
								data.put("rc", true);
								data.put("page", "home");
								data.put("userId", dbUser.getId());
								data.put("m", dbUser.getEmail() + " Logged in!!");
							}
							// if remember me value false it will be executed
							else {
								String token = tokenService.getToken(dbUser);// passing dbUser to getToken method it creates
																				// token for 1 days
								Cookie c = new Cookie("token", token);// setting created token in cookie
								c.setPath("/");// The cookie will be available to all pages and subdirectories
								response.addCookie(c);// adding cookie to response object
								data.put("rc", true);
								data.put("pg", "home");
								data.put("userId", dbUser.getId());
								data.put("m", dbUser.getEmail() + " Logged in!!");
							}
						}
						// if password is wrong it will be executed
						else {
							data.put("rc", false);
							data.put("msg", "Invalid Email/Password");
						}
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return data;
			}
	}
	
