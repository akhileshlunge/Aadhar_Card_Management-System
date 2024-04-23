package com.aadhar.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.aadhar.app.model.AadharCard;

@Transactional
public interface AadharCardRepository extends JpaRepository<AadharCard, Long> {
	
	boolean existsAadharCardByAadharNo(String aadharNo);
	
	AadharCard getAadharCardByEmail(String email);

}
