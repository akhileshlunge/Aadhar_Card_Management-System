package com.aadhar.app.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.aadhar.app.model.AadharCard;
import com.aadhar.app.model.User;

@Component
public class ObjectMapperUtility {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private GoogleAuthUtility googleAuthUtility;

	public User getUserFromAadharCardDetails(AadharCard aadharCard) {
		User user = new User();
		user.setFirstName(aadharCard.getFirstName());
		user.setLastName(aadharCard.getLastName());
		user.setEmail(aadharCard.getEmail());
		user.setDisplayName(aadharCard.getFirstName());
		user.setPassword(passwordEncoder.encode(aadharCard.getAadharNo()));
		user.setEnabled(false);
		user.setAccountNonLocked(true);
		user.setCreatedDate(CommonUtility.generateCurrentDateWithTimeStamp());
		user.setModifiedDate(CommonUtility.generateCurrentDateWithTimeStamp());
		user.setTwoFactorAuth(false);
		user.setSecretCode(googleAuthUtility.getSecretKeyUrl(aadharCard.getEmail()));
		return user;
	}
}
