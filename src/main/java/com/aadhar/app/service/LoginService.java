package com.aadhar.app.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.aadhar.app.model.User;

public interface LoginService extends UserDetailsService {

	List<User> getUsersByEmail(String username);

	User save(User theUser);

	void updateTwoFactorAuthentication(Long id, boolean flag);

	void updatePasswordById(long id, String encode);

	List<User> findAll();

	User getUsersById(long id);

	void updateUserDetails(User user);
}
