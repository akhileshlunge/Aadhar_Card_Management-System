package com.aadhar.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.aadhar.app.model.User;
@Transactional
public interface LoginRepository extends JpaRepository<User, Long>{
	
	List<User> findByEmail(String email);

	@Modifying
	@Query("update User u set u.twoFactorAuth = ?2 where u.id = ?1")
	void updateTwoFactorAuthentication(Long id, boolean flag);

	@Modifying
	@Query("update User u set u.password = ?2 where u.id = ?1")
	void updatePasswordById(long id, String encode);

}
