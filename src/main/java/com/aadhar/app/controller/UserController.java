package com.aadhar.app.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aadhar.app.model.AadharCard;
import com.aadhar.app.model.Address;
import com.aadhar.app.model.User;
import com.aadhar.app.service.AadharCardService;
import com.aadhar.app.utility.Constants;

@Controller
@RequestMapping("/user")
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	AadharCardService aadharCardService;

	@GetMapping("/dashboard")
	public String getUserDashboard(HttpSession session, Model model) {
		User user = null;
		try {
			user = (User) session.getAttribute("user");
		} catch (Exception e) {
			logger.error(Constants.USER_CONTROLLER + " dashboard " + Constants.USER_NOT_FOUND + " " + e.getMessage(), e);
		}
		String email = user.getEmail();
		logger.info(Constants.USER_CONTROLLER + " dashboard User is : " + user);
		AadharCard aadharCard = null;
		try {
			aadharCard = aadharCardService.getAadharCardByEmail(email);
		} catch (Exception e) {
			logger.error(
					Constants.USER_CONTROLLER + " dashboard " + Constants.AADHAR_CARD_NOT_FOUND + " " + e.getMessage());
		}
		if (null == aadharCard) {
			aadharCard = new AadharCard();
			aadharCard.setAddress(new Address());
			model.addAttribute("error", false);
			logger.info(Constants.USER_CONTROLLER + " dashboard no aadhar details" + Constants.AADHAR_CARD_NOT_FOUND);
		} else {
			logger.info(Constants.USER_CONTROLLER + " dashboard aadhar details found");
			model.addAttribute("error", true);
		}
		model.addAttribute("aadharCard", aadharCard);
		return "user/dashboard";
	}

}
