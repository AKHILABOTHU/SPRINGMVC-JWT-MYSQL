package com.thinkbig.service;

import com.thinkbig.entity.User;

public interface TokenService {
	public String getToken(User user);

	public String getExtendToken(User dbUser);

}
