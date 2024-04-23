package com.aadhar.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aadhar.app.model.VerificationToken;
import com.aadhar.app.repository.VerificationRepository;

@Service
public class VerificationServiceImpl implements VerificationService{

	@Autowired
	VerificationRepository verificationRepository;
	
	@Override
	public void save(VerificationToken token) {
		verificationRepository.save(token);
	}

	@Override
	public VerificationToken getVerificationTokenByToken(String token) {
		return verificationRepository.findByToken(token);
	}

}
