package com.aadhar.app.config;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.aadhar.app.model.Role;
import com.aadhar.app.model.User;
import com.aadhar.app.service.LoginService;
import com.aadhar.app.utility.Constants;
import com.aadhar.app.utility.GoogleAuthUtility;
import com.aadhar.app.utility.UserUtility;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

	@Autowired
	LoginService loginService;
	
	@Autowired
	private GoogleAuthUtility googleAuthUtility;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		String userName = authentication.getName();
		
		logger.info("User login sucessfully : " + userName);

		User user = null;
		List<User> theUser = loginService.getUsersByEmail(userName);
		if (!theUser.isEmpty()) {
			user = theUser.get(0);
			user.setPassword(null);
		}

		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		session.setAttribute("secretKey", googleAuthUtility.getKeyFromUrl(user.getSecretCode()));
		
		if (null != user && user.getTwoFactorAuth()) {
			response.sendRedirect(request.getContextPath() + "/otpform");
		} else {
			if (UserUtility.getRoleFromUser(user).equals(Constants.ROLE_OWNER)) {
				response.sendRedirect(request.getContextPath() + "/owner/dashboard");
			} else if (UserUtility.getRoleFromUser(user).equals(Constants.ROLE_USER)) {
				response.sendRedirect(request.getContextPath() + "/user/dashboard");
			} else if (UserUtility.getRoleFromUser(user).equals(Constants.ROLE_ADMIN)) {
				response.sendRedirect(request.getContextPath() + "/admin/dashboard");
			} 
		}

	}

}
