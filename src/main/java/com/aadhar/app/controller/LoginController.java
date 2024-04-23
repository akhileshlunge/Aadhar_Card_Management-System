package com.aadhar.app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aadhar.app.model.User;
import com.aadhar.app.model.VerificationToken;
import com.aadhar.app.service.EmailService;
import com.aadhar.app.service.LoginService;
import com.aadhar.app.service.VerificationService;
import com.aadhar.app.utility.CommonUtility;
import com.aadhar.app.utility.Constants;
import com.aadhar.app.utility.GoogleAuthUtility;
import com.aadhar.app.utility.UserUtility;

@Controller
public class LoginController {

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	LoginService loginService;

	@Autowired
	VerificationService verificationService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private GoogleAuthUtility googleAuthUtility;

	@Autowired
	EmailService emailService;

	@GetMapping("/")
	public String showHomePage() {
		logger.info(Constants.LOGIN_CONTROLLER + " home page");
		return "home";
	}

	@GetMapping("/login")
	public String showLoginPage() {
		logger.info(Constants.LOGIN_CONTROLLER + " login page");
		return "login";
	}

	@GetMapping("/signupform")
	public String showSignUpPage(Model theModel) {
		logger.info(Constants.LOGIN_CONTROLLER + " in signup page");
		theModel.addAttribute("theUser", new User());
		return "signupform";
	}

	@PostMapping("/signupprocess")
	public String saveUser(HttpSession session, @ModelAttribute("theUser") User theUser) {
		String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		boolean flag = true;
		if (null != theUser) {
			try {
				logger.info(Constants.LOGIN_CONTROLLER + " signupprocess for user : " + theUser.getEmail());
				String passKey = theUser.getPassword();
				theUser.setPassword(passwordEncoder.encode(theUser.getPassword()));
				theUser.setAccountNonLocked(true);
				theUser.setEnabled(false);

				theUser.setSecretCode(googleAuthUtility.getSecretKeyUrl(theUser.getEmail()));
				theUser.setTwoFactorAuth(false);

				theUser.setCreatedDate(CommonUtility.generateCurrentDateWithTimeStamp());
				theUser.setModifiedDate(CommonUtility.generateCurrentDateWithTimeStamp());

				logger.info(Constants.LOGIN_CONTROLLER + " signupprocess User created");

				try {
					theUser = loginService.save(theUser);
					logger.info(Constants.LOGIN_CONTROLLER + " signupprocess User saved successfully");
				} catch (IllegalArgumentException e) {
					flag = false;
					logger.error(Constants.LOGIN_CONTROLLER
							+ " signupprocess User creation failed with IllegalArgumentException" + e.getMessage(), e);
					return "signupfailed";
				} catch (Exception e) {
					flag = false;
					logger.error(Constants.LOGIN_CONTROLLER + " signupprocess User creation failed with Exception "
							+ e.getMessage(), e);
					return "signupfailed";
				}

				if (flag) {
					logger.info(Constants.LOGIN_CONTROLLER + " signupprocess Email process started");
					VerificationToken token = null;
					try {
						token = new VerificationToken(CommonUtility.generateVerificationToken(), theUser,
								CommonUtility.generateCurrentDateAfterOneWeek());
						verificationService.save(token);
					} catch (Exception e) {
						logger.error(
								Constants.LOGIN_CONTROLLER + " signupprocess token creation failed " + e.getMessage(),
								e);
					}

					logger.info(Constants.LOGIN_CONTROLLER + " " + token.getToken() + " token created for user "
							+ theUser.getFirstName());

					String link = baseUrl + "/activateuser/" + token.getToken() + "/" + theUser.getId();
					Map<String, Object> templateModel = new HashMap<>();
					templateModel.put("recipientName", theUser.getFirstName());
					templateModel.put("text", link);
					templateModel.put("key", passKey);
					templateModel.put("senderName", Constants.EMAIL_SENDER_NAME);

					logger.info(Constants.LOGIN_CONTROLLER + " signupprocess Email attributes created");

					try {
						emailService.sendMessageUsingThymeleafTemplate(theUser.getEmail(), Constants.EMAIL_SUBJECT,
								templateModel);
					} catch (IOException e) {
						logger.error(Constants.LOGIN_CONTROLLER + " signupprocess email failed with IOException "
								+ e.getMessage(), e);
					} catch (MessagingException e) {
						logger.error(Constants.LOGIN_CONTROLLER + " signupprocess email failed with MessagingException "
								+ e.getMessage(), e);
					} catch (Exception e) {
						logger.error(Constants.LOGIN_CONTROLLER + " signupprocess email failed with Exception "
								+ e.getMessage(), e);
					}
				}
			} catch (Exception e) {
				logger.error(Constants.LOGIN_CONTROLLER + " with Exception " + e.getMessage(), e);
			}
		} else {
			logger.error(Constants.LOGIN_CONTROLLER + " User is null ");
		}
		return "signupsuccess";
	}

	@GetMapping("/otpform")
	public String showOtpForm(HttpServletRequest request, HttpSession session, Model theModel) {
		User user = (User) session.getAttribute("user");
		if (null == user) {
			logger.error(Constants.LOGIN_CONTROLLER + " otpform " + Constants.USER_NOT_FOUND);
			return "redirect:/login";
		} else {
			logger.info(Constants.LOGIN_CONTROLLER + " otpform for user " + user.getEmail());
		}
		return "otpform";
	}

	@GetMapping("/authenticateotp")
	public String authenticateOTP(HttpServletRequest request, HttpSession session) {
		String otp = request.getParameter("otp");
		logger.info(Constants.LOGIN_CONTROLLER + " authenticateotp is " + otp);
		User user = null;
		try {
			user = (User) session.getAttribute("user");
		} catch (Exception e) {
			logger.error(
					Constants.LOGIN_CONTROLLER + " authenticateotp " + Constants.USER_NOT_FOUND + " " + e.getMessage(),
					e);
		}

		String key = googleAuthUtility.getKeyFromUrl(user.getSecretCode());
		if (null != key && googleAuthUtility.authenticated(key, Integer.parseInt(otp))) {

			logger.info(Constants.LOGIN_CONTROLLER + " " + Constants.OTP_AUTHENTICATION_SUCCESS);

			if (UserUtility.getRoleFromUser(user).equals(Constants.ROLE_OWNER)) {
				return "redirect:/owner/dashboard";
			} else if (UserUtility.getRoleFromUser(user).equals(Constants.ROLE_USER)) {
				return "redirect:/user/dashboard";
			} else if (UserUtility.getRoleFromUser(user).equals(Constants.ROLE_ADMIN)) {
				return "redirect:/admin/dashboard";
			}
		} else {
			logger.error(Constants.LOGIN_CONTROLLER + " " + Constants.OTP_AUTHENTICATION_FAILED);
			return "redirect:/otpform?error";
		}
		return "redirect:/otpform?error";
	}

	@GetMapping("/updateauthentication/{id}/{flag}")
	@ResponseBody
	public String updateTwoFactorAuthentication(@PathVariable("id") Long id, @PathVariable("flag") boolean flag) {
		String status = Constants.FAILED;
		if (id > 0 && (flag == true || flag == false)) {
			logger.info(
					Constants.LOGIN_CONTROLLER + " for updateauthentication Id is : " + id + " and Flag is : " + flag);
			try {
				loginService.updateTwoFactorAuthentication(id, flag);
				status = Constants.SUCCESS;
			} catch (Exception e) {
				status = Constants.FAILED;
				logger.error(Constants.LOGIN_CONTROLLER + " for updateauthentication update details failed "
						+ e.getMessage());
			}
		} else {
			status = Constants.FAILED;
			logger.error(Constants.LOGIN_CONTROLLER + " for updateauthentication ID or Flag is incorrect");
		}
		return status;
	}

	@PostMapping("/updatepassword")
	@ResponseBody
	public String changePassword(@RequestParam("id") long id, @RequestParam("password") String password) {
		String status = Constants.FAILED;
		if (id > 0 && null != password) {
			logger.info(Constants.LOGIN_CONTROLLER + " for updatepassword Id is : " + id);
			try {
				loginService.updatePasswordById(id, passwordEncoder.encode(password));
				status = Constants.SUCCESS;
			} catch (Exception e) {
				status = Constants.FAILED;
				logger.error(
						Constants.LOGIN_CONTROLLER + " for updatepassword update details failed " + e.getMessage());
			}

		} else {
			status = Constants.FAILED;
			logger.error(Constants.LOGIN_CONTROLLER + " for updatepassword ID or password is incorrect");
		}
		return status;
	}

	@GetMapping("/activateuser/{token}/{id}")
	public String activateUserByToken(@PathVariable("token") String token, @PathVariable("id") Long id, Model model) {
		boolean flag = false;
		User theUser = new User();
		if (StringUtils.hasText(token) && id > 0) {
			VerificationToken verificationToken = verificationService.getVerificationTokenByToken(token);
			if (null != verificationToken && verificationToken.getUser().getId() == id) {
				logger.info(
						Constants.LOGIN_CONTROLLER + " for activateuser Id is : " + id + " and Token is : " + token);
				theUser = verificationToken.getUser();
				theUser.setEnabled(true);
				theUser.setModifiedDate(CommonUtility.generateCurrentDateWithTimeStamp());
				try {
					loginService.updateUserDetails(theUser);
				} catch (Exception e) {
					logger.info(Constants.LOGIN_CONTROLLER + " for activateuser updation failed " + e.getMessage());
				}
			} else {
				flag = true;
				logger.error(Constants.LOGIN_CONTROLLER + " for activateuser ID and Token missmatched");
			}
		} else {
			flag = true;
			logger.error(Constants.LOGIN_CONTROLLER + " for activateuser ID or Token is incorrect");
		}

		model.addAttribute("error", flag);
		return "activation";
	}

}
