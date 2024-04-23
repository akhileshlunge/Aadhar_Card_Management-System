package com.aadhar.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aadhar.app.model.User;
import com.aadhar.app.service.LoginService;
import com.aadhar.app.utility.Constants;
import com.aadhar.app.utility.UserUtility;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private static Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	LoginService loginService;

	@GetMapping("/dashboard")
	public String getAdminDashboard() {
		logger.info(Constants.ADMIN_CONTROLLER + " Dashboard");
		return "admin/dashboard";
	}

	@GetMapping("/userupdatelist")
	public String getUserListToUpdate(Model model) {
		logger.info(Constants.ADMIN_CONTROLLER + " userupdatelist");
		model.addAttribute("userList", loginService.findAll());
		return "admin/userupdatelist";
	}

	@GetMapping("/updateuser/{id}")
	public String getUserToUpdate(@PathVariable("id") long id, Model model) {
		if (id > 0) {
			logger.info(Constants.ADMIN_CONTROLLER + " updateuser with id : " + id);
			User user = new User();
			if (id > 0) {
				user = loginService.getUsersById(id);
			}
			model.addAttribute("theUser", user);
			model.addAttribute("roleId", UserUtility.getRoleIdFromUser(user));
		} else {
			logger.error(Constants.ADMIN_CONTROLLER + " updateuser " + Constants.USER_ID_NOT_FOUND);
		}
		return "admin/userform";
	}

	@PostMapping("/updateuserdetails")
	public String updateUserDetails(User user) {
		if (null != user) {
			logger.info(Constants.ADMIN_CONTROLLER + " updateuserdetails with id : " + user.getId());
			loginService.updateUserDetails(user);
		} else {
			logger.error(Constants.ADMIN_CONTROLLER + " updateuserdetails " + Constants.USER_NOT_FOUND);
		}
		return "redirect:/admin/userupdatelist?success";
	}
}
