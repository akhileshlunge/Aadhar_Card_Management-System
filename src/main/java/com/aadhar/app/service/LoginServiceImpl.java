package com.aadhar.app.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aadhar.app.model.Role;
import com.aadhar.app.model.User;
import com.aadhar.app.repository.LoginRepository;
import com.aadhar.app.repository.RoleRepository;
import com.aadhar.app.utility.CommonUtility;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	LoginRepository loginRepository;
	
	@Autowired
	RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = null;
		List<User> theUser = loginRepository.findByEmail(username);
		if (!theUser.isEmpty()) {
			user = theUser.get(0);
		}
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password!!");
		}

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				user.getEnabled(), true, true, user.getAccountNonLocked(), mapRolesToAuthorities(user.getRoles()));

	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public List<User> getUsersByEmail(String username) {
		return loginRepository.findByEmail(username);
	}

	@Override
	public User save(User theUser) {
		Role role = roleRepository.findById(3L).get();
		theUser.setRoles(Arrays.asList(role));
		return loginRepository.save(theUser);	
	}

	@Override
	public void updateTwoFactorAuthentication(Long id, boolean flag) {
		loginRepository.updateTwoFactorAuthentication(id, flag);
	}

	@Override
	public void updatePasswordById(long id, String encode) {
		loginRepository.updatePasswordById(id,encode);
	}

	@Override
	public List<User> findAll() {		
		return loginRepository.findAll();
	}

	@Override
	public User getUsersById(long id) {
		return loginRepository.getOne(id);
	}

	@Override
	public void updateUserDetails(User user) {
		User existingUser = loginRepository.getOne(user.getId());
		user.setCreatedDate(existingUser.getCreatedDate());
		user.setModifiedDate(CommonUtility.generateCurrentDateWithTimeStamp());
		user.setPassword(existingUser.getPassword());
		user.setSecretCode(existingUser.getSecretCode());		
		loginRepository.save(user);		
	}
}
