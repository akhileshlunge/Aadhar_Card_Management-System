package com.aadhar.app.service;

import com.aadhar.app.model.VerificationToken;

public interface VerificationService {

	void save(VerificationToken token);

	VerificationToken getVerificationTokenByToken(String token);

}
