package com.thinkbig.filter;

	import java.util.logging.Logger;

	import javax.servlet.http.Cookie;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;

	import org.json.JSONObject;
	import org.springframework.web.servlet.ModelAndView;
	import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

	import com.auth0.jwt.JWT;
	import com.auth0.jwt.JWTVerifier;
	import com.auth0.jwt.algorithms.Algorithm;
	import com.auth0.jwt.exceptions.JWTVerificationException;
	import com.auth0.jwt.interfaces.DecodedJWT;

	public class RequestInterceptor extends HandlerInterceptorAdapter {


		
		private Logger logger = Logger.getLogger(getClass().getName());
		private final String SECRET_KEY;
		
		public RequestInterceptor(String key) {
			this.SECRET_KEY = key;
		}
		
		@Override
		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
				throws Exception {
			
		}

		@Override
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
				ModelAndView modelAndView) throws Exception {
			
		}

		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {
			

			logger.info("request URL is " + request.getRequestURL());
			String token = "";
			DecodedJWT jwt = null;
			Cookie[] cookies = request.getCookies();
			if(cookies != null)
				for (int i = 0; i < cookies.length; i++) {
					  String name = cookies[i].getName();
					  String value = cookies[i].getValue();
					  logger.info("request Cookie is " + name +" value is " + value);
						if(name.equalsIgnoreCase("token")) {
							token = value;
							break;
						}
					}
			if( token != null && token.length() > 0 ) {
				try {
				    Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
				    JWTVerifier verifier = JWT.require(algorithm)
				        .withIssuer("thinkbig")
				        .build(); //Reusable verifier instance
				    jwt = verifier.verify(token);
				} catch (JWTVerificationException exception){
				    
					// invalid request...
					response.setContentType("application/json");
					JSONObject returnObject = new JSONObject();
					returnObject.put("pg", "home");
					returnObject.put("rc", false);
					returnObject.put("m", "No user Logged in!!");
					response.getWriter().write(returnObject.toString());
					return false;
					
				}
			}
			else {
				
				// invalid request...
				response.setContentType("application/json");
				JSONObject returnObject = new JSONObject();
				returnObject.put("pg", "home");
				returnObject.put("rc", false);
				returnObject.put("m", "No user Logged in!!");
				response.getWriter().write(returnObject.toString());
				return false;
			}
			
			request.setAttribute("token", jwt);
			return true;
		}

}
