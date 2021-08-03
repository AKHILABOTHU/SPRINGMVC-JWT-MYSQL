package com.thinkbig.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.thinkbig.constants.UserRoles;
import com.thinkbig.entity.User;


@Service
public class TokenServiceImpl implements TokenService{

	@Autowired
	private Environment env;
	
	private long TIME_TO_LIVE_MILLI_SECONDS = TimeUnit.HOURS.toMillis(24);
	
	@Override
	public String getToken(User user) {
		
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);    

	    Algorithm algorithm = Algorithm.HMAC256(env.getProperty("secret_key"));
		String token = JWT.create()
				.withJWTId(user.getId()+"")
		        .withIssuer("thinkbig")
		        .withIssuedAt(now)
		        .withExpiresAt(new Date(nowMillis+TIME_TO_LIVE_MILLI_SECONDS))
		        .withArrayClaim("roles", new String[] { UserRoles.USER})
		        .sign(algorithm);
		
		return token;
	}

	@Override
	public String getExtendToken(User user) {
		// TODO Auto-generated method stub
		long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);    

	    Algorithm algorithm = Algorithm.HMAC256(env.getProperty("secret_key"));
		String token = JWT.create()
		        .withIssuer("thinkbig")
		        .withIssuedAt(now)
		        .withExpiresAt(new Date(nowMillis+86400000L*15))
		        .withArrayClaim("roles", new String[] { UserRoles.USER})
		        .sign(algorithm);
		
		return token;
	}

}
