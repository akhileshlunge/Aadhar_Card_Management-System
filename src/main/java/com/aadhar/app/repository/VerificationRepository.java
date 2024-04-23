package com.aadhar.app.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aadhar.app.model.VerificationToken;

@Transactional
public interface VerificationRepository extends JpaRepository<VerificationToken, Long>{

	VerificationToken findByToken(String token);
}
